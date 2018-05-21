package eu.city4age.dashboard.api.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.MeasuresService;
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
	private MeasuresService measuresService;

	@Autowired
	Environment environment;

	@Autowired 
	private PredictionService predictionService = new PredictionService();

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

		List<VariationMeasureValue> measures = null;
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
	public Response computeFromMeasures() throws Exception {
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		logger.info("computation started: " + new Date());

		List<Pilot> pilotsForComputation = setNewestSubmittedDataForAllPilots();
		
		List<Pilot> pilots = computeForAllPilots(pilotsForComputation);

		pilots = imputeAndPredict(pilots);
		
		logger.info("computation completed: " + new Date());
		return JerseyResponse.buildTextPlain("success", 200);
	}

	public List<Pilot> imputeAndPredict(List<Pilot> pilots) {
		
		if (pilots != null && !pilots.isEmpty()) {
			Iterator<Pilot> pilotsIterator = pilots.iterator();
			while (pilotsIterator.hasNext()) {
				Pilot pilot = pilotsIterator.next();
				predictionService.imputeAndPredict(pilot);
			}
		} 
		else {
			logger.info("No new data submitted!");
		}
		return pilots;
	}

	public List<Pilot> computeForAllPilots(List<Pilot> pilotsForComputation) {
		

		if (pilotsForComputation != null && !pilotsForComputation.isEmpty()) {

			Iterator<Pilot> pilotsIterator = pilotsForComputation.iterator();
			while (pilotsIterator.hasNext()) {
				Pilot pilot = pilotsIterator.next();
				try {
					measuresService.computeFor1Pilot(pilot.getPilotCode().name(), pilot.getNewestSubmittedData().toString());
				} catch (Exception e) {
					logger.info("pukao pilot: " + pilot.getPilotCode().getName());
					e.printStackTrace();
				}
			}
			pilotsIterator.remove();
		} else {
			logger.info("No new data submitted!");
		}
		
		return pilotsForComputation;
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
					if (previous == null || (previous != null && newestSubmittedData != null && previous.before(newestSubmittedData))) 
						result.add(pilot);
				}
			}
		}
		return result;
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
			TimeInterval newTimeInterval = measuresService.getOrCreateTimeInterval(intervalStart, TypicalPeriod.DAY);
			
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
				newTimeInterval = measuresService.getOrCreateTimeInterval(timeIntervalRepository.getTruncatedTimeIntervalStart(vm.getTimeInterval().getId(), "day"), TypicalPeriod.DAY);
				
			else
				newTimeInterval = measuresService.getOrCreateTimeInterval(intervalDiff, TypicalPeriod.DAY);
			
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
		variationMeasureValueRepository.bulkSave(retList);
		
		return JerseyResponse.buildTextPlain("success", 200);
		
	}
	
	public int determineTimeInterval (long start, long end, long differentiator) {
		
		if ((differentiator - start) >= (end - differentiator)) return 0;
		else return 1;
		
	}	

}