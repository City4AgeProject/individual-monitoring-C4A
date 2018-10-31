package eu.city4age.dashboard.api.rest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.analytics.CorrelationData;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.GroupAnalyticsService;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(GroupAnalyticsEndpoint.PATH)
public class GroupAnalyticsEndpoint {

	public static final String PATH = "group_analytics";

	static protected Logger logger = LogManager.getLogger(GroupAnalyticsEndpoint.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	public SessionFactory sessionFactory;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private GroupAnalyticsService groupAnalyticsService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("correlationCoefficient/detectionVariable/{detectionVariable : .+}")
	public Response getCorrelationCoefficient(@PathParam("detectionVariable") List<PathSegment> detectionVariableId, @QueryParam("pilot") String pilotString,
			@QueryParam("intervalStart") String intervalStartString, @QueryParam("intervalEnd") String intervalEndString) throws JsonProcessingException {

		List<Pilot> pilots = groupAnalyticsService.getPilots(pilotString);
		
		LocalDateTime intervalStartLDT = null;
		if (intervalStartString == null)
			intervalStartLDT = LocalDateTime.parse("2016-07-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		else
			intervalStartLDT = LocalDateTime.parse(intervalStartString.concat(" 00:00:00"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		LocalDateTime intervalEndLDT = null;
		if (intervalEndString == null)
			intervalEndLDT = LocalDateTime.now();
		else
			intervalEndLDT = LocalDateTime.parse(intervalEndString.concat(" 23:59:59"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		Date intervalStartDate = Date.from(intervalStartLDT.toInstant(ZoneOffset.UTC));
		Date intervalEndDate = Date.from(intervalEndLDT.toInstant(ZoneOffset.UTC));

		List<DetectionVariable> detectionVariables = groupAnalyticsService.getDetectionVariables(detectionVariableId);

		DetectionVariable overall = detectionVariableRepository.findOne(501L);
		
		LinkedHashMap<String, Double> valuesList = new LinkedHashMap<String, Double>();

		for (DetectionVariable dv : detectionVariables) {			

			List<Double> correlations = new ArrayList<Double>();

			for (Pilot pilot : pilots) {

				List<UserInRole> uirList = userInRoleRepository.findForPilotCode(pilot.getPilotCode());

				for (UserInRole uir : uirList) {

					correlations = groupAnalyticsService.calculateCorrelationCoefficientsForOneUser(overall, dv, correlations, intervalStartDate,
							intervalEndDate, uir);
				}
			}

			// average of the correlation coefficients for the given time period
			valuesList = groupAnalyticsService.averageCorrelationValues(valuesList, dv.getDetectionVariableName(), correlations);
		}

		return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(valuesList));
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("correlationCoefficientHeatMap/detectionVariable/{detectionVariable : .+}")
	public Response getCorrelationCoefficientsHeatMap(@PathParam("detectionVariable") List<PathSegment> detectionVariableId, @QueryParam("pilot") String pilotString) throws JsonProcessingException {

		List<Pilot> pilots = groupAnalyticsService.getPilots(pilotString);

		List<DetectionVariable> detectionVariables = groupAnalyticsService.getDetectionVariables(detectionVariableId);

		DetectionVariable overall = detectionVariableRepository.findOne(501L);

		LocalDateTime today = LocalDateTime.now();

		List<CorrelationData> correlationDataList = new ArrayList<CorrelationData>();

		for (DetectionVariable dv : detectionVariables) {

			CorrelationData correlationData = new CorrelationData();

			correlationData.setDetectionVariableName(dv.getDetectionVariableName());

			LocalDateTime startDate = LocalDateTime.parse("2016-07-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime endDate = LocalDateTime.parse("2016-12-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			LinkedHashMap<String, Double> valuesList = new LinkedHashMap<String, Double>();

			while (endDate.isBefore(today)) {

				String period = "";
				if (startDate.getMonthValue() == 1) {
					period = period.concat("1H ").concat(String.valueOf(startDate.getYear()));
				} else {
					period = period.concat("2H ").concat(String.valueOf(startDate.getYear()));
				}

				List<Double> correlations = new ArrayList<Double>();

				for (Pilot pilot : pilots) {

					Date intervalStartDate = Date.from(startDate.toInstant(ZoneOffset.UTC));
					Date intervalEndDate = Date.from(endDate.toInstant(ZoneOffset.UTC));

					List<UserInRole> uirList = userInRoleRepository.findForPilotCode(pilot.getPilotCode());

					for (UserInRole uir : uirList) {

						correlations = groupAnalyticsService.calculateCorrelationCoefficientsForOneUser(overall, dv, correlations, intervalStartDate,
								intervalEndDate, uir);
					}
				}

				// average of the correlation coefficients for the given time period
				valuesList = groupAnalyticsService.averageCorrelationValues(valuesList, period, correlations);

				startDate = startDate.plusMonths(6L);
				endDate = endDate.plusMonths(6L);
			}

			correlationData.setValues(valuesList);
			correlationDataList.add(correlationData);
		}

		return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(correlationDataList));
	}

}
