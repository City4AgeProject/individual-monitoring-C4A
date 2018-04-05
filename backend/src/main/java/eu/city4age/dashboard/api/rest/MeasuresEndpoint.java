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
import java.util.stream.Collectors;

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
import eu.city4age.dashboard.api.service.PredictionService;
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
@Path(MeasuresEndpoint.PATH)
public class MeasuresEndpoint {

	public static final String PATH = "measures";
	
	static protected Logger logger = LogManager.getLogger(MeasuresEndpoint.class);
	
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
	private PredictionService predictionService = new PredictionService();
	
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
		
		logger.info("computation started: " + new Date());

		List<Pilot> pilots = computeForAllPilots();
		predictionService.interpolateAndPredict(pilots);

		logger.info("computation completed: " + new Date());
		return JerseyResponse.buildTextPlain("success", 200);
	}
		
	private void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, List<Pilot.PilotCode> pilotCodes) {
		
		logger.info("");
		logger.info("computeFor1Month");
		logger.info("");
		
		List<String> stringPilotCodes = new ArrayList<>();
		for (Pilot.PilotCode pilotCode : pilotCodes) stringPilotCodes.add(pilotCode.getName().toLowerCase());

		List<Object[]> list = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, factor, stringPilotCodes);
		logger.info("list: " + list);

		/*if(factor.equals(DetectionVariableType.GEF))
			list.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GEF));*/

		if (list != null && list.size() > 0) {
			logger.info("list.size: " + list.size());
			List<GeriatricFactorValue> gfvs = createAllGFVs(list, startOfMonth, endOfMonth);
			nuiRepository.bulkSave(gfvs);
		}
	}

	private List<Pilot> computeForAllPilots() {
		
		logger.info("computeForAllPilots");
		
		List<Pilot> nevers = pilotRepository.findAllNeverComputed();
		List<Pilot> comps = pilotRepository.findAllComputed();
	
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth lastComputedYearMonth = null;
		List<Pilot.PilotCode> pilotCodes = new ArrayList<>();
		
		if(nevers != null && nevers.size() > 0) {
			lastComputedYearMonth = getComputedStartDate(nevers.get(0));
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
		if (lastComputedYearMonth == null ) {
			
			logger.info("No new data submitted!");
			
			return comps;
		}
		
		if (comps != null) logger.info("comps.size(): " + comps.size());
		else logger.info("comps is null");
		
		if (nevers != null) logger.info("nevers.size(): " + nevers.size());
		else logger.info("nevers is null");
		
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
				
				logger.info("pilotCodes: " + pilotCodes);
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				
				logger.info("");
				logger.info("currentComputedYearMonth: " + currentComputedYearMonth);
				logger.info("startOfMonth: " + startOfMonth);
				logger.info("endOfMonth: " + endOfMonth);
				logger.info("");
				
				computeNuisFor1Month(startOfMonth, endOfMonth, pilotCodes);
				computeGESsFor1Month(startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCodes);
				computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCodes);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
			}
		}
		
		logger.info("nevers.size(): " + nevers.size());
		comps.addAll(nevers);
		
		logger.info("comps.size(): " + comps.size());
		setVariablesComputedForAllPilots(comps);
		
		return comps;
	}

	private void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, List<Pilot.PilotCode> pilotCodes) {
		
		logger.info("");
		logger.info("computeGESsFor1Month");
		logger.info("");
		
		List<String> stringPilotCodes = new ArrayList<>();
		for (Pilot.PilotCode pilotCode : pilotCodes) stringPilotCodes.add(pilotCode.getName().toLowerCase());
		
		List<Object[]> gess = nativeQueryRepository.computeAllGess(startOfMonth, endOfMonth, stringPilotCodes);
		//gess.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GES));
		
		if(gess != null && gess.size() > 0) {
			logger.info("gess.size: " + gess.size());
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
		logger.info("");
		logger.info("COMPUTE GES");
		logger.info("");
		for(Object[] arr : list) {
			GeriatricFactorValue ges = new GeriatricFactorValue();
			ges.setGefValue((BigDecimal) arr[2]);
			ges.setTimeInterval(ti);
			try {
				ges.setDetectionVariableId(((Integer) arr[1]).longValue());
			} catch (Exception e) {
				ges.setDetectionVariableId(((Long) arr[1]));
			}
			try {
				ges.setUserInRoleId(((Integer) arr[0]).longValue());
			} catch (Exception e) {
				ges.setUserInRoleId(((Long) arr[0]));
			}
			ges.setDerivationWeight(arr[3] == null? new BigDecimal(1) : (BigDecimal) arr[3]);
			
			logger.info("");
			logger.info("uirId: " + ges.getUserInRoleId());
			logger.info("dvId: " + ges.getDetectionVariableId());
			logger.info("intervalStart: " + ges.getTimeInterval().getIntervalStart());
			logger.info("gesValue: " + ges.getGefValue());
			logger.info("");
			
			gfvs.add(ges);
		}
		return gfvs;
	}

	private void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, List<Pilot.PilotCode> pilotCodes) {
		
		logger.info("");
		logger.info("computeNuisFor1Month");
		logger.info("");
		
		List<String> stringPilotCodes = new ArrayList<>();
		for (Pilot.PilotCode pilotCode : pilotCodes) stringPilotCodes.add(pilotCode.getName().toLowerCase());
		
		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth, pilotCodes);

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			logger.info("vmsMonthly.size(): " + vmsMonthly.size());
			List<NumericIndicatorValue> nuis = createAllNuis(startOfMonth, endOfMonth, stringPilotCodes);
			nuiRepository.bulkSave(nuis);
		}
	}

	private List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth, List<String> pilotCodes) {
		final List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();
		
		logger.info("");
		logger.info("createAllNuis");
		logger.info("");

		logger.info("pilotCodes: " + pilotCodes);
		
		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, pilotCodes);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {
			
			logger.info("");
			logger.info(nuisList.size());
			logger.info("COMPUTE NUI");
			logger.info("");

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
		
		logger.info("");
		logger.info("uirId: " + nui.getUserInRoleId());
		logger.info("dvId: " + nui.getDetectionVariableId());
		logger.info("tiId: " + nui.getTimeInterval().getId());
		logger.info("nuiValue: " + nui.getNuiValue());
		logger.info("");

		return nui;
	}

	public TimeInterval getOrCreateTimeInterval(Date intervalStart, TypicalPeriod typicalPeriod) {
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
	
	@GET
	@ApiOperation("Method for fixing time intervals to have interval start and typical period, not interval start and interval end")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("fixTimeIntervals")
	public Response fixTimeIntervals () {				
		
		List<VariationMeasureValue> retList = new ArrayList <VariationMeasureValue> ();
		List<VariationMeasureValue> delList = new ArrayList <VariationMeasureValue> ();
		
		List<VariationMeasureValue> sameDaySleepMeasures = variationMeasureValueRepository.findAllSleepMeasuresInSameDay();
		
		for (VariationMeasureValue vm : sameDaySleepMeasures) {
			
			Timestamp intervalStart = timeIntervalRepository.getTruncatedTimeIntervalStart(vm.getTimeInterval().getId(), "day");			
			TimeInterval newTimeInterval = getOrCreateTimeInterval(intervalStart, TypicalPeriod.DAY);
			
			vm.setTimeInterval(newTimeInterval);
						
			List<VariationMeasureValue> filteredSameDaySleepMeasures = retList.stream().filter(p -> p.getUserInRole().getId().equals(vm.getUserInRole().getId()) && p.getDetectionVariable().getId().equals(vm.getDetectionVariable().getId()) && p.getTimeInterval().getId().equals(vm.getTimeInterval().getId())).collect(Collectors.toList());
			
			if (filteredSameDaySleepMeasures.isEmpty())
				retList.add(vm);
			
			else if (filteredSameDaySleepMeasures.size() == 1) {
				
				VariationMeasureValue vmv = filteredSameDaySleepMeasures.get(0);				
				int pos = retList.indexOf(vmv);
				vmv.setMeasureValue(vmv.getMeasureValue().add(vm.getMeasureValue()));
				
				retList.set(pos, vmv);	
				delList.add(vm);
			}
		}
		
		List<VariationMeasureValue> differentDaysSleepMeasures = variationMeasureValueRepository.findAllSleepMeasuresInDifferentDays();
		
		for (VariationMeasureValue vm : differentDaysSleepMeasures) {
			
			// ovde dohvata end, jer je to razlika izmedju start i end
			Timestamp intervalDiff = timeIntervalRepository.getTruncatedTimeIntervalEnd(vm.getTimeInterval().getId(), "day");
			TimeInterval newTimeInterval = null;
			
			if (determineTimeInterval(vm.getTimeInterval().getIntervalStart().getTime(), vm.getTimeInterval().getIntervalEnd().getTime(), intervalDiff.getTime()) == 0)
				newTimeInterval = getOrCreateTimeInterval(timeIntervalRepository.getTruncatedTimeIntervalStart(vm.getTimeInterval().getId(), "day"), TypicalPeriod.DAY);
				
			else 
				newTimeInterval = getOrCreateTimeInterval(intervalDiff, TypicalPeriod.DAY);
			
			vm.setTimeInterval(newTimeInterval);
			
			List<VariationMeasureValue> filtereddifferentDaysSleepMeasures = retList.stream().filter(p -> p.getUserInRole().getId().equals(vm.getUserInRole().getId()) && p.getDetectionVariable().getId().equals(vm.getDetectionVariable().getId()) && p.getTimeInterval().getId().equals(vm.getTimeInterval().getId())).collect(Collectors.toList());
			
			if (filtereddifferentDaysSleepMeasures.isEmpty()) 
				retList.add(vm);			
			
			else if (filtereddifferentDaysSleepMeasures.size() == 1) {
				
				VariationMeasureValue vmv = filtereddifferentDaysSleepMeasures.get(0);
				int pos = retList.indexOf(vmv);
				vmv.setMeasureValue(vmv.getMeasureValue().add(vm.getMeasureValue()));
				
				retList.set(pos, vmv);		
				delList.add(vm);
			}
		}
		
		variationMeasureValueRepository.delete(delList);
		variationMeasureValueRepository.flush();
		variationMeasureValueRepository.bulkSave (retList);		
		
		return JerseyResponse.buildTextPlain("success", 200);
		
	}
	
	public int determineTimeInterval (long start, long end, long differentiator) {
		
		if ((differentiator - start) >= (end - differentiator)) return 0;
		else return 1;
		
	}
	
	private YearMonth getComputedStartDate(Pilot pilot) {
		if (pilot.getLatestVariablesComputed() != null) {
			return YearMonth
					.from(pilot.getLatestVariablesComputed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
			return YearMonth.of(2015, 12); // Everything is computed from starting date of project, jan 2016
	}

	public String mockitoTest() {
		return "hello";
	}

}