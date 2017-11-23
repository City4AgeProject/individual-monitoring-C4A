package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.dto.Gfvs;
import eu.city4age.dashboard.api.pojo.dto.Nuis;
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

		return JerseyResponse.build(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures));

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

		computeNuisForAllPilots();
		computeGESsForAllPilots();
		setVariablesComputedForAllPilots();

		return JerseyResponse.build();
	}

	private void computeGESsForAllPilots() {
		YearMonth lastComputedYearMonth = pilotRepository.getOne("ATH").getLastComputed();
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		Timestamp startOfMonth;
		Timestamp endOfMonth;

		if (currentYearMonth.compareTo(currentComputedYearMonth) > 1) {
			while (!currentComputedYearMonth.equals(currentYearMonth)) {
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				computeGESsFor1Month(startOfMonth, endOfMonth);
				computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth);
				computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth);
				computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
			}
		}
	}

	
	private void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth) {

		logger.info("computeFor1Month: " + factor);

			List<Gfvs> list = viewGefValuesPersistedSourceGesTypesRepository.doAllGfvs(startOfMonth, endOfMonth, factor);
		
			if (list != null && list.size() > 0) {
				
				int i = 0;
				int batchSize = 20;
				
				for(Gfvs ges: list) {
					
					createGFV(ges.getDdvId(), ges.getGesValue(), ges.getWeight(), startOfMonth, endOfMonth, ges.getUirId());
					
					i++;
					if (i % batchSize == 0) {
			            //flush a batch of inserts and release memory
			            geriatricFactorRepository.flush();
			            geriatricFactorRepository.clear();
			        }
				
				}
				
				geriatricFactorRepository.flush();
	            geriatricFactorRepository.clear();
	   
			}

	}

	private void computeNuisForAllPilots() {
		YearMonth lastComputedYearMonth = pilotRepository.getOne("ATH").getLastComputed();
		YearMonth currentYearMonth = YearMonth.now();
		YearMonth currentComputedYearMonth = lastComputedYearMonth.plusMonths(1L);
		Timestamp startOfMonth;
		Timestamp endOfMonth;
		if (currentYearMonth.compareTo(currentComputedYearMonth) > 1) {
			while (!currentComputedYearMonth.equals(currentYearMonth)) {
				startOfMonth = Timestamp.valueOf(currentComputedYearMonth.atDay(1).atStartOfDay());
				endOfMonth = Timestamp.valueOf(currentComputedYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
				computeNuisFor1Month(startOfMonth, endOfMonth);
				currentComputedYearMonth = currentComputedYearMonth.plusMonths(1L);
			}
		}
	}

	private void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth) {
		logger.info("computeGESsFor1Month");
		
		List<Gfvs> gess = nuiRepository.doAllGess(startOfMonth, endOfMonth);

		if(gess != null && gess.size() > 0) {
			
			int i = 0;
			int batchSize = 20;

			for (Gfvs ges : gess) {
				if(ges.getGesValue() != null) {
					logger.info("gesValue before createGFG: " + ges.getGesValue());
					createGFV(ges.getDdvId(), ges.getGesValue(), ges.getWeight(), startOfMonth, endOfMonth, ges.getUirId());
				
					i++;
					if (i % batchSize == 0) {
			            //flush a batch of inserts and release memory
			            geriatricFactorRepository.flush();
			            geriatricFactorRepository.clear();
			        }

				}
			}
			
			geriatricFactorRepository.flush();
            geriatricFactorRepository.clear();

		}

	}
	
	private void setVariablesComputedForAllPilots() {
		List<Pilot> pilots = pilotRepository.findAll();
		for(Pilot pilot : pilots)
			pilot.setLatestVariablesComputed(new Date());
		pilotRepository.save(pilots);
	}

	private GeriatricFactorValue createGFV(Long dvId, BigDecimal gesValue, BigDecimal weight, Timestamp startOfMonth,
			Timestamp endOfMonth, Long userId) {

		logger.info("dvId: " + dvId);
		logger.info("gesValue: " + gesValue);
		logger.info("weight: " + weight);
		logger.info("startOfMonth: " + startOfMonth);
		logger.info("userId: " + userId);

		TimeInterval ti = getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH);
		UserInRole uir = userInRoleRepository.findOne(userId);
		
		GeriatricFactorValue ges = new GeriatricFactorValue();
		ges.setGefValue(gesValue);
		ges.setTimeInterval(ti);
		ges.setDetectionVariable(detectionVariableRepository.findOne(dvId));
		ges.setUserInRole(uir);
		
		if (weight == null) weight = pilotDetectionVariableRepository.findWeightByDetectionVariableAndPilotCodeGesGef(dvId, uir.getPilotCode());
		if (weight == null) weight = new BigDecimal(1);
		ges.setDerivationWeight(weight);

		geriatricFactorRepository.saveWithoutFlush(ges);
		
		return ges;
	}

	private void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth) {

		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth);

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			createAllNuis(startOfMonth, endOfMonth);
		}
	}

	private List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth) {
		
		List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();
		logger.info("startOfMonth: " + startOfMonth);

		List<Nuis> nuisList = variationMeasureValueRepository.doAllNuis(startOfMonth, endOfMonth);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {
			
			int i = 0;
			int batchSize = 20;

			for (Nuis nui:nuisList) {

				BigDecimal nuiValue = nui.getNuiValue();
				NumericIndicatorValue create1Nui = create1Nui(nui.getUserId(), nui.getNuiDvId(), nuiValue, startOfMonth);
			
				nuis.add(create1Nui);
				
				i++;
				if (i % batchSize == 0) {
		            //flush a batch of inserts and release memory
		            nuiRepository.flush();
		            nuiRepository.clear();
		        }

			}
			
            nuiRepository.flush();
            nuiRepository.clear();
		
		}

		return nuis;

	}

	private NumericIndicatorValue create1Nui(Long userId, Long nuiDvId, BigDecimal nuiValue, Timestamp startOfMonth) {
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(nuiValue);
		} else {
			nui.setNuiValue(new BigDecimal(0));
		}
		nui.setUserInRoleId(userId);
		nui.setDetectionVariableId(nuiDvId);
		nui.setTimeInterval(getOrCreateTimeInterval(startOfMonth, TypicalPeriod.MONTH));

		nuiRepository.saveWithoutFlush(nui);
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