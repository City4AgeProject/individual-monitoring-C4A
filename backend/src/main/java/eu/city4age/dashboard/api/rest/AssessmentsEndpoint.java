package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.AudienceRolesRepository;
import eu.city4age.dashboard.api.jpa.FilterTypeRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.jpa.VmvFilteringRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.FilterType;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValuesKey;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;
import eu.city4age.dashboard.api.pojo.dto.AssessmentJson;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramLast5Assessment;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredMeasuresAssessments;
import eu.city4age.dashboard.api.pojo.dto.clusteredMeasures.ClusteredVmv;
import eu.city4age.dashboard.api.pojo.json.AddAssessmentDeserializer;
import eu.city4age.dashboard.api.pojo.json.clusteredMeasures.AddAssessmentClusterDeserializer;
import eu.city4age.dashboard.api.pojo.json.clusteredMeasures.UpdateOrDeleteAssessmentDeserializer;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.AssessmentService;
import io.swagger.annotations.Api;
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
@Path(AssessmentsEndpoint.PATH)
@Api(value = "assessment", produces = "application/json")
public class AssessmentsEndpoint {

	public static final String PATH = "assessment";

	static protected Logger logger = LogManager.getLogger(AssessmentsEndpoint.class);

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private AudienceRolesRepository audienceRolesRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Autowired
	private VmvFilteringRepository vmvFilteringRepository;
	
	@Autowired
	private FilterTypeRepository filterTypeRepository;
	
	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;
	
	@Autowired
	private AssessmentService assessmentService;

