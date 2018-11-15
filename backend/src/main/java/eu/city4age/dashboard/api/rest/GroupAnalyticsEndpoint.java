package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.exceptions.JsonEmptyException;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewGroupAnalyticsDataRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.AnalyticsMetadataResponse;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.CorrelationData;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsGroups;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsResponse;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsSeries;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.OJDataTreeDetectionVariableAttribute;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.OJDataTreeDetectionVariableSingleElem;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.GroupAnalyticsService;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(GroupAnalyticsEndpoint.PATH)
public class GroupAnalyticsEndpoint {

	public static final String PATH = "groupAnalytics";

	static protected Logger logger = LogManager.getLogger(GroupAnalyticsEndpoint.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	public SessionFactory sessionFactory;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private ViewGroupAnalyticsDataRepository viewGroupAnalyticsDataRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private GroupAnalyticsService groupAnalyticsService;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

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
		
		Timestamp intervalStartDate = Timestamp.from(intervalStartLDT.toInstant(ZoneOffset.UTC));
		Timestamp intervalEndDate = Timestamp.from(intervalEndLDT.toInstant(ZoneOffset.UTC));

		List<DetectionVariable> detectionVariables = groupAnalyticsService.getDetectionVariables(detectionVariableId);

		DetectionVariable overall = detectionVariableRepository.findOne(501L);
		
		LinkedHashMap<String, Double> valuesList = new LinkedHashMap<String, Double>();

		for (DetectionVariable dv : detectionVariables) {			

			List<Double> correlations = new ArrayList<Double>();
			
			Double corr;

			for (Pilot pilot : pilots) {
				
				logger.info("poceo pilot: " + pilot.getPilotCode().name());

				List<UserInRole> uirList = userInRoleRepository.findForPilotCode(pilot.getPilotCode());

				for (UserInRole uir : uirList) {

					corr = groupAnalyticsService.calculateCorrelationCoefficientsForOneUser(overall, dv, intervalStartDate,	intervalEndDate, uir);
					
					if (corr != null) correlations.add(corr);
					
				}
				logger.info("zavrsio pilot: " + pilot.getPilotCode().name());
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
				
				Double corr;

				for (Pilot pilot : pilots) {

					Timestamp intervalStartDate = Timestamp.from(startDate.toInstant(ZoneOffset.UTC));
					Timestamp intervalEndDate = Timestamp.from(endDate.toInstant(ZoneOffset.UTC));

					List<UserInRole> uirList = userInRoleRepository.findForPilotCode(pilot.getPilotCode());

					for (UserInRole uir : uirList) {

						corr = groupAnalyticsService.calculateCorrelationCoefficientsForOneUser(overall, dv, intervalStartDate,
								intervalEndDate, uir);
						
						if (corr != null) correlations.add(corr);
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("analyticsMetadata")
	public Response getAnalyticsMetadata (@Context ServletConfig sc) throws JsonEmptyException, IOException {
		
		AnalyticsMetadataResponse response = new AnalyticsMetadataResponse ();
		
		List<OJDataTreeDetectionVariableSingleElem> detectionVariables = new ArrayList <OJDataTreeDetectionVariableSingleElem> ();
		
		List<DetectionVariable> varsList = detectionVariableRepository.findByDetectionVariableType(DetectionVariableType.GFG);
		
		Long counter = 0l;
		
		for (DetectionVariable gfg : varsList) {
			
			List<Pilot.PilotCode> pilotCodes = pilotDetectionVariableRepository.findPilotCodeByDetectionVariableId(gfg.getId());
			
			if (!pilotCodes.isEmpty()) {
				
				OJDataTreeDetectionVariableSingleElem gfgElement = new OJDataTreeDetectionVariableSingleElem ();
				OJDataTreeDetectionVariableAttribute gfgAttr = new OJDataTreeDetectionVariableAttribute ();
				List<OJDataTreeDetectionVariableSingleElem> gfgChildren = new ArrayList <OJDataTreeDetectionVariableSingleElem> ();
				
				gfgAttr.setId(++counter);
				gfgAttr.setDetectionVariableId(gfg.getId());
				gfgAttr.setTitle(gfg.getDetectionVariableName());
				gfgAttr.setType(DetectionVariableType.GFG.getDetectionVariableType());
				
				if (pilotCodes.size() == 6) 
					gfgAttr.setAllPilots(true);
				else
					gfgAttr.setAllPilots(false);
				
				gfgAttr.setPilots(pilotCodes);
				gfgElement.setAttr(gfgAttr);
				
				List<DetectionVariable> derivedGEFs = pilotDetectionVariableRepository.findDetectionVariableByDerivedDetectionVariableIdAndPilotCodes(gfg.getId(), pilotCodes);
				
				for (DetectionVariable gef : derivedGEFs) {
					
					List<Pilot.PilotCode> gefToGfgPilots = pilotDetectionVariableRepository.findPilotCodesByDetectionVariableIdAndDerivedDetectionVariableIdAndDetectionVariableType (gef.getDetectionVariableType(), gef.getId(), gfg.getId());
					
					if (!gefToGfgPilots.isEmpty()) {
						
						OJDataTreeDetectionVariableSingleElem gefElement = new OJDataTreeDetectionVariableSingleElem ();
						OJDataTreeDetectionVariableAttribute gefAttr = new OJDataTreeDetectionVariableAttribute ();
						List<OJDataTreeDetectionVariableSingleElem> gefChildren = new ArrayList <OJDataTreeDetectionVariableSingleElem> ();
						
						gefAttr.setId(++counter);
						gefAttr.setDetectionVariableId(gef.getId());
						gefAttr.setTitle(gef.getDetectionVariableName());
						gefAttr.setType(DetectionVariableType.GEF.getDetectionVariableType());
						
						if (gefToGfgPilots.size() == 6) 
							gefAttr.setAllPilots(true);
						else
							gefAttr.setAllPilots(false);
						
						gefAttr.setPilots(gefToGfgPilots);
						gefElement.setAttr(gefAttr);
						
						List<DetectionVariable> derivedGESs = pilotDetectionVariableRepository.findDetectionVariableByDerivedDetectionVariableIdAndPilotCodes(gef.getId(), gefToGfgPilots);
						
						for (DetectionVariable ges : derivedGESs) {
							
							List<Pilot.PilotCode> gesToGefPilots = pilotDetectionVariableRepository.findPilotCodesByDetectionVariableIdAndDerivedDetectionVariableIdAndDetectionVariableType (ges.getDetectionVariableType(), ges.getId(), gef.getId());
							
							if (!gesToGefPilots.isEmpty()) {
								
								OJDataTreeDetectionVariableSingleElem gesElement = new OJDataTreeDetectionVariableSingleElem ();
								OJDataTreeDetectionVariableAttribute gesAttr = new OJDataTreeDetectionVariableAttribute ();
								List<OJDataTreeDetectionVariableSingleElem> gesChildren = new ArrayList <OJDataTreeDetectionVariableSingleElem> ();
								
								gesAttr.setId(++counter);
								gesAttr.setDetectionVariableId(ges.getId());
								gesAttr.setTitle(ges.getDetectionVariableName());
								gesAttr.setType(DetectionVariableType.GES.getDetectionVariableType());
								
								if (gesToGefPilots.size() == 6) 
									gesAttr.setAllPilots(true);
								else
									gesAttr.setAllPilots(false);
								
								gesAttr.setPilots(gesToGefPilots);
								gesElement.setAttr(gesAttr);
								
								List<DetectionVariable> derivedMEAs = pilotDetectionVariableRepository.findDetectionVariableByDerivedDetectionVariableIdAndPilotCodes(ges.getId(), gesToGefPilots);
								
								for (DetectionVariable mea : derivedMEAs) {
									
									List<Pilot.PilotCode> meaToGesPilots = pilotDetectionVariableRepository.findPilotCodesByDetectionVariableIdAndDerivedDetectionVariableIdAndDetectionVariableType (mea.getDetectionVariableType(), mea.getId(), ges.getId());
									
									if (!meaToGesPilots.isEmpty()) {
										
										OJDataTreeDetectionVariableSingleElem meaElement = new OJDataTreeDetectionVariableSingleElem ();
										OJDataTreeDetectionVariableAttribute meaAttr = new OJDataTreeDetectionVariableAttribute ();
										
										meaAttr.setId(++counter);
										meaAttr.setDetectionVariableId(mea.getId());
										meaAttr.setTitle(mea.getDetectionVariableName());
										meaAttr.setType(DetectionVariableType.MEA.getDetectionVariableType());
										
										if (meaToGesPilots.size() == 6) 
											meaAttr.setAllPilots(true);
										else
											meaAttr.setAllPilots(false);
										
										meaAttr.setPilots(meaToGesPilots);
										meaElement.setAttr(meaAttr);
										
										gesChildren.add(meaElement);
									}
								}
								
								gesElement.setChildren(gesChildren);
								gefChildren.add(gesElement);
							}
						}
						
						gefElement.setChildren(gefChildren);
						gfgChildren.add(gefElement);
					}
				}
				
				gfgElement.setChildren(gfgChildren);
				detectionVariables.add(gfgElement);
			}
		}
		
		response.setDetectionVariables(detectionVariables);
		
		response.setPilots(pilotRepository.findAll());
		
		response.setSocioEconomics(Arrays.asList("sex", "marital_status", "age_group", 
				"education", "cohabiting", "informal_caregiver_ability", "quality_housing", "quality_neighborhood", "working"));
		
		return JerseyResponse.build(objectMapper.writeValueAsString(response));

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
	@Path("graphData")
	public Response getGraphData(@QueryParam(value = "pilotCode") String pilotCode,
			@QueryParam(value = "detectionVariable") String detectionVariable,
			@QueryParam(value = "intervalStart") String intervalStart,
			@QueryParam(value = "intervalEnd") String intervalEnd,
			@QueryParam(value = "category") String category,
			@QueryParam(value = "comparison") String comparison,
			@Context ServletConfig sc) throws Exception {

		List<String> categories = new ArrayList <String> ();
		
		List<List<Filter>> allCategoryFilters = new ArrayList<List<Filter>>();
		
		List<List<Filter>> allPilotsFilters = new ArrayList<List<Filter>>();
		
		List<List<Filter>> allVariablesFilters = new ArrayList<List<Filter>>();
		
		List<List<Filter>> allTimesFilters = new ArrayList<List<Filter>>();
		
		List<List<Filter>> allFilters = new ArrayList<List<Filter>>();
		
		Map<String, Object> inQueryParams = new HashMap<String, Object> ();

		List<String> pilotCodes = new ArrayList<String> ();
		
		if(pilotCode != null) 
			for (String s :Arrays.asList(pilotCode.split(" "))) 
				pilotCodes.add (s);
		List<Long> detectionVariableIDs = new ArrayList <Long> ();
		
		if(detectionVariable != null) {
			List<String> detectionVariables = Arrays.asList(detectionVariable.split(" "));
			for (String s : detectionVariables) 
				detectionVariableIDs.add(Long.parseLong(s));
		}
		
		Boolean comp = null;

		if (comparison != null)
			comp = Boolean.parseBoolean(comparison);

		allPilotsFilters = groupAnalyticsService.createAllFiltersFromPilotCodes(pilotCodes, comp);

		allVariablesFilters = groupAnalyticsService.createAllFiltersFromVariables (detectionVariableIDs);

		if (category != null)
			categories = Arrays.asList(category.split(" "));

		if (!categories.isEmpty())
			allCategoryFilters = groupAnalyticsService.createAllCategoryFilters (categories);
		
		OffsetDateTime intervalStartODT = null;
		OffsetDateTime intervalEndODT = null;
		
		if(intervalStart != null) intervalStartODT = LocalDate.parse(intervalStart.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		if(intervalEnd != null) intervalEndODT = LocalDate.parse(intervalEnd.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		if (intervalStartODT != null) {
			if (intervalEndODT == null) 
				intervalEndODT = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).atOffset(ZoneOffset.UTC);
			
			allTimesFilters = groupAnalyticsService.createAllTimeFilters (intervalStartODT, intervalEndODT, comparison, categories.size());
		}

		allFilters = groupAnalyticsService.createAllFilters(allVariablesFilters, allPilotsFilters, allCategoryFilters, allTimesFilters);

		GenericTableData tableData = new GenericTableData();
		
		for (List<Filter> filter : allFilters) {
			Object[] dataAvg = viewGroupAnalyticsDataRepository.doQueryWithFilterAggr(filter, "grAn", inQueryParams);
			tableData = groupAnalyticsService.addGenericTableData(filter, dataAvg, comp, tableData, pilotCodes, categories.isEmpty());
		}

		return JerseyResponse.build(objectMapper.writeValueAsString(tableData));
	}
	
	@POST
	@Consumes("text/plain")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM})
	@Path("groupsAndSeries")
	public Response getGroupsAndSeries (String url) throws JsonProcessingException {

		GroupAnalyticsResponse response = new GroupAnalyticsResponse();

		boolean comparison = url.contains("comparison=true");
		boolean comp = url.contains("comparison");

		HashMap<String, List<String>> socioEconomics = groupAnalyticsService.createSocioEconomicsMap();

		ResponseEntity<GenericTableData> graphResponse = restTemplate().getForEntity(url, GenericTableData.class);

		GenericTableData json = graphResponse.getBody();		

		// categories
		List<String> categories = groupAnalyticsService.getPropertyFromURL(url, "category");
		Collections.reverse(categories);
		
		// dates
		List<String> datesStringList = null;

		if (comparison == false) {
			datesStringList = groupAnalyticsService.createDateList(url); 
		}
		
		// create groups all scenarios
		List<?> groups = new ArrayList<GroupAnalyticsGroups>();
		if (url.contains("category")) {
			groups = groupAnalyticsService.createGroups(categories, socioEconomics, datesStringList, comparison, comp);
		} else {
			groups = datesStringList;
		}
		response.setGroups(groups);
		

		// create series all scenarios
		List<GroupAnalyticsSeries> series = groupAnalyticsService.createSeries(comparison, json);

		response.setSeries(series);

		return JerseyResponse.build(objectMapper.writeValueAsString(response));
	}

}
