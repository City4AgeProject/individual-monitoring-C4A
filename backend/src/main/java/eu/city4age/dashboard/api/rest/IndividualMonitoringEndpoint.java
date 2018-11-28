package eu.city4age.dashboard.api.rest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.exceptions.JsonEmptyException;
import eu.city4age.dashboard.api.jpa.DerivedMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.jpa.VmvFilteringRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;
import eu.city4age.dashboard.api.pojo.dto.Item;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresData;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresGroups;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresItems;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresLegend;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresLegendItems;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresSeries;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataSet;
import eu.city4age.dashboard.api.pojo.json.clusteredMeasures.ClusteredMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.IndividualMonitoringService;
import io.swagger.annotations.ApiParam;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(IndividualMonitoringEndpoint.PATH)
public class IndividualMonitoringEndpoint {

	public static final String PATH = "individualMonitoring";

	static protected Logger logger = LogManager.getLogger(IndividualMonitoringEndpoint.class);

	@Context
	private ContextResolver<ObjectMapper> mapperResolver;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	private NUIRepository nuiRepository;

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
	private IndividualMonitoringService individualMonitoringService;
	
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

			TreeSet<DataIdValue> monthLabels = individualMonitoringService.createMonthLabels(list);
			
			if(parentFactorId == null) {
				OJDiagramFrailtyStatus frailtyStatus = individualMonitoringService.transformToDto(list, monthLabels);
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

			List<ViewGefCalculatedInterpolatedPredictedValues> gfvsList = individualMonitoringService.convertToViewGFVs(derivedMeasures);
			
			TreeSet<DataIdValue> monthLabels = individualMonitoringService.createMonthLabels(gfvsList);

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
					with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(individualMonitoringService.getClusteredSeries (realPath, userInRoleId, detectionVariableId));
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
			cmli.setColor(individualMonitoringService.getColorsOfClusters(numOfClusters)[i]);
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
		
		List<VmvFiltering> vfs = vmvFilteringRepository.findFilterTypeByVmvId(data.getVmvid());
		
		for (int i = 0; i < data.getGroups().size(); i++) {
			LocalDate groupDate = LocalDate.parse(data.getGroups().get(i));
			Long id = data.getVmvid().get(i);
			
			curr = YearMonth.from(groupDate);
			
			dataIDs.add(id);
			dataIDsStrings.add(id.toString());
			Character filterType = null;
			for (VmvFiltering vf : vfs) {
				if (vf.getVmvId().equals(id)) {
					filterType = 'E';
					break;
				}
			}
			
			for (int j = 0; j < numOfClusters; j++) {
				String cluster = data.getCluster().get(j).getItems().get(i);
				if (!cluster.equals("null")) {
					List<String> categories = new ArrayList<String> ();
					String name = data.getCluster().get(j).getName();
					categories.add(name);
					
					if (filterType != null)
						categories.add(filterTypeDescription.get(filterType));
					
					items.add(new ClusteredMeasuresItems(id.toString(), cluster, "circle" , "8", 
							individualMonitoringService.getColorsOfClusters(numOfClusters)[j], "value: " + cluster + 
							"\ngroup: " + data.getGroups().get(i) + "\ncluster: " + data.getCluster().get(j).getName(), 
							filterType != null ? "images/X.png" : null, 
							filterType != null ? "images/X_sel.png" : null, categories));
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

}