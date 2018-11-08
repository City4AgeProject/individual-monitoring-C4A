package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.exceptions.JsonEmptyException;
import eu.city4age.dashboard.api.jpa.DerivedMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.jpa.ViewGroupAnalyticsDataRepository;
import eu.city4age.dashboard.api.jpa.VmvFilteringRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.Item;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.analytics.AnalyticsMetadataResponse;
import eu.city4age.dashboard.api.pojo.dto.analytics.OJDataTreeDetectionVariableAttribute;
import eu.city4age.dashboard.api.pojo.dto.analytics.OJDataTreeDetectionVariableSingleElem;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresData;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresGroups;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresItems;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresLegend;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresLegendItems;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresSeries;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsGroups;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsResponse;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsSeries;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataSet;
import eu.city4age.dashboard.api.pojo.json.clusteredMeasures.ClusteredMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.py.HiddenMarkovModelService;
import eu.city4age.dashboard.api.service.ViewService;
import io.swagger.annotations.ApiParam;
import jep.Jep;
import jep.JepConfig;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(ViewEndpoint.PATH)
public class ViewEndpoint {

	public static final String PATH = "view";

	static protected Logger logger = LogManager.getLogger(ViewEndpoint.class);

	@Context
	private ContextResolver<ObjectMapper> mapperResolver;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	private NUIRepository nuiRepository;
	
	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private VmvFilteringRepository vmvFilteringRepository;
	
	@Autowired
	private DerivedMeasureValueRepository derivedMeasureValueRepository;
	
