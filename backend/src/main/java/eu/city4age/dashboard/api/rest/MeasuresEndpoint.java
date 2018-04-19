package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
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
import org.springframework.http.ResponseEntity;
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
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
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
//@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
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
	Environment environment;

	@Autowired 
	private PredictionService predictionService = new PredictionService();
	
	@Autowired
    private RestTemplate restTemplate;

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
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public Response computeFromMeasures() throws JsonProcessingException, IOException {

		logger.info("computation started: " + new Date());

		List<Pilot> pilotsForComputation = setNewestSubmittedDataForAllPilots();
		
		List<Pilot> pilots = computeForAllPilots(pilotsForComputation);

		pilots = imputeAndPredict(pilots);
		
		logger.info("computation completed: " + new Date());
		return JerseyResponse.buildTextPlain("success", 200);
	}

	public void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode) {
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> list = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, factor, stringPilotCode);

		/*if(factor.equals(DetectionVariableType.GEF))
		list.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GEF));*/

		if (list != null && list.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(list, startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(gfvs);
			//nuiRepository.flush();
		}
	}
	
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public List<Pilot> imputeAndPredict(List<Pilot> pilots) {
		
		if (pilots != null && !pilots.isEmpty()) {
			Iterator<Pilot> pilotsIterator = pilots.iterator();
			while (pilotsIterator.hasNext()) {
				Pilot pilot = pilotsIterator.next();
				String request = "http://localhost:8080/C4A-dashboard/rest/measures/imputeAndPredictFor1Pilot/" + pilot.getPilotCode().name();
				ResponseEntity<String> response = restTemplate.getForEntity(request, String.class);
				
				if (response.getBody().contains("failed")) {
					logger.info("");
					logger.info("IMPUTE_AND_PREDICT FAILED FOR PILOT: " + pilot.getPilotCode().name() + ".");
					logger.info("");
					pilotsIterator.remove();
				}
			}
		} 
		else {
			logger.info("No new data submitted!");
		}
		return pilots;
	}
	
	@GET
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	@Produces(MediaType.APPLICATION_JSON)
	@Path ("imputeAndPredictFor1Pilot/{pilotCode}")
	public Response imputeAndPredictFor1Pilot (@PathParam("pilotCode") String pilotCodeString) {
		
		Pilot pilot = pilotRepository.findByPilotCode(Pilot.PilotCode.valueOf(pilotCodeString.toUpperCase()));
		try {
			predictionService.imputeAndPredict(pilot);
			return JerseyResponse.buildTextPlain("success");
		} catch (Exception e) {
			return JerseyResponse.buildTextPlain("failed");
		}
		
	}

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public List<Pilot> computeForAllPilots(List<Pilot> pilotsForComputation) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		

		if (pilotsForComputation != null && !pilotsForComputation.isEmpty()) {

			Iterator<Pilot> pilotsIterator = pilotsForComputation.iterator();
			while (pilotsIterator.hasNext()) {
				Pilot pilot = pilotsIterator.next();
				String request = "http://localhost:8080/C4A-dashboard/rest/measures/computeFor1Pilot/pilotCode/" + pilot.getPilotCode().name() + "/newestSubmittedDate/" + pilot.getNewestSubmittedData().toString();
				ResponseEntity<String> response = restTemplate.getForEntity(request, String.class);
				
				if (response.getBody().contains("failed")) {
					logger.info("");
					logger.info("COMPUTATION FAILED FOR PILOT: " + pilot.getPilotCode().name() + ".");
					logger.info("");
					pilotsIterator.remove();
				}
			}
		} else {
			logger.info("No new data submitted!");
		}
		
		return pilotsForComputation;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("computeFor1Pilot/pilotCode/{pilotCode}/newestSubmittedDate/{newestSubmittedDate}")
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public Response computeFor1Pilot(@PathParam("pilotCode") String pilotCodeString, @PathParam("newestSubmittedDate") String newestSubmittedDataString) throws Exception {
		Pilot pilot = pilotRepository.findByPilotCode(Pilot.PilotCode.valueOf(pilotCodeString.toUpperCase()));
		PilotCode pilotCode = pilot.getPilotCode();
		
		logger.info("pilotCode: " + pilotCode.name());

		Date latestDerivedMeasuresComputed = pilot.getLatestVariablesComputed();
		Timestamp newestSubmittedTimestamp = Timestamp.valueOf(newestSubmittedDataString);
		Date newestSubmittedData = Date.from(newestSubmittedTimestamp.toInstant());
		
		YearMonth startOfComputationYearMonth;

		if (latestDerivedMeasuresComputed == null) {
			startOfComputationYearMonth = 
					YearMonth.from(variationMeasureValueRepository.findFirstMonthForPilot(pilot.getPilotCode()).toInstant().atZone(ZoneOffset.UTC).toLocalDate());
		} else {
			startOfComputationYearMonth = 
					YearMonth.from(pilotRepository.findNextMonthForPilot(pilot.getPilotCode()).toInstant().atZone(ZoneOffset.UTC).toLocalDate());
		}

		YearMonth endOfComputationYearMonth = YearMonth.from(YearMonth.from(newestSubmittedData.toInstant().atZone(ZoneOffset.UTC).toLocalDate()));

		Timestamp startOfMonth;
		Timestamp endOfMonth;
		
		if (endOfComputationYearMonth.isBefore(YearMonth.now())) {
			endOfComputationYearMonth = endOfComputationYearMonth.plusMonths(1L);
			if (pilot.getLatestVariablesComputed() != null) startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}
		
		try {
			while(!startOfComputationYearMonth.equals(endOfComputationYearMonth)) {

				startOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				
				logger.info(startOfMonth);
				
				computeNuisFor1Month(startOfMonth, endOfMonth, pilotCode);
				computeGESsFor1Month(startOfMonth, endOfMonth, pilotCode);
				computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCode);
				computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCode);
				computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCode);
				startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
			}

			Timestamp endOfComputation = Timestamp.valueOf(endOfComputationYearMonth.minusMonths(1L).atDay(1).atStartOfDay());
			setVariablesComputedForAllPilots(pilot, endOfComputation, newestSubmittedData);
			return JerseyResponse.buildTextPlain("success");
		} catch (Exception e) {
			return JerseyResponse.buildTextPlain("failed");
		}
	}

	public void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) throws Exception {
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		//ROLLBACK SCENARIO!!!
		//if (startOfMonth.equals(Timestamp.valueOf("2017-06-01 00:00:00.0")) && pilotCode == Pilot.PilotCode.ATH) throw new Exception();
		
		List<Object[]> gess = nativeQueryRepository.computeAllGess(startOfMonth, endOfMonth, stringPilotCode);
		//gess.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GES));

		if(gess != null && gess.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(gess, startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(gfvs);
			//nuiRepository.flush();
		}

	}

	public void setVariablesComputedForAllPilots(Pilot pilot, Timestamp endOfComputation, Date newestSubmittedData) {
		
		pilot.setLatestVariablesComputed(endOfComputation);
		pilot.setTimeOfComputation(new Date());
		pilot.setNewestSubmittedData(newestSubmittedData);
		pilotRepository.save(pilot);
	}

	public List<Pilot> setNewestSubmittedDataForAllPilots() {
		
		List<Pilot> result = new ArrayList<>();
		
		List<Pilot> pilots = pilotRepository.findAll();

		for (Pilot pilot : pilots) {
			if (pilot.getLatestSubmissionCompleted() != null) {
				Date previous;
				if (pilot.getTimeOfComputation() == null || pilot.getTimeOfComputation().before(pilot.getLatestSubmissionCompleted())) {
					previous = pilot.getNewestSubmittedData();
					Timestamp newestSubmittedData = variationMeasureValueRepository.findMaxTimeIntervalStartByPilotCode(pilot.getPilotCode());
					pilot.setNewestSubmittedData(newestSubmittedData);
					if (previous == null || (previous != null && previous.before(newestSubmittedData))) 
						result.add(pilot);
				}
			}
		}
		return result;
	}

	public List<GeriatricFactorValue> createAllGFVs(List<Object[]> list, Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {
		
		final List<GeriatricFactorValue> gfvs = new ArrayList<GeriatricFactorValue>();
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode);
		for(Object[] arr : list) {
			GeriatricFactorValue ges = new GeriatricFactorValue();
			ges.setGefValue((BigDecimal) arr[2]);
			ges.setTimeInterval(ti);
			//try {
			ges.setDetectionVariableId(((Integer) arr[1]).longValue());
			//} catch (Exception e) {
			//	ges.setDetectionVariableId(((Long) arr[1]));
			//}
			//try {
			ges.setUserInRoleId(((Integer) arr[0]).longValue());
			//} catch (Exception e) {
			//	ges.setUserInRoleId(((Long) arr[0]));
			//}
			ges.setDerivationWeight(arr[3] == null ? new BigDecimal(1) : (BigDecimal) arr[3]);
			gfvs.add(ges);
		}
		return gfvs;
	}

	public void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {
		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth, pilotCode);

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			List<NumericIndicatorValue> nuis = createAllNuis(startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(nuis);
			//nuiRepository.flush();
		}
	}

	public List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {
		
		final List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();

		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, stringPilotCode);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {

			for (Object[] nui:nuisList) {

				NumericIndicatorValue create1Nui = create1Nui((Integer) nui[0],  (Integer) nui[1], (Double) nui[2], startOfMonth, pilotCode);

				nuis.add(create1Nui);

			}

		}
		return nuis;

	}

	public NumericIndicatorValue create1Nui(Integer userId, Integer nuiDvId, Double nuiValue, Timestamp startOfMonth, PilotCode pilotCode) {
		
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(nuiValue);
		} else {
			nui.setNuiValue(new Double(0));
		}
		nui.setUserInRoleId(userId);
		nui.setDetectionVariableId(nuiDvId);
		nui.setTimeInterval(getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode));

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
			//timeIntervalRepository.flush();
		}
		return ti;
	}

	public TimeInterval getOrCreateTimeIntervalPilotTimeZone(Date intervalStart, TypicalPeriod typicalPeriod, PilotCode pilotCode) {
		
		//String computationTimeZone = pilotRepository.findByPilotCode(pilotCode).getCompZone();
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		TimeInterval ti = timeIntervalRepository.findByIntervalStartAndTypicalPeriod(intervalStart,
				typicalPeriod.getDbName());
		if (ti == null) {
			ti = new TimeInterval();
			ti.setIntervalStart(intervalStart);
			ti.setTypicalPeriod(typicalPeriod.getDbName());
			ti.setCreated(new Timestamp((new Date().getTime())));
			timeIntervalRepository.save(ti);
			//timeIntervalRepository.flush();
		}
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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

	public String mockitoTest() {
		return "hello";
	}
	
	

}