package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.GeriatricFactorRepository;
import eu.city4age.dashboard.api.persist.NUIRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.DailyMeasure;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;

/**
 * @author milos.holclajtner
 *
 */
@Component
@Transactional("transactionManager")
@Path(MeasuresService.PATH)
public class MeasuresService {

	public static final String PATH = "measures";

	static protected Logger logger = LogManager.getLogger(MeasuresService.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	static protected String SQL_SELECT = "SELECT AVG (vm.measure_value * dv.derivation_weight) AS avg, CASE WHEN STDDEV(vm.measure_value * dv.derivation_weight)/AVG(vm.measure_value*dv.derivation_weight) IS NULL THEN 0 ELSE STDDEV(vm.measure_value * dv.derivation_weight)/AVG(vm.measure_value*dv.derivation_weight) END AS stDev, PERCENTILE_CONT(0.25) WITHIN GROUP(ORDER BY vm.measure_value*dv.derivation_weight DESC)/AVG(vm.measure_value*dv.derivation_weight) AS best25Perc, (PERCENTILE_CONT(0.25) WITHIN GROUP(ORDER BY vm.measure_value*dv.derivation_weight DESC)-AVG(vm.measure_value*dv.derivation_weight))/AVG(vm.measure_value*dv.derivation_weight) AS delta25PercAvg FROM variation_measure_value AS vm INNER JOIN cd_detection_variable AS dv ON dv. ID = vm.measure_type_id INNER JOIN time_interval AS ti ON ti. ID = vm.time_interval_id WHERE ti.interval_start > '";
	static protected String SQL_AND = "' AND ti.interval_end < '";
	static protected String SQL_END = "'";

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private NUIRepository nuiRepository;

	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("computeMeasures")
	public Response computeMeasures() throws JsonProcessingException, IOException {

		logger.info("computeMeasures");

		// lastYearMonth hardcoded for pilotCode
		// bice lastSubmited i lastComputed(servis upisuje) date

		List<Pilot> pilots = pilotRepository.findAll();

		for (Pilot pilot : pilots) {
			logger.info("getLastComputed before Nuis: " + pilot.getLastComputed());
			computeNuisForPilot(pilot.getPilotCode(), pilot.getLastComputed());
		}

		for (Pilot pilot : pilots) {
			logger.info("getLastComputed before GESs: " + pilot.getLastComputed());
			computeGESsForPilot(pilot.getPilotCode(), pilot.getLastComputed());
		}

		// NumericIndicatorValue nui = createMonthlyMeasure();
		return Response.ok().build();
	}

	private void computeGESsForPilot(String pilotCode, YearMonth lastComputedYearMonth) {
		logger.info("computeGESsForPilot");
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		while (!currentComputedYearMonth.equals(currentYearMonth)) {
			computeGESsFor1Month(pilotCode, currentComputedYearMonth);
			currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
		}
	}

	private void computeNuisForPilot(String pilotCode, YearMonth lastComputedYearMonth) {
		logger.info("computeNuisForPilot");
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		while (!currentComputedYearMonth.equals(currentYearMonth)) {
			Timestamp startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
			Timestamp endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
			computeNuisFor1Month(pilotCode, startOfMonth, endOfMonth);
			currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
		}
	}

	private void computeGESsFor1Month(String pilotCode, YearMonth yearMonth) {
		logger.info("computeGESsFor1Month");
		logger.info("pilotCode: " + pilotCode);
		Timestamp timestamp = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
		List<NumericIndicatorValue> nuisThisMonth = nuiRepository.getNuisFor1Month(pilotCode, timestamp);
		logger.info("timestamp: " + timestamp);
		logger.info("nuisThisMonth: " + nuisThisMonth.size());
		nuisThisMonth.sort(new Comparator<NumericIndicatorValue>() {
			@Override
			public int compare(NumericIndicatorValue o1, NumericIndicatorValue o2) {
				int value1 = o1.getDetectionVariable().getId().compareTo(o2.getDetectionVariable().getId());
				if (value1 == 0) {
					int value2 = o1.getUserInRole().getId().compareTo(o2.getUserInRole().getId());
					if (value2 == 0)
						return o2.getTimeInterval().getId().compareTo(o2.getTimeInterval().getId());
					else
						return value2;
				} else
					return value1;
			}
		});
		BigDecimal sum = new BigDecimal(0);
		BigDecimal dn;
		BigDecimal tn = new BigDecimal(0);
		UserInRole previousUir = null;
		NumericIndicatorValue previousNuiThisMonth = null;
		BigDecimal previousSum = null;
		for (NumericIndicatorValue nuiThisMonth : nuisThisMonth) {
			UserInRole currentUir = nuiThisMonth.getUserInRole();
			if (previousUir != null && !previousUir.equals(currentUir)) {
				createGes(previousNuiThisMonth, previousSum);
			}
			previousUir = currentUir;
			previousNuiThisMonth = nuiThisMonth;
			previousSum = sum;
			Long minNuiId = nuiRepository.findMinId(nuiThisMonth.getDetectionVariable(), nuiThisMonth.getUserInRole());
			NumericIndicatorValue nuiMonthZero = null;
			BigDecimal weight = new BigDecimal(0);
			if (minNuiId != null) {
				nuiMonthZero = nuiRepository.findOne(minNuiId);
				if (nuiMonthZero != null) {
					weight = nuiThisMonth.getDetectionVariable().getDerivationWeight();
					logger.info("nuiThisMonth.getNuiValue(): " + nuiThisMonth.getNuiValue());
					logger.info("nuiMonthZero.getNuiValue(): " + nuiMonthZero.getNuiValue());
					dn = (nuiThisMonth.getNuiValue().subtract(nuiMonthZero.getNuiValue()))
							.divide(nuiMonthZero.getNuiValue(), 2, RoundingMode.HALF_UP);
					logger.info("dn: " + dn);
					if (dn.compareTo(new BigDecimal(.25)) < 0) {
						if (dn.compareTo(new BigDecimal(.1)) < 0) {
							if (dn.compareTo(new BigDecimal(-.1)) < 0) {
								if (dn.compareTo(new BigDecimal(-.25)) < 0) {
									tn = new BigDecimal(1);
								} else {
									tn = new BigDecimal(2);
								}
							} else {
								tn = new BigDecimal(3);
							}
						} else {
							tn = new BigDecimal(4);
						}
					} else {
						tn = new BigDecimal(5);
					}
				}
			}
			logger.info("tn: " + tn.toString());
			logger.info("weight: " + weight.toString());
			sum = sum.add(tn.multiply(weight));
		}
		if (nuisThisMonth != null && !nuisThisMonth.isEmpty())
			createGes(nuisThisMonth.get(nuisThisMonth.size() - 1), sum);

	}

	private void createGes(NumericIndicatorValue nui, BigDecimal gesValue) {
		logger.info("createGes");
		List<VariationMeasureValue> mmsThisMonth = variationMeasureValueRepository
				.findByUserInRoleId(nui.getUserInRole().getId(), nui.getTimeInterval().getIntervalStart());
		BigDecimal sum = new BigDecimal(0);
		BigDecimal ds;
		BigDecimal ts;
		for (VariationMeasureValue mmThisMonth : mmsThisMonth) {
			VariationMeasureValue mmMonthZero = variationMeasureValueRepository.findOne(variationMeasureValueRepository
					.findMinId(mmThisMonth.getDetectionVariable(), mmThisMonth.getUserInRole()));
			BigDecimal weight = mmThisMonth.getDetectionVariable().getDerivationWeight();
			ds = (mmThisMonth.getMeasureValue().subtract(mmMonthZero.getMeasureValue()))
					.divide(mmMonthZero.getMeasureValue(), 2, RoundingMode.HALF_UP);
			if (ds.compareTo(new BigDecimal(.25)) < 0) {
				if (ds.compareTo(new BigDecimal(.1)) < 0) {
					if (ds.compareTo(new BigDecimal(-.1)) < 0) {
						if (ds.compareTo(new BigDecimal(-.25)) < 0) {
							ts = new BigDecimal(1);
						} else {
							ts = new BigDecimal(2);
						}
					} else {
						ts = new BigDecimal(3);
					}
				} else {
					ts = new BigDecimal(4);
				}
			} else {
				ts = new BigDecimal(5);
			}
			logger.info("ts: " + ts.toString());
			logger.info("weight: " + weight.toString());
			sum = sum.add(ts.multiply(weight));
		}

		/*
		 * logger.info("pre merge");
		 * timeIntervalRepository.merge(nui.getTimeInterval());
		 * logger.info("posle merge");
		 */

		NumericIndicatorValue newNui = nuiRepository.findOne(nui.getId());
		nuiRepository.flush();

		GeriatricFactorValue ges = new GeriatricFactorValue();
		BigDecimal total = gesValue.add(sum);
		logger.info("gesValue: " + gesValue.toString());
		logger.info("sum: " + sum.toString());
		logger.info("total: " + total.toString());
		ges.setGefValue(total.setScale(2, RoundingMode.HALF_UP));

		/*
		 * logger.info("pre get ti id"); Long tiId =
		 * nui.getTimeInterval().getId(); logger.info("pre get ti");
		 * TimeInterval ti = timeIntervalRepository.findOne(tiId );
		 * logger.info("pre set ti");
		 */
		ges.setTimeInterval(newNui.getTimeInterval());

		/*
		 * logger.info("pre get ti"); TimeInterval ti =
		 * getOrCreateTimeInterval(Timestamp.valueOf("2017-07-21 00:00:00"),
		 * TypicalPeriod.MONTH); logger.info("pre set ti");
		 * ges.setTimeInterval(ti);
		 */
		ges.setDetectionVariable(nui.getDetectionVariable());
		ges.setUserInRole(nui.getUserInRole());
		logger.info("pre save");
		geriatricFactorRepository.save(ges);
	}

	private void computeNuisFor1Month(String pilotCode, Timestamp startOfMonth, Timestamp endOfMonth) {
		logger.info("computeNuisFor1Month");
		List<VariationMeasureValue> list = variationMeasureValueRepository.findByPilotCode(pilotCode, startOfMonth,
				endOfMonth);
		list.sort(new Comparator<VariationMeasureValue>() {
			@Override
			public int compare(VariationMeasureValue o1, VariationMeasureValue o2) {
				int value1 = o1.getDetectionVariable().getId().compareTo(o2.getDetectionVariable().getId());
				if (value1 == 0) {
					int value2 = o1.getUserInRole().getId().compareTo(o2.getUserInRole().getId());
					if (value2 == 0)
						return o2.getTimeInterval().getId().compareTo(o2.getTimeInterval().getId());
					else
						return value2;
				} else
					return value1;
			}
		});
		Long id = 0L;
		for (VariationMeasureValue vm : list) {
			String formula = MeasuresService.SQL_SELECT + startOfMonth + MeasuresService.SQL_AND + endOfMonth
					+ MeasuresService.SQL_END;
			List<NumericIndicatorValue> nuis = createAllNuis(vm, formula);
			if (!id.equals(vm.getDetectionVariable().getId())) {
				// ovo mora da bude za sve usere na tom pilotu a ne za svakog
				// usera
				createDailyMeasure(vm, DailyMeasure.ALL, pilotCode, formula);
				// pdvs.add(pdv);
			}
			id = vm.getDetectionVariable().getId();
		}
	}

	private PilotDetectionVariable createDailyMeasure(VariationMeasureValue vm, DailyMeasure dailyMeasure,
			String pilotCode, String formula) {
		logger.info("createDailyMeasure");
		PilotDetectionVariable pdv = new PilotDetectionVariable();
		pdv.setDetectionVariable(vm.getDetectionVariable());
		pdv.setFormula(dailyMeasure.getFormula());
		pdv.setPilotCode(pilotCode);
		pdv.setDerivedDetectionVariable(null);
		pdv.setValidFrom(new Date());
		pdv.setFormula(formula);
		pilotDetectionVariableRepository.save(pdv);
		return pdv;
	}

	private List<NumericIndicatorValue> createAllNuis(VariationMeasureValue vm, String formula) {
		logger.info("createAllNuis");
		List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();

		formula += " AND vm.user_in_role_id = " + vm.getUserInRole().getId().toString() + " AND vm.measure_type_id = "
				+ vm.getDetectionVariable().getId().toString() + ";";
		Query sql = variationMeasureValueRepository.getEntityManager().createNativeQuery(formula);
		variationMeasureValueRepository.getEntityManager().getEntityManagerFactory()
				.addNamedQuery("VariationMeasureValue.dynamic", sql);
		Query namedQuery = variationMeasureValueRepository.getEntityManager()
				.createNamedQuery("VariationMeasureValue.dynamic");

		Object[] nuiValue = (Object[]) namedQuery.getSingleResult();

		for (int i = 0; i < nuiValue.length; i++) {
			NumericIndicatorValue nui = create1Nui(vm, nuiValue[i]);
			nuis.add(nui);
		}
		return nuis;
	}

	private NumericIndicatorValue create1Nui(VariationMeasureValue vm, Object nuiValue) {
		logger.info("create1Nui");
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(new BigDecimal(nuiValue.toString()));
		} else {
			nui.setNuiValue(new BigDecimal(0));
		}
		nui.setUserInRole(vm.getUserInRole());
		nui.setDetectionVariable(vm.getDetectionVariable());
		Timestamp startOfAMonth = startOfAMonth(vm.getTimeInterval().getIntervalStart());
		nui.setTimeInterval(getOrCreateTimeInterval(startOfAMonth, TypicalPeriod.MONTH));
		nuiRepository.save(nui);
		return nui;
	}

