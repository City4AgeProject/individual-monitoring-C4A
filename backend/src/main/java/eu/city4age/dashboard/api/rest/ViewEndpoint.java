package eu.city4age.dashboard.api.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.jpa.FilterTypeRepository;
import eu.city4age.dashboard.api.jpa.VmvFilteringRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
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
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;	
	
	@Autowired
	private FilterTypeRepository filterTypeRepository;
	
	@Autowired
	private VmvFilteringRepository vmvFilteringRepository;
	
	@Autowired
	private ViewService viewService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getNuiValues/userInRoleId/{userInRoleId}")
	public Response getNuiValuesMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @QueryParam("varName") String varName,
			@ApiParam(hidden = true) @QueryParam("varId") Long varId) throws JsonProcessingException, JsonEmptyException {

		List<NumericIndicatorValue> nuis;
		if(varName==null) {
			nuis = nuiRepository.getNuisForAllMea(userInRoleId);
		}else {
			if(varName.compareTo("ges")==0) {
				nuis = nuiRepository.getNuisForSelectedGes(userInRoleId, varId);
			}else if(varName.compareTo("mea")==0) {
				nuis = nuiRepository.getNuisForSelectedMea(userInRoleId, varId);
			}else {
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

			TreeSet<DataIdValue> monthLabels = viewService.createMonthLabels(list);
			
			if(parentFactorId == null) {
				OJDiagramFrailtyStatus frailtyStatus = viewService.transformToDto(list, monthLabels);
				response.setFrailtyStatus(frailtyStatus);
			}

			response.getGroups().addAll(monthLabels);

			Set<DetectionVariable> dvs = new HashSet<DetectionVariable>();

			for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
				DetectionVariable dv = new DetectionVariable();
				dv.setId(gef.getId().getDetectionVariableId());
				dv.setDetectionVariableName(gef.getDetectionVariableName());
				dvs.add(dv);
			}

			for (DetectionVariable dv : dvs) {

				String detectionVariableName = dv.getDetectionVariableName();

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie series = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(
						detectionVariableName);

				for (ViewGefCalculatedInterpolatedPredictedValues gef : list) {
					Boolean gefAdded = false;

					if (gefAdded != true && dv.getId().equals(gef.getId().getDetectionVariableId())) {
						
						series.getItems().add(new Item(gef.getId().getId(), gef.getGefValue(), gef.getId().getDataType(), gef.getId().getDetectionVariableId(), gef.getId().getTimeIntervalId()));
						gefAdded = true;
					}
				}
				((DataSet)response).getSeries().add(series);
			}
			
			dvs.clear();

		}

		return JerseyResponse.build(objectMapper.writeValueAsString(response));

	}

	@SuppressWarnings("deprecation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDerivedMeasures/userInRoleId/{userInRoleId}/parentFactorId/{parentFactorId}")
	public Response getDerivedMeasures(@PathParam("userInRoleId") Long userInRoleId, @PathParam("parentFactorId") Long parentFactorId) throws JsonProcessingException {

		DataSet response = new DataSet();

		List<Object[]> derivedMeasures = nativeQueryRepository.computeDerivedMeasures(userInRoleId, parentFactorId);

		if (derivedMeasures != null && !derivedMeasures.isEmpty()) {

			List<ViewGefCalculatedInterpolatedPredictedValues> gfvsList = viewService.convertToViewGFVs(derivedMeasures);
			
			TreeSet<DataIdValue> monthLabels = viewService.createMonthLabels(gfvsList);

			response.getGroups().addAll(monthLabels);
			
			Set<DetectionVariable> dvs = new HashSet<DetectionVariable>();

			for (Object[] derivedMeasure : derivedMeasures) {
				DetectionVariable dv = new DetectionVariable();
				dv.setId(Long.valueOf((Integer) derivedMeasure[1]));
				dv.setDetectionVariableName(detectionVariableRepository.findOne(Long.valueOf((Integer) derivedMeasure[1])).getDetectionVariableName());
				dvs.add(dv);
			}

			for (DetectionVariable dv : dvs) {

				String detectionVariableName = dv.getDetectionVariableName();

				eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie series = new eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie(
						detectionVariableName);

				for (Object[] derivedMeasure : derivedMeasures) {
					Boolean derivedMeasureAdded = false;
					Long derivedMeasureId = Long.valueOf((Integer) derivedMeasure[1]);
					if (derivedMeasureAdded != true && dv.getId().equals(derivedMeasureId)) {
						TimeInterval ti = timeIntervalRepository.findOne(Long.valueOf((Integer) derivedMeasure[4]));
						Date intervalStart = ti.getIntervalStart();
						int year = intervalStart.getYear() + 1900;
						logger.info("year: " + year);
						int month = intervalStart.getMonth() + 1;
						logger.info("month: " + month);
						StringBuilder monthLabelBuilder = new StringBuilder();
						monthLabelBuilder.append(year).append("/");
						if (month < 10) {
							monthLabelBuilder.append(0).append(month);
						} else {
							monthLabelBuilder.append(month);
						}
						series.getItems().add(new Item((BigDecimal) derivedMeasure[5], Long.valueOf((Integer) derivedMeasure[1]), Long.valueOf((Integer) derivedMeasure[4]), monthLabelBuilder.toString()));
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
	@Path("clusteredMeasures/userInRoleId/{userInRoleId}/detectionVariableId/{detectionVariableId}")
	public Response getClusteredMeasuresMea(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("detectionVariableId") Long detectionVariableId,
			@Context ServletConfig sc) throws JsonProcessingException, JsonEmptyException {
		
		String realPath = sc.getServletContext().getRealPath("/WEB-INF/classes/python/");
		realPath += "\\";
		//logger.info("arg: /WEB-INF/classes/python/     real path: " + realPath);
		
		ClusteredMeasuresData response = new ClusteredMeasuresData();
		
		ClusteredMeasuresDeserializer data = new ClusteredMeasuresDeserializer();
		
		try {
			data = objectMapper.readerFor(ClusteredMeasuresDeserializer.class).
					with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(getClusteredSeries (realPath, userInRoleId, detectionVariableId));
		} catch (Exception e) {
			logger.info("exception u citanju");
			logger.info(e.getMessage());
			return JerseyResponse.build(objectMapper.writeValueAsString(""));
		}
		
		List<ClusteredMeasuresGroups> groups = new ArrayList <ClusteredMeasuresGroups> ();
		
		ClusteredMeasuresGroups cmg = null;
		List<String> subgroups = null;
		YearMonth curr = null;
		
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
					cmg.setName(curr.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));				
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
				cmg.setName(curr.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
				groups.add(cmg);
			}
		}
		response.setGroups(groups); 
		
		ClusteredMeasuresSeries cms = new ClusteredMeasuresSeries();
		cms.setName(detectionVariableRepository.findOne(detectionVariableId).getDetectionVariableName());
		cms.setColor("#000000");
		cms.setLineStyle("dotted");
		cms.setLineWidth("0.25");
		cms.setMarkerDisplayed("on");		
		
		List<ClusteredMeasuresItems> items = new ArrayList<ClusteredMeasuresItems> ();
		List<ClusteredMeasuresLegendItems> legendItems = new ArrayList<ClusteredMeasuresLegendItems> ();
		ClusteredMeasuresLegend legend = new ClusteredMeasuresLegend ();
		
		int numOfClusters = data.getCluster().size();	
		
		//String[] markerShapes = {"circle", "square", "plus", "diamond", "triangleDown"}; // dodati ostale
		
		//String[] markerSizes = {"10", "10"}; // dodati ostale
		
		//String[] colors = {"#ff3300","#ff6600","#ff8000","#ffcc00","bfff00","#99ff33","#66ff33","#008080","#aa6e28","#800000",}; // dodati ostale
		
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
		
		
		List<Long> dataIDs = new ArrayList<Long> ();
		List<String> dataIDsStrings = new ArrayList<String> ();
		
		for (int i = 0; i < data.getGroups().size(); i++) {
			for (int j = 0; j < numOfClusters; j++) {
				if (!data.getCluster().get(j).getItems().get(i).equals("null")) {
					List<String> categories = new ArrayList<String> ();
					String name = data.getCluster().get(j).getName();
					categories.add(name);
					LocalDate groupDate = LocalDate.parse(data.getGroups().get(i));
					curr = YearMonth.from(groupDate);
					Long id = data.getVmvid().get(i);
					dataIDs.add(id);
					dataIDsStrings.add(id.toString());
					Character filterType = vmvFilteringRepository.findFilterTypeByVmvId(id);
					if (filterType != null)
						categories.add(filterTypeRepository.findOne(filterType).getFilterDescription());
					items.add(new ClusteredMeasuresItems(id.toString(), data.getCluster().get(j).getItems().get(i), "circle" , "8", 
							getColorsOfClusters(numOfClusters)[j], "value: " + data.getCluster().get(j).getItems().get(i).toString() + 
							"\ngroup: " + data.getGroups().get(i) + "\ncluster: " + data.getCluster().get(j).getName(), 
							(filterType != null && filterType.equals('E')) ? "images/X.png" : null, 
							(filterType != null && filterType.equals('E')) ? "images/X_sel.png" : null, categories));
					break;
				}
			}
		}
		
		cms.setItems(items);
		
		List<ClusteredMeasuresSeries> cmsList = new ArrayList <ClusteredMeasuresSeries> ();
		cmsList.add(cms);
		response.setSeries(cmsList);
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