	@Context
	private ContextResolver<ObjectMapper> mapperResolver;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@GET
	@ApiOperation("Get last five assessments for data sets in specific time interval.")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.AssessmentView.class)
	@Path("getLast5AssessmentsForDiagramTimeline/userInRoleId/{userInRoleId}/parentDetectionVariableId/{parentDetectionVariableId}/intervalStart/{intervalStart}/intervalEnd/{intervalEnd}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userInRoleId", value = "id of care recipient", required = false, dataType = "long", paramType = "path", defaultValue = "1"),
		@ApiImplicitParam(name = "parentDetectionVariableId", value = "id of parent detection variable", required = false, dataType = "long", paramType = "path", defaultValue = "2"),
		@ApiImplicitParam(name = "intervalStart", value = "start of interval", required = false, dataType = "string", paramType = "path", defaultValue = "2016-9-1"),
		@ApiImplicitParam(name = "intervalEnd", value = "end of interval", required = false, dataType = "string", paramType = "path", defaultValue = "2016-10-1")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = OJDiagramLast5Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response getLast5ForDiagram(@ApiParam(hidden = true) @PathParam("userInRoleId") Long userInRoleId,
			@ApiParam(hidden = true) @PathParam("parentDetectionVariableId") Long parentDetectionVariableId,
			@ApiParam(hidden = true) @PathParam("intervalStart") String intervalStart,
			@ApiParam(hidden = true) @PathParam("intervalEnd") String intervalEnd)
			throws JsonProcessingException {

		Timestamp intervalStartTimestamp = Timestamp.valueOf(intervalStart.concat(" 00:00:00"));
		Timestamp intervalEndTimestamp = Timestamp.valueOf(intervalEnd.concat(" 00:00:00"));

		List<Object[]> l5a = null;

		try {
			l5a = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(userInRoleId, parentDetectionVariableId, intervalStartTimestamp, intervalEndTimestamp);
		} catch (Exception e) {
			logger.info("getLastFiveForDiagram REST service - query exception: ", e);
		}
	
		return JerseyResponse.build(objectMapper.writeValueAsString(l5a != null ? assessmentService.transformToOJ(l5a) : ""));
	}

	@GET
	@ApiOperation("Find all assessments for selected data sets.")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.AssessmentView.class)
	@Path("findForSelectedDataSet/geriatricFactorValueIds/{geriatricFactorValueIds : .+}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleId", value = "id of role of auditorium", required = false, dataType = "long", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "orderById", value = "selection of ordering (1 - date asc, 2 - date desc, 3 - author name asc, 4 - author name desc, 5 - author role asc, 6 - author role desc, 7 - type)", required = false, dataType = "long", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "riskStatusWarning", value = "risk warning marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "riskStatusAlert", value = "risk alert marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "riskStatusNoRisk", value = "no risk marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "dataValidityQuestionable", value = "data questionable marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "dataValidityFaulty", value = "data faulty marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "dataValidityValid", value = "data valid marker", required = false, dataType = "boolean", paramType = "query", defaultValue = "true"),
		@ApiImplicitParam(name = "geriatricFactorValueIds", value = "id of geriatric factor object", required = false, dataType = "list", paramType = "path", defaultValue = "30")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response findForSelectedDataSet(
			@ApiParam(hidden = true) @QueryParam(value = "roleId") Long roleId,
			@ApiParam(hidden = true) @QueryParam(value = "orderById") Long orderById,
			@ApiParam(hidden = true) @QueryParam(value = "riskStatusWarning") Boolean riskStatusWarning,
			@ApiParam(hidden = true) @QueryParam(value = "riskStatusAlert") Boolean riskStatusAlert,
			@ApiParam(hidden = true) @QueryParam(value = "riskStatusNoRisk") Boolean riskStatusNoRisk,
			@ApiParam(hidden = true) @QueryParam(value = "dataValidityQuestionable") Boolean dataValidityQuestionable,
			@ApiParam(hidden = true) @QueryParam(value = "dataValidityFaulty") Boolean dataValidityFaulty,
			@ApiParam(hidden = true) @QueryParam(value = "dataValidityValid") Boolean dataValidityValid,
			@ApiParam(hidden = true) @PathParam(value = "geriatricFactorValueIds") List<PathSegment> geriatricFactorValueIds)
			throws JsonProcessingException {

		List<Assessment> aaList;

		Map<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", assessmentService.convertToListLong(geriatricFactorValueIds));

		List<Filter> filters = new ArrayList<Filter>();

		if (riskStatusWarning != null || riskStatusAlert != null || riskStatusNoRisk != null) {
			Filter byRiskStatus = new Filter();
			byRiskStatus.setName("riskStatus");
			List<Character> inParams = new ArrayList<Character>();
			if (riskStatusWarning != null && riskStatusWarning) {
				inParams.add('W');
			}
			if (riskStatusAlert != null && riskStatusAlert) {
				inParams.add('A');
			}
			if (riskStatusNoRisk != null && riskStatusNoRisk) {
				inParams.add('N');
			}
			byRiskStatus.getInParams().put("riskStatus", inParams);
			filters.add(byRiskStatus);
		}

		if (dataValidityFaulty != null || dataValidityQuestionable != null || dataValidityValid != null) {
			Filter byDataValidity = new Filter();
			byDataValidity.setName("dataValidity");
			List<Character> inParams = new ArrayList<Character>();
			if (dataValidityFaulty != null && dataValidityFaulty) {
				inParams.add('F');
			}
			if (dataValidityQuestionable != null && dataValidityQuestionable) {
				inParams.add('Q');
			}
			if (dataValidityValid != null && dataValidityValid) {
				inParams.add('V');
			}
			byDataValidity.getInParams().put("dataValidity", inParams);
			filters.add(byDataValidity);

		}

		if (roleId != null) {
			Filter byRoleId = new Filter();
			byRoleId.setName("roleId");
			byRoleId.getInParams().put("roleId", roleId);
			filters.add(byRoleId);
		}

		aaList = assessmentRepository.doQueryWithFilter(filters, "findForSelectedDataSet", inQueryParams);
		List<Assessment> aa;

		if (orderById != null) {
			aa = assessmentService.orderByForFiltering(aaList, orderById);
		} else {
			aa = aaList;
		}

		return JerseyResponse.build(objectMapper.writerWithView(View.AssessmentView.class).writeValueAsString(aa));

	}

	/**
	 * Adds assessments for selected data set
	 * 
	 * @param json
	 *            assessment data json
	 * @return 200 OK, after successful sending of data
	 * @throws JsonProcessingException
	 *             JsonProcessingException exception
	 * @throws IOException
	 *             IOException exception
	 */
	@POST
	@ApiOperation("Add assessment for selected data sets.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON, "application/javascript"})
	@Path("addForSelectedDataSet")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response addForSelectedDataSet(@HeaderParam("Authorization") String jwt, String json) throws JsonProcessingException, IOException {
		
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();
		List<VmvFiltering> vmvFilteringList = new ArrayList<VmvFiltering>();
		
		AddAssessmentDeserializer data = objectMapper.readerFor(AddAssessmentDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);

		DecodedJWT token;
		try {
			token = JWT.decode(jwt);
			String username = token.getClaim("usr").asString();
			
			UserInRole userInRole = userInRoleRepository.findBySystemUsername(username);

			Assessment assessment = new Assessment.AssessmentBuilder().userInRole(userInRole)
					.assessmentComment(data.getComment())
					.riskStatus((data.getRiskStatus() != null) ? data.getRiskStatus().charValue() : null)
					.dataValidity(data.getDataValidity().toChar()).build();

			assessment = assessmentRepository.saveAndFlush(assessment);

			for (Long audienceId : data.getAudienceIds())
				assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder()
						.assessmentId(assessment.getId().intValue()).userInRoleId(audienceId.intValue()).build());


			audienceRolesRepository.save(assessmentAudienceRoles);
			
			String jsonString = "";
			if (data.getType() == null) {
				for (Long gefId : data.getGeriatricFactorValueIds()) {

					ViewGefCalculatedInterpolatedPredictedValuesKey key = new ViewGefCalculatedInterpolatedPredictedValuesKey();
					key.setId(gefId);
					key.setDataType("c");
					
					List<ViewGefCalculatedInterpolatedPredictedValues> gefs = viewGefCalculatedInterpolatedPredictedValuesRepository.findByKey(key);

					AssessmentJson assJson;
					
					if(gefs.size() > 5) {
						assJson = new AssessmentJson(assessment, gefs.subList(gefs.size()-5, gefs.size()));
					} else {
						assJson = new AssessmentJson(assessment, gefs);
					}
					
					jsonString = objectMapper.writeValueAsString(assJson);
					assessedGefValueSets.add(new AssessedGefValueSet.AssessedGefValueSetBuilder()
							.assessmentId(assessment.getId().intValue()).gefValueId(gefId.intValue()).jsonString(jsonString).build());
				}
				assessedGefValuesRepository.save(assessedGefValueSets);
				
			} else {
				
				for (Long vmvId : data.getGeriatricFactorValueIds())
					vmvFilteringList.add(new VmvFiltering(variationMeasureValueRepository.findOne(vmvId), "", new Date(), assessment));
				vmvFilteringRepository.save(vmvFilteringList);
				
			}
			
			return JerseyResponse.build(objectMapper.writeValueAsString(assessment));
		} catch (JWTDecodeException exception){
			exception.printStackTrace();
			return JerseyResponse.build("402");
		
		}
	
	}

	@GET
	@ApiOperation("Delete assessment for assessment id.")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("deleteAssessment/{assessmentId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "assessmentId", value = "id of assessment", required = false, dataType = "long", paramType = "path", defaultValue = "506")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response deleteAssessment(@ApiParam(hidden = true) @PathParam("assessmentId") Long assessmentId) throws JsonProcessingException {

		Assessment assessment = assessmentRepository.findOne(assessmentId);
		List<AssessmentAudienceRole> aar = audienceRolesRepository.findByAssessmentId(assessmentId.intValue());
		List<AssessedGefValueSet> agvs = assessedGefValuesRepository.findByAssessmentId(assessmentId.intValue());

		audienceRolesRepository.deleteInBatch(aar);
		audienceRolesRepository.flush();
		assessedGefValuesRepository.deleteInBatch(agvs);
		assessedGefValuesRepository.flush();

		assessmentRepository.delete(assessment);

		return JerseyResponse.build("Deleted");
	}
	
	@POST
	@Path("addAssessmentForClusteredMeasures")	
	public Response addClusterAssessments(@HeaderParam("Authorization") String jwt, String json) throws JsonProcessingException, IOException {
		
		DecodedJWT token;
		Assessment assessment;
		try {
						
			token = JWT.decode(jwt);
			String username = token.getClaim("usr").asString();
			UserInRole userInRole = userInRoleRepository.findBySystemUsername(username);
			
			AddAssessmentClusterDeserializer data = objectMapper.readerFor(AddAssessmentClusterDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
			
			logger.info("data: " + objectMapper.writeValueAsString(data));
			
			assessment = new Assessment.AssessmentBuilder().userInRole(userInRole)
					.assessmentComment(data.getComment())
					.riskStatus(null)
					.dataValidity(null).build();
			
			assessment = assessmentRepository.save(assessment);
			
			for (Long id: data.getDataIDs()) {
				vmvFilteringRepository.save(new VmvFiltering(variationMeasureValueRepository.findOne(id), data.getFilterType().toString(), new Date (), assessment));
			
			}
		} catch (JWTDecodeException exception){
			exception.printStackTrace();
			return JerseyResponse.build("402");
		}
		logger.info("body: " + json);
		
		return JerseyResponse.build(objectMapper.writeValueAsString(assessment));
	}
	
	@GET
	@Path("clusterAssessments/dataPointsIds/{dataPointsIds : .+}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response clusterAssessments (@PathParam(value = "dataPointsIds") List<PathSegment> dataPointsIds,
			@QueryParam(value = "roleId") Long roleId,
			@QueryParam(value = "orderById") Long orderById,
			@QueryParam(value = "riskStatusWarning") Boolean riskStatusWarning,
			@QueryParam(value = "riskStatusAlert") Boolean riskStatusAlert,
			@QueryParam(value = "riskStatusNoRisk") Boolean riskStatusNoRisk,
			@QueryParam(value = "dataValidityQuestionable") Boolean dataValidityQuestionable,
			@QueryParam(value = "dataValidityFaulty") Boolean dataValidityFaulty,
			@QueryParam(value = "dataValidityValid") Boolean dataValidityValid) throws JsonProcessingException, IOException {		
		
		List<Assessment> assessmentList;
		
		for (PathSegment segment: dataPointsIds) {
			logger.info(segment.toString());
		}

		Map<String, Object> inQueryParams = new HashMap<String, Object>();
		List<Long> dataPointsLongIds = assessmentService.convertToListLong(dataPointsIds);
		inQueryParams.put("dataPointsIds", dataPointsLongIds);
		
		List<Filter> filters = new ArrayList<Filter>();
		
		assessmentList = assessmentRepository.doQueryWithFilter(filters, "findClusterForSelectedDataSet", inQueryParams);
		
		String typicalPeriod = null;
		ZoneId timeZone = null;
		DateTimeFormatter dtf = null;
		
		List<ClusteredMeasuresAssessments> cmsAssessments = new ArrayList <ClusteredMeasuresAssessments> ();
		UserInRole uir = null;
		DetectionVariable measureType = null;
		
		if (dataPointsLongIds.size() > 0) {
			VariationMeasureValue vmv = variationMeasureValueRepository.findOne(dataPointsLongIds.get(0));
			uir = vmv.getUserInRole();
			measureType = vmv.getDetectionVariable();
		}
		
		for (Assessment a : assessmentList) {
			//logger.info(a);
			Character filterType = vmvFilteringRepository.findFilterTypeByAssessmentId(a);
			List<Long> vmvIDs = vmvFilteringRepository.findVmvIDsByAssessment (a, uir, measureType);
			List<VariationMeasureValue> vmvList = variationMeasureValueRepository.findAll(vmvIDs);
			List<ClusteredVmv> assessmentMeasures = new ArrayList<ClusteredVmv> ();
			FilterType filter = filterTypeRepository.findOne(filterType);
			
			List<String> dataIDs = new ArrayList <String> ();
			for (Long vmvID : vmvIDs)
				dataIDs.add(vmvID.toString());
			
			BigDecimal minValue = null;
			BigDecimal maxValue = null;
			OffsetDateTime intervalStart = null;
			OffsetDateTime intervalEnd = null;			
			BigDecimal mean = new BigDecimal (0);
			
			for (VariationMeasureValue vmv : vmvList) {
				logger.info("value: " + vmv.getMeasureValue() + " interval: " + vmv.getTimeInterval().getStart());
				
				if (typicalPeriod == null) {
					typicalPeriod = vmv.getTimeInterval().getTypicalPeriod();
					if (typicalPeriod == null) typicalPeriod = "day";
					if (a.getUserInRole().getPilot() != null)
						timeZone = ZoneId.of(a.getUserInRole().getPilot().getCompZone());
					else 
						timeZone = ZoneId.systemDefault();
					if (typicalPeriod.equalsIgnoreCase("mon")) 
						dtf = DateTimeFormatter.ofPattern("yyyy MMMM", Locale.ENGLISH);
					else 
						dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				}
				BigDecimal value = vmv.getMeasureValue();
				OffsetDateTime start =  OffsetDateTime.ofInstant(vmv.getTimeInterval().getIntervalStart().toInstant(), timeZone);		
				mean = mean.add(value);
				
				ClusteredVmv clusteredVmv = new ClusteredVmv (value, start.format(dtf), filter.getImagePath(), filter.getFilterDescription());
				assessmentMeasures.add(clusteredVmv);
				
				if (minValue == null) {
					minValue = value;
					maxValue = value;
					intervalStart = OffsetDateTime.ofInstant(vmv.getTimeInterval().getIntervalStart().toInstant(), timeZone);
					intervalEnd = OffsetDateTime.ofInstant(vmv.getTimeInterval().getIntervalStart().toInstant(), timeZone);
				}
				else {
					if (value.compareTo(minValue) < 0)
						minValue = value;
					if (value.compareTo(maxValue) > 0)
						maxValue = value;
					if (start.isBefore(intervalStart))
						intervalStart = start;
					if (start.isAfter(intervalEnd))
						intervalEnd = start;
				}
			}
			
			OffsetDateTime created = OffsetDateTime.ofInstant(a.getCreated().toInstant(), timeZone);
			OffsetDateTime updated = null;
			if (a.getUpdated() != null) 
				updated = OffsetDateTime.ofInstant(a.getUpdated().toInstant(), timeZone);
			
			String minMax = minValue.setScale(2).toString() + " - " + maxValue.setScale(2).toString();
			mean = mean.divide(new BigDecimal (vmvList.size()), RoundingMode.HALF_UP);
			
			cmsAssessments.add(new ClusteredMeasuresAssessments (a.getId(), filter, a.getAssessmentComment(), 
					updated != null ? updated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZZ")) : 
						created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZZ")),  a.getUserInRole().getUserInSystem().getDisplayName(), 
						dataIDs, mean.setScale(10), minMax, intervalStart.format(dtf) + " - " +
					intervalEnd.format(dtf), assessmentMeasures));
		}		
		
		return JerseyResponse.build(objectMapper.writeValueAsString(cmsAssessments));
		
	}
	
	@POST
	@Path("deleteAssessmentForClusteredMeasures")	
	public Response deleteClusterAssessments (@HeaderParam("Authorization") String jwt, String json) {
		
		DecodedJWT token;
		
		try {
			token = JWT.decode(jwt);
			
			String username = token.getClaim("usr").asString();
			UserInRole userInRole = userInRoleRepository.findBySystemUsername(username);
			
			UpdateOrDeleteAssessmentDeserializer data = objectMapper.readerFor(UpdateOrDeleteAssessmentDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
			
			if (data.getUpdateOrDelete().equals('U')) {
				
				Assessment assessment = assessmentRepository.findOne(data.getAssessmentId());
				assessment.setAssessmentComment(data.getComment());
				assessment.setUpdated(new Date ());
				assessment.setUserInRole(userInRole);
				
				Set<VmvFiltering> vmvFilterings = assessment.getVmvFiltering();
				List<VmvFiltering> newVmvFilterings = new ArrayList <VmvFiltering> ();
				
				/*
				 *  varijanta sa validTo
				 */
				/*for (VmvFiltering vmvFilter : vmvFilterings) {
					vmvFilter.setValidTo(new Date ());
					
					VmvFiltering newVmvFilter = new VmvFiltering(vmvFilter.getVmv(), data.getFilterType().toString(), new Date (), assessment);
					newVmvFilterings.add(newVmvFilter);
				} */
				
				/*
				 * varijanta samo sa promenom filtera
				 */
				
				for (VmvFiltering vmvFilter : vmvFilterings) {
					vmvFilter.setValidFrom(new Date ());
					vmvFilter.setFilterType(data.getFilterType().toString());
				}
				
				assessmentRepository.save(assessment);				
				vmvFilteringRepository.save(vmvFilterings);
				/* ovo je samo za prvu varijantu */ vmvFilteringRepository.save(newVmvFilterings);
				
			}
			else if (data.getUpdateOrDelete().equals('D')) {
				
				Assessment assessment = assessmentRepository.findOne(data.getAssessmentId());
				Set<VmvFiltering> vmvFilterings = assessment.getVmvFiltering();
				
				/*
				 * varijanta bez brisanja iz baze, sa postavljanjem flaga
				 */
				/*for (VmvFiltering vmvFilter : vmvFilterings) {
					vmvFilter.setValidTo(new Date ());
				}
				
				vmvFilteringRepository.save(vmvFilterings);
				
				assessment.setFlag ("D");
				
				assessmentRepository.save(assessment);*/
				
				/*
				 * varijanta sa brisanjem iz baze
				 */
				
				vmvFilteringRepository.delete(vmvFilterings);
				assessmentRepository.delete(assessment);
			}
			else throw new Exception ("Not update nor delete operation");
			
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			return JerseyResponse.build("402");
		} 
		  catch (Exception e) {
			e.printStackTrace();
			return JerseyResponse.build("400");
		} 
		return null;
		
	}
	
	@GET
	@Path("undoAssessment/{assessmentId}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response clusterAssessments (@PathParam(value = "assessmentId") Long assessmentId) throws JsonProcessingException, IOException {		
		
		//List<Long> dataPoints = assessmentService.convertToListLong(dataPointsIds);
		
		vmvFilteringRepository.delete(vmvFilteringRepository.findByAssessment (assessmentRepository.findOne(assessmentId)));		
		assessmentRepository.delete(assessmentRepository.findOne(assessmentId));		
		
		vmvFilteringRepository.flush();
		assessmentRepository.flush();
		
		return JerseyResponse.build();
		
	}

}