	private Timestamp startOfAMonth(Timestamp anyTimestamp) {
		logger.info("startOfAMonth");
		Calendar cal = Calendar.getInstance();
		cal.setTime(anyTimestamp);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp(cal.getTimeInMillis());
	}

	public TimeInterval getOrCreateTimeInterval(Timestamp intervalStart, TypicalPeriod typicalPeriod) {
		logger.info("getOrCreateTimeInterval");
		TimeInterval ti = timeIntervalRepository.findByIntervalStartAndTypicalPeriod(intervalStart,
				typicalPeriod.getDbName());
		if (ti == null) {
			ti = new TimeInterval();
			ti.setIntervalStart(intervalStart);
			ti.setTypicalPeriod(typicalPeriod.getDbName());
			timeIntervalRepository.save(ti);
			timeIntervalRepository.flush();
		}
		return ti;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("configureDailyMeasures")
	public Response configureDailyMeasures(String json) throws JsonProcessingException, IOException, ProcessingException {
		
		/*
		 * Avoiding to use readerFor method instead because of conflict with
		 * older version of jackson jars in GLASSFISH_HOME/glassfish/modules of
		 * Glassfish 4.1.1 which then would have to be replaced.
		 */
		@SuppressWarnings("deprecation")
		ConfigureDailyMeasuresDeserializer data = objectMapper.reader(ConfigureDailyMeasuresDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		List<DetectionVariable> dvs = detectionVariableRepository.findAll();
		List<PilotDetectionVariable> pdvs = pilotDetectionVariableRepository.findAll();
		detectionVariableRepository.flush();
		return Response.ok(objectMapper.writeValueAsString(pdvs)).build();
	}

}