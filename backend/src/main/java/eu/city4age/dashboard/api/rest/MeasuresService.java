package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.GeriatricFactorRepository;
import eu.city4age.dashboard.api.persist.NUIRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.persist.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.persist.ViewGefValuesPersistedSourceGesTypesRepository;
import eu.city4age.dashboard.api.persist.ViewMeaNuiDerivationPerPilotRepository;
import eu.city4age.dashboard.api.persist.ViewNuiValuesPersistedSourceMeaTypesRepository;
import eu.city4age.dashboard.api.persist.ViewPilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;
import eu.city4age.dashboard.api.pojo.domain.ViewNuiValuesPersistedSourceMeaTypes;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.json.view.View;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private ViewNuiValuesPersistedSourceMeaTypesRepository viewNuiValuesPersistedSourceMeaTypesRepository;

	@Autowired
	private ViewPilotDetectionVariableRepository viewPilotDetectionVariableRepository;

	@Autowired
	private ViewMeaNuiDerivationPerPilotRepository viewMeaNuiDerivationPerPilotRepository;

	@Autowired
	private ViewGefValuesPersistedSourceGesTypesRepository viewGefValuesPersistedSourceGesTypesRepository;
	
	@Autowired
	Environment environment;

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@GET
	@ApiOperation("Get daily measures for care recipient within given geriatric subfactor.")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.VariationMeasureValueView.class)
	@Path("getDailyMeasures/userInRoleId/{userInRoleId}/gesId/{gesId}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userInRoleId", value = "id of care recipient", required = false, dataType = "long", paramType = "path", defaultValue = "1"),
			@ApiImplicitParam(name = "gesId", value = "id of geriatric subfactor", required = false, dataType = "long", paramType = "path", defaultValue = "2") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = VariationMeasureValue.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getDailyMeasures(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("gesId") Long gesId) throws JsonProcessingException {

		List<VariationMeasureValue> measures = new ArrayList<VariationMeasureValue>();
		try {
			measures = variationMeasureValueRepository.findByUserAndGes(userInRoleId, gesId);

		} catch (Exception e) {
			logger.info("getDailyMeasures REST service - query exception: ", e);
		}

		return Response
				.ok(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures))
				.build();

	}
	
	@Scheduled(cron = "0 0 0 * * *", zone = "UTC")
	public void cronJob() throws UnknownHostException {
		String host = InetAddress.getLocalHost().getHostAddress();
		String port = environment.getProperty("local.server.port");
		String appName = environment.getProperty("spring.application.name");
		String uri = "http://" + host + ":" + port + "/" + appName + "/rest/measures/computeFromMeasures";
		ResponseEntity<String> response = restTemplate().getForEntity(uri, String.class);
		if (!response.getStatusCode().equals(HttpStatus.OK)) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("computeFromMeasures")
	public Response computeFromMeasures() throws JsonProcessingException, IOException {

		List<Pilot> pilots = pilotRepository.findAll();

		for (Pilot pilot : pilots)
			computeNuisForPilot(pilot.getPilotCode(), pilot.getLastComputed());

		for (Pilot pilot : pilots)
			computeGESsForPilot(pilot.getPilotCode(), pilot.getLastComputed());

		return Response.ok().build();
	}
	
	
	private void computeGESsForPilot(String pilotCode, YearMonth lastComputedYearMonth) {
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		Timestamp startOfMonth;
		Timestamp endOfMonth;

		List<PilotDetectionVariable> deriveds = pilotDetectionVariableRepository.findDerived(pilotCode, DetectionVariableType.GEF);
		BigDecimal weight = new BigDecimal(1);
		if(deriveds != null && deriveds.size() > 0) weight = deriveds.get(0).getDerivationWeight();
		
		if (currentYearMonth.compareTo(currentComputedYearMonth) > 1) {
			while (!currentComputedYearMonth.equals(currentYearMonth)) {
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				Set<Long> users = computeGESsFor1Month(pilotCode, startOfMonth, endOfMonth);
				if(users != null && users.size() > 0) computeFor1Month(users, DetectionVariableType.GEF, weight, pilotCode, startOfMonth, endOfMonth);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
				if(deriveds != null && deriveds.size() > 0) computeDerivedFor1Month(users, DetectionVariableType.GFG, pilotCode, startOfMonth, endOfMonth);
			}
		}
	}

	private void computeDerivedFor1Month(Set<Long> users, DetectionVariableType type, String pilotCode, Timestamp startOfMonth,
			Timestamp endOfMonth) {
		
		logger.info("computeDerivedFor1Month");
		logger.info("pilotCode: " + pilotCode);
		logger.info("type: " + type);

		BigDecimal weight = new BigDecimal(1);
		List<PilotDetectionVariable> deriveds = pilotDetectionVariableRepository.findDerived(pilotCode, type);
		if(deriveds != null && deriveds.size() > 0) {
			weight = deriveds.get(0).getDerivationWeight();
			logger.info("dv type" + deriveds.get(0).getDetectionVariable().getDetectionVariableType());
			logger.info("ddv type" + deriveds.get(0).getDerivedDetectionVariable().getDetectionVariableType());
		}
		
		if(users != null && users.size() > 0) computeFor1Month(users, type, weight, pilotCode, startOfMonth, endOfMonth);
		if(deriveds != null && deriveds.size() > 0) computeDerivedFor1Month(users, deriveds.get(0).getDerivedDetectionVariable().getDetectionVariableType(), pilotCode, startOfMonth, endOfMonth);
	
	}

	
	private void computeFor1Month(Set<Long> users, DetectionVariableType factor, BigDecimal weight, String pilotCode, Timestamp startOfMonth,
			Timestamp endOfMonth) {

		logger.info("computeFor1Month: " + factor);

		for (Long userId : users) {
			List<ViewGefValuesPersistedSourceGesTypes> list = viewGefValuesPersistedSourceGesTypesRepository
					.findAllFor1MonthByUserAndDerived(userId, startOfMonth, endOfMonth, factor);
			
			Map<Long, BigDecimal> sumW2 = new HashMap<Long, BigDecimal>();
			
			for (ViewGefValuesPersistedSourceGesTypes ges: list)
				if(sumW2.containsKey(ges.getId().getDerivedDetectionVariableId()))
					sumW2.put(ges.getId().getDerivedDetectionVariableId(), sumW2.get(ges.getId().getDerivedDetectionVariableId()).add(ges.getDerivationWeight()));
				else
					sumW2.put(ges.getId().getDerivedDetectionVariableId(), ges.getDerivationWeight());

			if (list != null && list.size() > 0) {
				
				list.sort(new Comparator<ViewGefValuesPersistedSourceGesTypes>() {
					@Override
					public int compare(ViewGefValuesPersistedSourceGesTypes o1, ViewGefValuesPersistedSourceGesTypes o2) {
						return o1.getId().getDerivedDetectionVariableId()
									.compareTo(o2.getId().getDerivedDetectionVariableId());
					} 
				});
				Long previousDerivedDvId = 0L;
				BigDecimal sum = new BigDecimal(0);
				for(ViewGefValuesPersistedSourceGesTypes ges: list) {
					Long dvId = ges.getId().getDetectionVariableId();
					Long derivedDvId = ges.getId().getDerivedDetectionVariableId();
					logger.info("dvId: " + dvId);
					logger.info("derivedDvId: " + derivedDvId);
					BigDecimal gefValue = ges.getGefValue();
					BigDecimal gesDerivationWeight = ges.getDerivationWeight();
					logger.info("gefValue: " + gefValue);
					logger.info("gesDerivationWeight: " + gesDerivationWeight);
					BigDecimal multiply = gefValue.multiply(gesDerivationWeight.abs());
					logger.info("multiply: " + multiply);
					if(sumW2 != null && !sumW2.equals(new BigDecimal(0))) {
						BigDecimal divisor = sumW2.get(derivedDvId);
						logger.info("divisor: " + divisor);
						multiply = multiply.divide(divisor.abs(), 2, RoundingMode.HALF_UP);
						logger.info("new multiply: " + multiply);
					}
					if(!previousDerivedDvId.equals(derivedDvId) && previousDerivedDvId != 0L) {
						logger.info("sumW2: " + sumW2);
						logger.info("before createGFV - gesValue: " + sum);
						createGFV(previousDerivedDvId, sum, weight, startOfMonth, endOfMonth, userId, pilotCode);
						sum = multiply;
						logger.info("sum equals: " + sum);
					} else {
						sum = sum.add(multiply);
						logger.info("sum add: " + sum);
					}
					previousDerivedDvId = derivedDvId;
				}				

				if (list != null && list.size() > 0) {
					logger.info("last createGFV");
					logger.info("gesValue: " + sum);
					createGFV(previousDerivedDvId, sum, weight, startOfMonth, endOfMonth, userId, pilotCode);
				}
			}
		}
	}

	private void computeNuisForPilot(String pilotCode, YearMonth lastComputedYearMonth) {
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		Timestamp startOfMonth;
		Timestamp endOfMonth;
		if (currentYearMonth.compareTo(currentComputedYearMonth) > 1) {
			while (!currentComputedYearMonth.equals(currentYearMonth)) {
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				computeNuisFor1Month(pilotCode, startOfMonth, endOfMonth);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
			}
		}
	}

	private Set<Long> computeGESsFor1Month(String pilotCode, Timestamp startOfMonth, Timestamp endOfMonth) {
		logger.info("computeGESsFor1Month");
		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository.findAllForMonthByPilotCode(pilotCode,
				startOfMonth, endOfMonth);

		Set<Long> users = null;
		
		if (vmsMonthly != null && vmsMonthly.size() > 0) {

			users = new HashSet<Long>();

			for (VariationMeasureValue vmv : vmsMonthly) {
				users.add(vmv.getUserInRole().getId());
			}

			if (users != null && users.size() > 0) {

				for (Long userId : users) { // iterate users
					
					List<ViewPilotDetectionVariable> gesList = viewPilotDetectionVariableRepository.findAllGes(userId);
	
					if (gesList != null && gesList.size() > 0) {
						for (ViewPilotDetectionVariable ges : gesList) {
							
							BigDecimal gesValue = null;

							List<ViewPilotDetectionVariable> meaList = viewPilotDetectionVariableRepository
									.findAllMeaGes(userId, ges.getId().getDetectionVariableId()); // all measure-ges relations
							
							logger.info("findAllMeaGes size: " + meaList.size());
							
							if (meaList != null && meaList.size() > 0) {

								BigDecimal sumW1 = new BigDecimal(0);

								List<ViewPilotDetectionVariable> newMeaList = new ArrayList<ViewPilotDetectionVariable>();
								
								for(ViewPilotDetectionVariable mea : meaList) {
									List<VariationMeasureValue> vmvs = variationMeasureValueRepository.findByUserInRoleId(userId, mea.getId().getDetectionVariableId(), startOfMonth, endOfMonth);

									if(vmvs != null) logger.info("vmvs size: " + vmvs.size());
									
									if(vmvs != null && vmvs.size() > 0) {
										newMeaList.add(mea);
										BigDecimal w1 = mea.getDerivationWeight();
										logger.info("w1: " + w1);
										sumW1 = sumW1.add(w1.abs());
									}
								}

								logger.info("sumW1: " + sumW1);
								
								List<BigDecimal> derivedMeas = new ArrayList<BigDecimal>();
								List<BigDecimal> weights = new ArrayList<BigDecimal>();


								for (int i = 0; i < newMeaList.size(); i++) { //iterate measures for every derived ges

									logger.info("findAllMeaGes dvId: " + newMeaList.get(i).getId().getDetectionVariableId());
									logger.info("findAllMeaGes uirId: " + newMeaList.get(i).getId().getUserInRoleId());


									BigDecimal derivedMea = computeDerivedMea(pilotCode, startOfMonth, endOfMonth,
											newMeaList.get(i), userId, newMeaList.size());
									
									BigDecimal weight = new BigDecimal(0);
									if(sumW1 != null && !sumW1.equals(new BigDecimal(0)))
										weight = newMeaList.get(i).getDerivationWeight().abs().divide(sumW1, 2, RoundingMode.HALF_UP);
									
									logger.info("weight W1 : " + weight);
									logger.info("sumW1: " + sumW1);
									
									if (derivedMea != null) {
										derivedMeas.add(derivedMea);
										weights.add(weight);
									}

								}
								
								logger.info("gesValue initial: " + gesValue);
								
								if (derivedMeas.size() > 0) {
									gesValue = new BigDecimal(0);
									for (int i = 0; i < derivedMeas.size(); i++) {
										gesValue = gesValue.add(derivedMeas.get(i).multiply(weights.get(i).abs()));
										logger.info("derivedMeas.get(i): " + derivedMeas.get(i));
										logger.info("weights.get(i): " + weights.get(i));
										logger.info("gesValue after add: " + gesValue);
									} 
								}

							}
							
							if(gesValue != null) {
								logger.info("gesValue before createGFG: " + gesValue);
								createGFV(ges.getId().getDetectionVariableId(),
										gesValue, ges.getDerivationWeight(), startOfMonth,
										endOfMonth, userId, pilotCode);
							}
						} 
						
					}
				}
			}
		}

		Pilot pilot = pilotRepository.findByPilotCode(pilotCode);
		pilot.setLatestVariablesComputed(new Date());
		pilotRepository.save(pilot);
		
		return users;
	}
	
	private BigDecimal computeDerivedMea(String pilotCode, Timestamp startOfMonth, Timestamp endOfMonth,
			ViewPilotDetectionVariable vpdv, Long userId, int size) {
		logger.info("user: " + userId);
		logger.info("meaId: " + vpdv.getId().getDetectionVariableId());
		logger.info("startOfMonth: " + startOfMonth);

		String dtp = vpdv.getDefaultTypicalPeriod();
		
		BigDecimal result = null;
		
		int signum = vpdv.getDerivationWeight().signum();
		if(signum == 0) signum = 1;
		
		if (dtp.equals("DAY") || dtp.equals("1WK")) { // if DAY or 1WK
			logger.info("DAY || 1WK");
			
			result = new BigDecimal(0);

			Long meaId = vpdv.getId().getDetectionVariableId();
			
			List<ViewMeaNuiDerivationPerPilot> list = viewMeaNuiDerivationPerPilotRepository.findAllNuiForMea(meaId, pilotCode);

			for (ViewMeaNuiDerivationPerPilot nuiVpdv : list) { // for each of 4 nuis

				ViewNuiValuesPersistedSourceMeaTypes userEntryForNui1Month = viewNuiValuesPersistedSourceMeaTypesRepository
						.findNuiFor1Month(startOfMonth, nuiVpdv.getDerivedNuiId(), userId); // get nui value

				if (userEntryForNui1Month != null) {

					BigDecimal dn;
					BigDecimal tn = new BigDecimal(3);

					Long nuiMonthZeroId = nuiRepository.findMonthZero(nuiVpdv.getDerivedNuiId(),
							userId);
					NumericIndicatorValue nuiMonthZero = null;

					BigDecimal weight = new BigDecimal(signum).divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);

					logger.info("weight: " + weight);
	
					if (nuiMonthZeroId != null) {
						nuiMonthZero = nuiRepository.findOne(nuiMonthZeroId);
						if (nuiMonthZero != null && nuiMonthZero.getNuiValue().compareTo(new BigDecimal(0)) > 0) {
							dn = (userEntryForNui1Month.getNuiValue().subtract(nuiMonthZero.getNuiValue()))
									.divide(nuiMonthZero.getNuiValue(), 2, RoundingMode.HALF_UP);

							if (dn.compareTo(new BigDecimal(.25)) < 0)
								if (dn.compareTo(new BigDecimal(.1)) < 0)
									if (dn.compareTo(new BigDecimal(-.1)) < 0)
										if (dn.compareTo(new BigDecimal(-.25)) < 0)
											tn = new BigDecimal(1);
										else tn = new BigDecimal(2);
									else tn = new BigDecimal(3);
								else tn = new BigDecimal(4);
							else tn = new BigDecimal(5);
	
						}
					}


					logger.info("tn: " + tn);
					logger.info("weight: " + weight);
					result = result.add(tn.multiply(weight.abs()));
					logger.info("sum: " + result);
					
				}
			}

			
		} else if (dtp.equals("MON")) { // if MON
			logger.info("MON");
		
			List<VariationMeasureValue> mmsThisMonth = variationMeasureValueRepository.findMMByUserInRoleId(userId, vpdv.getId().getDetectionVariableId(), startOfMonth, endOfMonth);

			BigDecimal ds;
			BigDecimal ts = new BigDecimal(3);

			BigDecimal weight = new BigDecimal(signum).divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
			logger.info("weight: " + weight);

			if(mmsThisMonth != null && mmsThisMonth.size() > 0 ) {
				result = new BigDecimal(0);
				
				 VariationMeasureValue mmThisMonth = mmsThisMonth.get(0);
				logger.info("vmvId: " + mmThisMonth.getId());
				logger.info("dvId: " + mmThisMonth.getDetectionVariable().getId());
				
				VariationMeasureValue mmMonthZero = variationMeasureValueRepository
						.findOne(variationMeasureValueRepository.findMinId(mmThisMonth.getDetectionVariable(), userId));
				
				if (!mmThisMonth.getId().equals(mmMonthZero.getId())) {
					logger.info("mm this month: " + mmThisMonth.getMeasureValue());
					logger.info("mm zero month: " + mmMonthZero.getMeasureValue());
					logger.info("mm substract: " + mmThisMonth.getMeasureValue().subtract(mmMonthZero.getMeasureValue()));

					ds = (mmThisMonth.getMeasureValue().subtract(mmMonthZero.getMeasureValue()))
							.divide(mmMonthZero.getMeasureValue(), 2, RoundingMode.HALF_UP);
					logger.info("ds: " + ds);

					if (ds.compareTo(new BigDecimal(.25)) < 0)
						if (ds.compareTo(new BigDecimal(.1)) < 0)
							if (ds.compareTo(new BigDecimal(-.1)) < 0)
								if (ds.compareTo(new BigDecimal(-.25)) < 0)
									ts = new BigDecimal(1);
								else ts = new BigDecimal(2);
							else ts = new BigDecimal(3);
						else ts = new BigDecimal(4);
					else ts = new BigDecimal(5);
				}
				
				logger.info("ts: " + ts);
				logger.info("weight: " + weight);
				result = ts.multiply(weight.abs());
				logger.info("sum: " + result);

			}
		}
		
		return result;
	}

	private GeriatricFactorValue createGFV(Long dvId, BigDecimal gesValue, BigDecimal weight, Timestamp startOfMonth,
			Timestamp endOfMonth, Long userId, String pilotCode) {

		logger.info("dvId: " + dvId);
		logger.info("gesValue: " + gesValue);
		logger.info("weight: " + weight);
		logger.info("startOfMonth: " + startOfMonth);
		logger.info("userId: " + userId);
		logger.info("pilotCode: " + pilotCode);

		TimeInterval ti = getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH);
		UserInRole uir = userInRoleRepository.findOne(userId);
		
		GeriatricFactorValue ges = new GeriatricFactorValue();
		ges.setGefValue(gesValue);
		ges.setTimeInterval(ti);
		ges.setDetectionVariable(detectionVariableRepository.findOne(dvId));
		ges.setUserInRole(uir);
		
		if (weight == null) weight = pilotDetectionVariableRepository.findWeightByDetectionVariableAndPilotCodeGesGef(dvId, pilotCode);
		if (weight == null) weight = new BigDecimal(1);
		ges.setDerivationWeight(weight);

		geriatricFactorRepository.save(ges);
		
		return ges;
	}


	public List<VariationMeasureValue> filterListByDetectionVariable(final List<VariationMeasureValue> list,
			final DetectionVariable dv) {
		Stream<VariationMeasureValue> filter = list.stream()
				.filter(o -> dv.getId().equals(o.getDetectionVariable().getId()));
		List<VariationMeasureValue> findAll = filter.collect(Collectors.toList());
		return findAll;
	}

	private void computeNuisFor1Month(String pilotCode, Timestamp startOfMonth, Timestamp endOfMonth) {
		List<DetectionVariable> dvsWithMEA = detectionVariableRepository.findAllMEADvTypeByPilotCode(pilotCode);

		Set<DetectionVariable> dvsWithMEASet = new HashSet<DetectionVariable>(dvsWithMEA);

		if (dvsWithMEASet != null && dvsWithMEASet.size() > 0) {

			List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
					.findAllForMonthByPilotCodeNui(pilotCode, startOfMonth, endOfMonth);

			if (vmsMonthly != null && vmsMonthly.size() > 0) {
				Long prevDvId = 0L;
				for (DetectionVariable pdv : dvsWithMEASet) {
					if (!prevDvId.equals(pdv.getId())) {
						DetectionVariable dv = pdv;
						List<VariationMeasureValue> vmForSpecificMeasure = filterListByDetectionVariable(vmsMonthly,
								dv);
						Set<UserInRole> users = new HashSet<UserInRole>();
						for (VariationMeasureValue vmv : vmForSpecificMeasure) {
							users.add(vmv.getUserInRole());
						}
						if (vmForSpecificMeasure != null) {
							for (UserInRole uir : users) {
								createAllNuis(uir, dv, startOfMonth, endOfMonth, pilotCode);
							}
						}
						prevDvId = pdv.getId();
					}
				}
			}

		}

	}

	private List<NumericIndicatorValue> createAllNuis(UserInRole uir, DetectionVariable dv, Timestamp startOfMonth,
			Timestamp endOfMonth, String pilotCode) {
		List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();
		logger.info("uId: " + uir.getId());
		logger.info("dvId: " + dv.getId());
		logger.info("startOfMonth: " + startOfMonth);
		logger.info("pilotCode: " + pilotCode);
		// menja se where za upit jer smo izmenili upit i podelili na dva (upit
		// i podupit)
		String formula = "WITH subq AS (SELECT AVG (vm.measure_value) avg, COALESCE (STDDEV(vm.measure_value), 0) stDev, PERCENTILE_CONT (0.25) WITHIN GROUP (ORDER BY vm.measure_value DESC) best25 FROM variation_measure_value AS vm INNER JOIN time_interval AS ti ON ti. ID = vm.time_interval_id WHERE ti.interval_start >= '"
				+ startOfMonth + "' AND ti.interval_start <= '" + endOfMonth + "' AND vm.user_in_role_id = "
				+ uir.getId().toString() + " AND vm.measure_type_id = " + dv.getId().toString()
				+ ") SELECT avg, (CASE WHEN avg != 0 THEN stDev / avg ELSE 0 END) std, (CASE WHEN avg != 0 THEN best25 / avg ELSE 0 END) best, (CASE WHEN avg != 0 THEN best25 - avg / avg ELSE 0 END) delta FROM subq";
		Query sql = variationMeasureValueRepository.getEntityManager().createNativeQuery(formula);
		variationMeasureValueRepository.getEntityManager().getEntityManagerFactory()
				.addNamedQuery("VariationMeasureValue.dynamic", sql);
		Query namedQuery = variationMeasureValueRepository.getEntityManager()
				.createNamedQuery("VariationMeasureValue.dynamic");

		Object[] nuiForMeasure = (Object[]) namedQuery.getSingleResult(); // Long
																			// should
																			// be
																			// converted
																			// to
																			// BigDecimal
																			// or
																			// Double!!!

		
		if(nuiForMeasure != null && nuiForMeasure.length == 4) {

		
			logger.info("nui1: " + nuiForMeasure[0]);
			logger.info("nui2: " + nuiForMeasure[1]);
			logger.info("nui3: " + nuiForMeasure[2]);
			logger.info("nui4: " + nuiForMeasure[3]);
	
			if (nuiForMeasure[0] != null) {
				List<ViewMeaNuiDerivationPerPilot> dvNuisForMeasure = viewMeaNuiDerivationPerPilotRepository
						.findAllDvNuisForMeasure(dv.getDetectionVariableName(), pilotCode);
				BigDecimal nuiValue = new BigDecimal(0);
				for (int i = 0; i < 4; i++) {

					if (dvNuisForMeasure.get(i).getFormula().contains("avg")) {
						String nui1 = String.valueOf(nuiForMeasure[0]);
						nuiValue = new BigDecimal(nui1);
					} else if (dvNuisForMeasure.get(i).getFormula().contains("std")) {
						String nui2 = String.valueOf(nuiForMeasure[1]);
						nuiValue = new BigDecimal(nui2);
					} else if (dvNuisForMeasure.get(i).getFormula().contains("best")) {
						String nui3 = String.valueOf(nuiForMeasure[2]);
						nuiValue = new BigDecimal(nui3);
					} else if (dvNuisForMeasure.get(i).getFormula().contains("delta")) {
						String nui4 = String.valueOf(nuiForMeasure[3]);
						nuiValue = new BigDecimal(nui4);
					}

					NumericIndicatorValue nui = create1Nui(uir, nuiValue, dvNuisForMeasure.get(i).getDerivedNuiId(),
							startOfMonth);

					nuis.add(nui);
				} 
			} 
		}
		return nuis;
	}

	private NumericIndicatorValue create1Nui(UserInRole uir, BigDecimal nuiValue, Long nuiDvId,
			Timestamp startOfMonth) {
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(new BigDecimal(nuiValue.toString()));
		} else {
			nui.setNuiValue(new BigDecimal(0));
		}
		nui.setUserInRole(uir);
		nui.setDetectionVariable(detectionVariableRepository.getOne(nuiDvId));
		nui.setTimeInterval(getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH));
		nuiRepository.save(nui);
		return nui;
	}

	public TimeInterval getOrCreateTimeInterval(Timestamp intervalStart, TypicalPeriod typicalPeriod) {
		TimeInterval ti = timeIntervalRepository.findByIntervalStartAndTypicalPeriod(intervalStart,
				typicalPeriod.getDbName());
		if (ti == null) {
			ti = new TimeInterval();
			ti.setIntervalStart(intervalStart);
			ti.setTypicalPeriod(typicalPeriod.getDbName());
			ti.setCreated(new Timestamp((new Date().getTime())));
			timeIntervalRepository.save(ti);
			timeIntervalRepository.flush();
		}
		return ti;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getNuiValues/userInRoleId/{userInRoleId}/detectionVariableId/{detectionVariableId}")
	public Response getNuiValues(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("detectionVariableId") Long detectionVariableId) throws JsonProcessingException {
		List<NumericIndicatorValue> nuis = nuiRepository.getNuisForSelectedGes(userInRoleId, detectionVariableId);
		return Response.ok(objectMapper.writerWithView(View.NUIView.class).writeValueAsString(nuis)).build();
	}
	
	public String test() {
		return "hello";
	}

}