	@Autowired
	private ViewService viewService;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private ViewGroupAnalyticsDataRepository viewGroupAnalyticsDataRepository;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getNuiValues/userInRoleId/{userInRoleId}")
	public Response getNuiValuesMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @QueryParam("varName") String varName,
			@ApiParam(hidden = true) @QueryParam("varId") Long varId) throws JsonProcessingException, JsonEmptyException {

		List<NumericIndicatorValue> nuis;
		if(varName==null) {
			nuis = nuiRepository.getNuisForAllMea(userInRoleId);
		} else {
			if(varName.compareTo("ges")==0) {
				nuis = nuiRepository.getNuisForSelectedGes(userInRoleId, varId);
			} else if(varName.compareTo("mea")==0) {
				nuis = nuiRepository.getNuisForSelectedMea(userInRoleId, varId);
			} else {
				throw new JsonEmptyException ("Variable name must be ges or mea");	
			}
		}
		return JerseyResponse.build(objectMapper.writerWithView(View.NUIView.class).writeValueAsString(nuis));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDailyMeasures/userInRoleId/{userInRoleId}")
	public Response getDailyMeasuresMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @QueryParam("varName") String varName,
			@ApiParam(hidden = true) @QueryParam("varId") Long varId) throws JsonProcessingException, JsonEmptyException {

		List<VariationMeasureValue> measures;

		if(varName==null) {
			measures = variationMeasureValueRepository.findByUser(userInRoleId);
		}else {
			if(varName.compareTo("ges")==0) {
				measures = variationMeasureValueRepository.findByUserAndGes(userInRoleId, varId);
			}else if(varName.compareTo("mea")==0) {
				measures = variationMeasureValueRepository.findByUserAndMea(userInRoleId, varId);
			}else {
				throw new JsonEmptyException ("Variable name must be ges or mea");	
			}
		}
		return JerseyResponse.build(objectMapper.writerWithView(View.VariationMeasureValueView.class).writeValueAsString(measures));

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
		
		List<ArrayList<Filter>> allCategoryFilters = new ArrayList<ArrayList<Filter>>();
		
		List<ArrayList<Filter>> allPilotsFilters = new ArrayList<ArrayList<Filter>>();
		
		List<ArrayList<Filter>> allVariablesFilters = new ArrayList<ArrayList<Filter>>();
		
		List<ArrayList<Filter>> allTimesFilters = new ArrayList<ArrayList<Filter>>();
		
		List<ArrayList<Filter>> allFilters = new ArrayList<ArrayList<Filter>>();
		
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
		
		Boolean comp = false;

		if (comparison != null)
			comp = Boolean.parseBoolean(comparison);

		allPilotsFilters = viewService.createAllFiltersFromPilotCodes(pilotCodes, comp);

		allVariablesFilters = viewService.createAllFiltersFromVariables (detectionVariableIDs);

		if (category != null)
			categories = Arrays.asList(category.split(" "));

		if (!categories.isEmpty())
			allCategoryFilters = viewService.createAllCategoryFilters (categories);
		
		OffsetDateTime intervalStartODT = null;
		OffsetDateTime intervalEndODT = null;
		
		if(intervalStart != null) intervalStartODT = LocalDate.parse(intervalStart.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		if(intervalEnd != null) intervalEndODT = LocalDate.parse(intervalEnd.substring(0, 10), 
				DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC);
		
		if (intervalStartODT != null) {
			if (intervalEndODT == null) 
				intervalEndODT = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).atOffset(ZoneOffset.UTC);
			
			allTimesFilters = viewService.createAllTimeFilters (intervalStartODT, intervalEndODT, comp);
		}

		allFilters = viewService.createAllFilters (allVariablesFilters, allPilotsFilters, allCategoryFilters, allTimesFilters);
		
		GenericTableData tableData = new GenericTableData();
		
		for (ArrayList<Filter> filter : allFilters) {
			Object[] dataAvg = viewGroupAnalyticsDataRepository.doQueryWithFilterAggr(filter, "grAn", inQueryParams);
			tableData = viewService.addGenericTableData(filter, dataAvg, comp, tableData, pilotCodes);
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

		HashMap<String, List<String>> socioEconomics = createSocioEconomicsMap();

		ResponseEntity<GenericTableData> graphResponse = restTemplate().getForEntity(url, GenericTableData.class);

		GenericTableData json = graphResponse.getBody();

		List<List<String>> data = json.getData();

		// categories
		List<String> categories = getPropertyFromURL(url, "category");
		Collections.reverse(categories);
		
		// dates
		List<String> datesStringList = null; 

		if (comparison == false) {
			datesStringList = createDateList(url); 
		}
		
		// create groups all scenarios		
		List<Object> groups = createGroups(categories, socioEconomics, datesStringList, comparison);
		response.setGroups(groups);
		

		// create series all scenarios
		GroupAnalyticsSeries serie = new GroupAnalyticsSeries();
		List<GroupAnalyticsSeries> series = new ArrayList<GroupAnalyticsSeries>();
		
		String previousPilot = data.get(0).get(5);
		String previousDetectionVariableName = data.get(0).get(3);

		String firstPilot = data.get(0).get(5);

		serie.setName(previousDetectionVariableName);
		serie.setPilot(previousPilot);

		List<BigDecimal> items = new ArrayList<BigDecimal>();

		for (List<String> entry : data) {

			if (!(previousDetectionVariableName.contains(entry.get(3)) && previousPilot.contains(entry.get(5)))) {

				previousDetectionVariableName = entry.get(3);
				previousPilot = entry.get(5);

				serie.setItems(items);
				series.add(serie);
				items = new ArrayList<BigDecimal>();

				serie = new GroupAnalyticsSeries();

				serie.setName(entry.get(3));
				serie.setPilot(previousPilot);

				if (comparison && !entry.get(5).equals(firstPilot)) {
					serie.setAssignedToY2("on");
					serie.setDisplayInLegend("off");
				}

			}

			if (entry.get(0).matches("")) {
				items.add(null);
			} else {
				if (comparison && !entry.get(5).equals(firstPilot)) {
					items.add(BigDecimal.valueOf(0 - Double.parseDouble(entry.get(0))));
				} else {
					items.add(BigDecimal.valueOf(Double.parseDouble(entry.get(0))));
				}
			}

		}
		serie.setItems(items);
		series.add(serie);

		response.setSeries(series);

		return JerseyResponse.build(objectMapper.writeValueAsString(response));
	}

	public List<Object> createGroups(List<String> categories,
			HashMap<String, List<String>> socioEconomics, List<String> datesStringList, boolean comparison) {

		int cnt = categories.size();

		List<GroupAnalyticsGroups> groups = new ArrayList<GroupAnalyticsGroups>();

		if (cnt - 1 == 0) {
			if (comparison == true) {				
				GroupAnalyticsGroups group1 = new GroupAnalyticsGroups();
				group1.setGroups(socioEconomics.get(categories.get(cnt - 1)));
				return new ArrayList<Object>(socioEconomics.get(categories.get(cnt - 1)));
			} else {
				for (String category1 : socioEconomics.get(categories.get(cnt - 1))) {
					GroupAnalyticsGroups group1 = new GroupAnalyticsGroups();
					group1.setName(category1);
					group1.setGroups(datesStringList);
					groups.add(group1);
				}
				return new ArrayList<Object>(groups);
			}
		} else {

			ArrayList<String> currCategories = new ArrayList<String> ();
			for (int i = 0; i < cnt - 1; i++) 
				currCategories.add(categories.get(i));

			String type = categories.get(cnt - 1);

			List<Object> alreadyCreatedGroups = createGroups(currCategories, socioEconomics, datesStringList, comparison);

			for (String category : socioEconomics.get(type)) {
				GroupAnalyticsGroups group = new GroupAnalyticsGroups();
				group.setName(category);
				group.setGroups(alreadyCreatedGroups);
				groups.add(group);

			}
			return new ArrayList<Object>(groups);
		}
	}

	/**
	 * @param url
	 * @return
	 */
	private List<String> createDateList(String url) {
		List<String> datesStringList;
		String startDate = "";
		String endDate = "";
		for (String str : Arrays.asList(url.split("&"))) {
			if (str.contains("intervalStart"))
				startDate = str.split("=")[1];
			if (str.contains("intervalEnd"))
				endDate = str.split("=")[1];
		}
		LocalDate intervalStartLD = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate intervalEndLD = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		datesStringList = new ArrayList<String>();
		while (intervalStartLD.isBefore(intervalEndLD)) {
			String intervalStart = intervalStartLD.format(DateTimeFormatter.ofPattern("yyyy/MM"));
			datesStringList.add(intervalStart);
			intervalStartLD = intervalStartLD.plusMonths(1L);
		}
		return datesStringList;
	}

	/**
	 * @return
	 */
	public HashMap<String, List<String>> createSocioEconomicsMap() {
		HashMap<String, List<String>> socioEconomics = new HashMap <String, List<String>> ();
		socioEconomics.put("sex", Arrays.asList("m", "f"));
		socioEconomics.put("marital_status", Arrays.asList("s", "m", "w", "d", "t"));
		socioEconomics.put("age_group", Arrays.asList("50-59", "60-69", "70-79", "80-89", "90+"));
		socioEconomics.put("education", Arrays.asList("none", "primary", "secondary", "tertiary"));
		socioEconomics.put("cohabiting", Arrays.asList("alone", "family", "friends", "other"));
		socioEconomics.put("informal_caregiver_ability", Arrays.asList("t", "f"));
		socioEconomics.put("quality_housing", Arrays.asList("low", "average", "high"));
		socioEconomics.put("quality_neighborhood", Arrays.asList("low", "average", "high"));
		socioEconomics.put("working", Arrays.asList("t", "f"));
		return socioEconomics;
	}

	/**
	 * @param url
	 * @param list
	 * @param property
	 * @return
	 */
	public List<String> getPropertyFromURL(String url, String property) {
		
		List<String> propertyList = new ArrayList<String>();
		for (String str : Arrays.asList(url.split("&"))) {
			if (str.contains(property)) {
				propertyList = Arrays.asList(str.split("=")[1].split(" "));
			}
		}
		return propertyList;
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
	@Path("getDiagramData/careRecipientId/{careRecipientId}")
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	public Response getDiagramData(@PathParam("careRecipientId") Long careRecipientId,
			@QueryParam("parentFactorId") Long parentFactorId) throws JsonProcessingException {

		DataSet response = new DataSet();

		List<ViewGefCalculatedInterpolatedPredictedValues> list;
		if(parentFactorId != null) {
			list = viewGefCalculatedInterpolatedPredictedValuesRepository.findByCareRecipientIdAndParentFactorIds(careRecipientId, Arrays.asList(parentFactorId));
		} else {
			list = viewGefCalculatedInterpolatedPredictedValuesRepository.findByCareRecipientId(careRecipientId);
		}

		if (list != null && !list.isEmpty()) {

			TreeSet<DataIdValue> monthLabels = viewService.createMonthLabels(list);
			
			if(parentFactorId == null) {
				OJDiagramFrailtyStatus frailtyStatus = viewService.transformToDto(list, monthLabels);
				response.setFrailtyStatus(frailtyStatus);
			}

			response.getGroups().addAll(monthLabels);

			Set<DetectionVariable> dvs = new HashSet<DetectionVariable>();

			for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
				DetectionVariable dv = new DetectionVariable();
				dv.setId(gef.getDetectionVariableId());
				dv.setDetectionVariableName(gef.getDetectionVariableName());
				dvs.add(dv);
			}

			for (DetectionVariable dv : dvs) {

				String detectionVariableName = dv.getDetectionVariableName();

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie series = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(
						detectionVariableName);

				for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
					Boolean gefAdded = false;

					if (gefAdded != true && dv.getId().equals(gef.getDetectionVariableId())) {
						
						series.getItems().add(new Item(gef.getId().getId(), gef.getGefValue(), gef.getId().getDataType(), gef.getDetectionVariableId(), gef.getTimeIntervalId()));
						gefAdded = true;
					}
				}
				((DataSet)response).getSeries().add(series);
			}
			
			dvs.clear();

		}

		return JerseyResponse.build(objectMapper.writeValueAsString(response));

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDerivedMeasures/userInRoleId/{userInRoleId}/parentFactorId/{parentFactorId}")
	public Response getDerivedMeasures(@PathParam("userInRoleId") Long userInRoleId, @PathParam("parentFactorId") Long parentFactorId) throws JsonProcessingException {

		DataSet response = new DataSet();
		
		List<DerivedMeasureValue> derivedMeasures = derivedMeasureValueRepository.findByUserInRoleIdAndParentFactorId(userInRoleId, parentFactorId);

		if (derivedMeasures != null && !derivedMeasures.isEmpty()) {

			List<ViewGefCalculatedInterpolatedPredictedValues> gfvsList = viewService.convertToViewGFVs(derivedMeasures);
			
			TreeSet<DataIdValue> monthLabels = viewService.createMonthLabels(gfvsList);

			response.getGroups().addAll(monthLabels);
			
			Set<DetectionVariable> dvs = new HashSet<DetectionVariable>();

			for (DerivedMeasureValue derivedMeasure : derivedMeasures) {
				DetectionVariable dv = new DetectionVariable();
				dv.setId(derivedMeasure.getDetectionVariableId());
				dv.setDetectionVariableName(detectionVariableRepository.findOne(Long.valueOf(derivedMeasure.getDetectionVariableId())).getDetectionVariableName());
				dvs.add(dv);
			}			

			for (DetectionVariable dv : dvs) {

				String detectionVariableName = dv.getDetectionVariableName();

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie series = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(
						detectionVariableName);

				for (DerivedMeasureValue derivedMeasure : derivedMeasures) {
					Boolean derivedMeasureAdded = false;
					Long derivedMeasureId = Long.valueOf(derivedMeasure.getDetectionVariableId());
					if (derivedMeasureAdded != true && dv.getId().equals(derivedMeasureId)) {
						TimeInterval ti = derivedMeasure.getTimeInterval();
						Date intervalStart = ti.getIntervalStart();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(intervalStart);
						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH) + 1;
						StringBuilder monthLabelBuilder = new StringBuilder();
						monthLabelBuilder.append(year).append("/");
						if (month < 10) {
							monthLabelBuilder.append(0).append(month);
						} else {
							monthLabelBuilder.append(month);
						}
						series.getItems().add(new Item(derivedMeasure.getDmValue(), derivedMeasure.getDetectionVariableId(), derivedMeasure.getTimeInterval().getId(), monthLabelBuilder.toString()));
						derivedMeasureAdded = true;
					}
				}
				((DataSet)response).getSeries().add(series);
			}
			
			dvs.clear();

		}
		return JerseyResponse.build(objectMapper.writeValueAsString(response));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("clusteredMeasures/userInRoleId/{userInRoleId}/detectionVariableId/{detectionVariableId}/locale/{localeString}")
	public Response getClusteredMeasuresMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("detectionVariableId") Long detectionVariableId,
			@ApiParam(hidden = true) @PathParam("localeString") String localeString,
			@Context ServletConfig sc) throws JsonProcessingException, JsonEmptyException {
		
		String realPath = sc.getServletContext().getRealPath("/WEB-INF/classes/python/");
		realPath += "/";
		//logger.info("arg: /WEB-INF/classes/python/     real path: " + realPath);
		
		HashMap <String, Locale> languageMap = new HashMap <String, Locale> ();
		
		languageMap.put("en", Locale.ENGLISH);
		languageMap.put("el", new Locale("el", "GR"));
		languageMap.put("es", new Locale("es", "ES"));
		languageMap.put("it", new Locale("it", "IT"));
		languageMap.put("fr", new Locale("fr", "FR"));
		languageMap.put("zh-Hant", Locale.ENGLISH);
		
		ClusteredMeasuresData response = new ClusteredMeasuresData();
		
		ClusteredMeasuresDeserializer data = new ClusteredMeasuresDeserializer();
		
		try {
			logger.info ("JEP started");
			data = objectMapper.readerFor(ClusteredMeasuresDeserializer.class).
					with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(getClusteredSeries (realPath, userInRoleId, detectionVariableId));
		} catch (Exception e) {
			logger.info("exception u citanju");
			logger.info(e.getMessage());
			return JerseyResponse.build(objectMapper.writeValueAsString(""));
		}
		
		logger.info ("JEP finnished");
		
		List<ClusteredMeasuresGroups> groups = new ArrayList <ClusteredMeasuresGroups> ();
		
		ClusteredMeasuresGroups cmg = null;
		List<String> subgroups = null;
		YearMonth curr = null;
		
		logger.info ("groups started");
		
		if (!detectionVariableRepository.findOne(detectionVariableId).getDefaultTypicalPeriod().equals("mon")) {
			for (String group : data.getGroups()) {
				
				LocalDate groupDate = LocalDate.parse(group);
							
				// TODO ovde ce ici provera da li je mera mesecna; sad je HC, pa je dnevna po defaultu
				if (cmg == null || !YearMonth.from(groupDate).equals(curr)) {
					if (cmg != null) {
						cmg.setGroups(subgroups);
						groups.add(cmg);
					}
					cmg = new ClusteredMeasuresGroups ();
					curr = YearMonth.from(groupDate);
					subgroups = new ArrayList <String> ();
					cmg.setName(curr.format(DateTimeFormatter.ofPattern("MMMM yyyy", languageMap.get(localeString))));				
				}
				subgroups.add(group.substring(8, 10));			
				//logger.info("subgroups.size: " + subgroups.size());
				//groups.add(group.format(DateTimeFormatter.ISO_LOCAL_DATE));
			}
			cmg.setGroups(subgroups);
			groups.add(cmg);			
		}
		else {
			for (String group : data.getGroups()) {
				LocalDate groupDate = LocalDate.parse(group);
				cmg = new ClusteredMeasuresGroups ();
				curr = YearMonth.from(groupDate);
				cmg.setName(curr.format(DateTimeFormatter.ofPattern("MMMM yyyy", languageMap.get(localeString))));
				groups.add(cmg);
			}
		}
		response.setGroups(groups); 
		
		logger.info ("groups finished");
		
		logger.info ("series started");
		
		ClusteredMeasuresSeries cms = new ClusteredMeasuresSeries();
		cms.setName(detectionVariableRepository.findOne(detectionVariableId).getDetectionVariableName());
		cms.setColor("#000000");
		cms.setLineStyle("dotted");
		cms.setLineWidth("0.25");
		cms.setMarkerDisplayed("on");		
		cms.setDisplayInLegend("off");
		
		List<ClusteredMeasuresItems> items = new ArrayList<ClusteredMeasuresItems> ();
		List<ClusteredMeasuresLegendItems> legendItems = new ArrayList<ClusteredMeasuresLegendItems> ();
		ClusteredMeasuresLegend legend = new ClusteredMeasuresLegend ();
		
		int numOfClusters = data.getCluster().size();	
		
		//String[] markerShapes = {"circle", "square", "plus", "diamond", "triangleDown"}; // dodati ostale
		
		//String[] markerSizes = {"10", "10"}; // dodati ostale
		
		//String[] colors = {"#ff3300","#ff6600","#ff8000","#ffcc00","bfff00","#99ff33","#66ff33","#008080","#aa6e28","#800000",}; // dodati ostale
		
		logger.info ("legend started");
		
		for (int i = 0; i < numOfClusters; i++) {
			ClusteredMeasuresLegendItems cmli = new ClusteredMeasuresLegendItems ();
			cmli.setText(data.getCluster().get(i).getName());
			cmli.setColor(getColorsOfClusters(numOfClusters)[i]);
			cmli.setMarkerShape("circle");
			cmli.setMarkerSize("10");
			cmli.setDrilling("on");
			List<String> legendCategories = new ArrayList<String> ();
			legendCategories.add(cmli.getText());
			cmli.setCategories(legendCategories);
			legendItems.add(cmli);
		}
		
		ClusteredMeasuresLegendItems cmli = new ClusteredMeasuresLegendItems ();
		cmli.setText("Excluded");
		List<String> legendCategories = new ArrayList<String> ();
		legendCategories.add("Exclude");
		cmli.setCategories(legendCategories);
		cmli.setSymbolType("image");
		cmli.setSource("images/X.png");
		cmli.setDrilling("on");
		legendItems.add(cmli);
		
		logger.info ("legend finished");
		
		
		List<Long> dataIDs = new ArrayList<Long> ();
		List<String> dataIDsStrings = new ArrayList<String> ();
		
		logger.info ("items started");
		
		HashMap <Character, String> filterTypeDescription = new HashMap <Character, String> ();
		filterTypeDescription.put('C', "Confirm");
		filterTypeDescription.put('E', "Exclude");
		filterTypeDescription.put('Q', "Questionable");
		
		for (int i = 0; i < data.getGroups().size(); i++) {
			LocalDate groupDate = LocalDate.parse(data.getGroups().get(i));
			Long id = data.getVmvid().get(i);
			
			curr = YearMonth.from(groupDate);
			
			dataIDs.add(id);
			dataIDsStrings.add(id.toString());
			List<VmvFiltering> vf = vmvFilteringRepository.findFilterTypeByVmvId(id);
			Character filterType = vf.size() > 0 ? vf.get(0).getFilterType().charAt(0) : null;
			
			for (int j = 0; j < numOfClusters; j++) {
				
				String cluster = data.getCluster().get(j).getItems().get(i);
				if (!cluster.equals("null")) {
					List<String> categories = new ArrayList<String> ();
					String name = data.getCluster().get(j).getName();
					categories.add(name);
					
					if (filterType != null)
						categories.add(filterTypeDescription.get(filterType));
					
					items.add(new ClusteredMeasuresItems(id.toString(), cluster, "circle" , "8", 
							getColorsOfClusters(numOfClusters)[j], "value: " + cluster + 
							"\ngroup: " + data.getGroups().get(i) + "\ncluster: " + data.getCluster().get(j).getName(), 
							(filterType != null && filterType.equals('E')) ? "images/X.png" : null, 
							(filterType != null && filterType.equals('E')) ? "images/X_sel.png" : null, categories));
					break;
				}
			}
		}
		
		cms.setItems(items);
		
		logger.info ("items finished");
		
		List<ClusteredMeasuresSeries> cmsList = new ArrayList <ClusteredMeasuresSeries> ();
		cmsList.add(cms);
		response.setSeries(cmsList);
		
		logger.info ("series finished");
		
		legend.setItems(legendItems);
		List<ClusteredMeasuresLegend> cmsLegendList = new ArrayList<ClusteredMeasuresLegend> ();
		cmsLegendList.add(legend);
		response.setLegend(cmsLegendList);
		
		/*List<ClusteredMeasuresAssessments> cmsAssessments = new ArrayList <ClusteredMeasuresAssessments> ();
		Map<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("dataPointsIds", dataIDs);
		
		List<Filter> filters = new ArrayList<Filter>();
		
		List<Assessment> assessmentList = assessmentRepository.doQueryWithFilter(filters, "findClusterForSelectedDataSet", inQueryParams);
		logger.info ("listSize: " + assessmentList.size());
		for (Assessment a : assessmentList) {
			//logger.info(a);
			Character filterType = vmvFilteringRepository.findFilterTypeByAssessmentId(a);
			List<Long> vmvIDs = vmvFilteringRepository.findVmvIDsByAssessment (a);
			cmsAssessments.add(new ClusteredMeasuresAssessments (a.getId(), filterTypeRepository.findOne(filterType), a.getAssessmentComment(), 
					a.getCreated(), a.getUpdated(), a.getUserInRole(), vmvIDs));
		}
		
		response.setAssessments(cmsAssessments);*/
		response.setDataIDs(dataIDsStrings);
		
		return JerseyResponse.build(objectMapper.writeValueAsString(response));
	}
	
	/*public String[] getShapesOfClusters(int num) {
		switch (num) {
		case 1:
			String [] retArray1 = {"circle"};
			return retArray1;
		case 2:
			String [] retArray2 = {"circle", "square"};
			return retArray2;
		case 3:
			String [] retArray3 = {"circle", "triangleDown", "square"};
			return retArray3;
		case 4:
			String [] retArray4 = {"circle", "triangleDown", "plus", "square"};
			return retArray4;
		case 5:
			String [] retArray5 = {"circle", "triangleDown", "diamond", "plus", "square"};
			return retArray5;
		case 6:
			String [] retArray6 = {"circle", "triangleDown", "diamond", "plus", "square", "circle"};
			return retArray6;
		case 7:
			String [] retArray7 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown"};
			return retArray7;
		case 8:
			String [] retArray8 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond"};
			return retArray8;
		case 9:
			String [] retArray9 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond", "plus"};
			return retArray9;
		case 10:
			String [] retArray10 = {"circle", "triangleDown", "diamond", "plus", "square", "circle", "triangleDown", "diamond", "plus", "square"};
			return retArray10;
		default:			
			return null;
		}	
	}
*/
	public String[] getColorsOfClusters (int num) {
				
		switch (num) {
		case 1:
			String [] retArray1 = {"#7EE500"};
			return retArray1;
		case 2:
			String [] retArray2 = {"#D2E000", "#7EE500"};
			return retArray2;
		case 3:
			String [] retArray3 = {"#D2E000", "#25EA00", "#00F7F9"};
			return retArray3;
		case 4:
			String [] retArray4 = {"#D2E000", "#7EE500", "#00F497", "#00F7F9"};
			return retArray4;
		case 5:
			String [] retArray5 = {"#DB9200", "#D2E000", "#7EE500", "#00F497", "#00F7F9"};
			return retArray5;
		case 6:
			String [] retArray6 = {"#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00F497"};
			return retArray6;
		case 7:
			String [] retArray7 = {"#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00",  "#00F497", "#00F7F9"};
			return retArray7;
		case 8:
			String [] retArray8 = {"#D31700", "#D64800", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00F497", "#00F7F9"};
			return retArray8;
		case 9:
			String [] retArray9 = {"#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9"};
			return retArray9;
		case 10:
			String [] retArray10 = {"#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9", "#0099FF"};
			return retArray10;
		case 11:
			String [] retArray11 = {"#cc0066", "#D10017", "#D63B00", "#DB9200", "#D2E000", "#7EE500", "#25EA00", "#00EF37", "#00F497", "#00F7F9", "#0099FF"};
			return retArray11;
		default:			
			return null;
		}	
	}
		
	public String getClusteredSeries(String path, Long userInRoleId, Long varId) throws Exception {
		
		String response;
		
		JepConfig jepConfig = new JepConfig ();
		jepConfig.addSharedModules("pandas");
		jepConfig.addSharedModules("numpy");
		jepConfig.addSharedModules("hmmlearn");
		jepConfig.addSharedModules("scipy");
		jepConfig.addSharedModules("sys");
		jepConfig.addSharedModules("json");
		jepConfig.addSharedModules("psycopg2");
		jepConfig.addSharedModules("csv");
		jepConfig.addSharedModules("sklearn");
		jepConfig.addSharedModules("warnings");
		jepConfig.addSharedModules("selectionCriteria");
		jepConfig.addSharedModules("data_preparation_uni");
		jepConfig.addSharedModules("data_preparation_multi");
		jepConfig.addSharedModules("learn_optimal_model");
		jepConfig.addSharedModules("learnOptimalHMMs_and_persist");
		Jep jep = new Jep (jepConfig);
		
		jep.setInteractive(true);
		
		try {
			response = HiddenMarkovModelService.clusterSingleSeries(path, jep, userInRoleId.intValue(), varId.intValue());
		} catch (Exception e) {
			logger.info("error unutar jep");
			logger.info(e.toString());
			e.printStackTrace();
			response = "error unutar jep";
		}
		//logger.info(response);
		jep.close();
		
		return response;
	}

}