package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
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
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(MeasuresService.PATH)
public class MeasuresService {

	public static final String PATH = "measures";
	
	static protected Logger logger = LogManager.getLogger(MeasuresService.class);
	
	static protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private NUIRepository nuiRepository;

	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;
	
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

		return JerseyResponse.build(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures));

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("computeFromMeasures")
	@Scheduled(cron = "0 0 0 * * *", zone = "UTC")
	public Response computeFromMeasures() throws JsonProcessingException, IOException {
		
		logger.info("pokrenuo compute: " + new Date());

		computeForAllPilots();

		logger.info("izvrsio compute: " + new Date());
		return JerseyResponse.build("success", 200);
	}

	private void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, List<String> pilotCodes) {
		List<Object[]> list = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, factor, pilotCodes);

		if (list != null && list.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(list, startOfMonth, endOfMonth);
			nuiRepository.bulkSave(gfvs);
		}
	}

	private void computeForAllPilots() {
		
		List<Pilot> nevers = pilotRepository.findAllNeverComputed();
		List<Pilot> comps = pilotRepository.findAllComputed();
	
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth lastComputedYearMonth = null;
		List<String> pilotCodes = new ArrayList<String>();
		
		if(nevers != null && nevers.size() > 0) {
			lastComputedYearMonth = nevers.get(0).getComputedStartDate();
			for(Pilot pilot : nevers) {
				pilotCodes.add(pilot.getPilotCode());
			}
		} else {
			for(Pilot pilot : comps) {
				YearMonth lastComputedPilot = YearMonth.from(pilot.getLatestVariablesComputed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				if(lastComputedYearMonth == null || lastComputedYearMonth.isBefore(lastComputedPilot)) {
					lastComputedYearMonth = lastComputedPilot;
				}
			}
		}
		if (lastComputedYearMonth == null) {
			
			logger.info("No new data submitted!");
			
			return;
		}
		
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		
		Timestamp startOfMonth;
		Timestamp endOfMonth;
		if (currentYearMonth.compareTo(currentComputedYearMonth) > 0) {
			while (!currentComputedYearMonth.equals(currentYearMonth)) {
				
				for(Pilot pilot : nevers) {
					if(currentComputedYearMonth.isAfter(YearMonth.from(pilot.getLatestSubmissionCompleted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))) {
						pilotCodes.remove(pilot.getPilotCode());
					}
				}
				
				for(Pilot pilot : comps) {
					YearMonth lastComputedPilot = YearMonth.from(pilot.getLatestVariablesComputed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					if(currentComputedYearMonth.equals(lastComputedPilot.plusMonths(1L))) {
						pilotCodes.add(pilot.getPilotCode());
					}
					if(currentComputedYearMonth.isAfter(YearMonth.from(pilot.getLatestSubmissionCompleted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))) {
						pilotCodes.remove(pilot.getPilotCode());
					}
				} 
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				computeNuisFor1Month(startOfMonth, endOfMonth, pilotCodes);
				computeGESsFor1Month(startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCodes);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
			}
		}
		comps.addAll(nevers);
		setVariablesComputedForAllPilots(comps);
	}

	private void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, List<String> pilotCodes) {
		List<Object[]> gess = nativeQueryRepository.computeAllGess(startOfMonth, endOfMonth, pilotCodes);
		
		if(gess != null && gess.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(gess, startOfMonth, endOfMonth);
			nuiRepository.bulkSave(gfvs);
		}
	
	}

	private void setVariablesComputedForAllPilots(List<Pilot> pilots) {
		for(Pilot pilot : pilots)
			pilot.setLatestVariablesComputed(new Date());
		pilotRepository.save(pilots);
	}
	
	private List<GeriatricFactorValue> createAllGFVs(List<Object[]> list, Timestamp startOfMonth, Timestamp endOfMonth) {
		final List<GeriatricFactorValue> gfvs = new ArrayList<GeriatricFactorValue>();
		final TimeInterval ti = getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH);
		for(Object[] arr : list) {
			GeriatricFactorValue ges = new GeriatricFactorValue();
			ges.setGefValue((BigDecimal) arr[2]);
			ges.setTimeInterval(ti);
			ges.setDetectionVariableId(((Integer) arr[1]).longValue());
			ges.setUserInRoleId(((Integer) arr[0]).longValue());
			ges.setDerivationWeight(arr[3] == null? new BigDecimal(1) : (BigDecimal) arr[3]);
			gfvs.add(ges);
		}
		return gfvs;
	}

	private void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, List<String> pilotCodes) {
		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth, pilotCodes);

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			List<NumericIndicatorValue> nuis = createAllNuis(startOfMonth, endOfMonth, pilotCodes);
			nuiRepository.bulkSave(nuis);
		}
	}

	private List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth, List<String> pilotCodes) {
		final List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();
		

		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, pilotCodes);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {

			for (Object[] nui:nuisList) {

				NumericIndicatorValue create1Nui = create1Nui((Integer) nui[0],  (Integer) nui[1], (Double) nui[2], startOfMonth);

				nuis.add(create1Nui);

			}

		}
		return nuis;

	}

	private NumericIndicatorValue create1Nui(Integer userId, Integer nuiDvId, Double nuiValue, Timestamp startOfMonth) {
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(nuiValue);
		} else {
			nui.setNuiValue(new Double(0));
		}
		nui.setUserInRoleId(userId);
		nui.setDetectionVariableId(nuiDvId);
		nui.setTimeInterval(getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH));

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
		}
		return ti;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getNuiValues/userInRoleId/{userInRoleId}/detectionVariableId/{detectionVariableId}")
	public Response getNuiValues(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("detectionVariableId") Long detectionVariableId) throws JsonProcessingException {
		List<NumericIndicatorValue> nuis = nuiRepository.getNuisForSelectedGes(userInRoleId, detectionVariableId);
		return JerseyResponse.build(objectMapper.writerWithView(View.NUIView.class).writeValueAsString(nuis));
	}

	public String mockitoTest() {
		return "hello";
	}

}