package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.ext.ContextResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.persist.AssessmentRepository;
import eu.city4age.dashboard.api.persist.AudienceRolesRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.Last5Assessment;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramLast5Assessment;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValueLastFiveAssessment;
import eu.city4age.dashboard.api.pojo.dto.oj.Serie;
import eu.city4age.dashboard.api.pojo.json.AddAssessmentDeserializer;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.pojo.persist.Filter;
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
@Transactional("transactionManager")
@Path(AssessmentsService.PATH)
@Api(value = "assessment", produces = "application/json")
public class AssessmentsService {

	public static final String PATH = "assessment";

	static protected Logger logger = LogManager.getLogger(AssessmentsService.class);

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private AudienceRolesRepository audienceRolesRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;

	@Context
	private ContextResolver<ObjectMapper> mapperResolver;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();


	@GET
	@ApiOperation("Get last five assessments for data sets in specific time interval.")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.AssessmentView.class)
	@Path("getLastFiveForDiagram/userInRoleId/{userInRoleId}/parentDetectionVariableId/{parentDetectionVariableId}/intervalStart/{intervalStart}/intervalEnd/{intervalEnd}")
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

		List<Last5Assessment> l5a = new ArrayList<Last5Assessment>();

		try {
			l5a = timeIntervalRepository.getLastFiveForDiagram(userInRoleId, parentDetectionVariableId,
					intervalStartTimestamp, intervalEndTimestamp);
		} catch (Exception e) {
			logger.info("getLastFiveForDiagram REST service - query exception: ", e);
		}

		OJDiagramLast5Assessment ojLfa = transformToOJ(l5a);

		return Response.ok(objectMapper.writeValueAsString(ojLfa)).build();
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
		inQueryParams.put("geriatricFactorIds", convertToListLong(geriatricFactorValueIds));

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

		List<Assessment> aa = new ArrayList<Assessment>();

		if (orderById != null) {
			aa = orderByForFiltering(aaList, orderById);
		} else {
			aa = aaList;
		}

		return Response.ok(objectMapper.writerWithView(View.AssessmentView.class).writeValueAsString(aa)).build();

	}

	private List<Assessment> orderByForFiltering(List<Assessment> list, Long orderById) {

		switch (orderById.intValue()) {
		case 1:
			list.sort(Comparator.comparing(Assessment::getCreated));
			break;
		case 2:
			list.sort(Comparator.comparing(Assessment::getCreated).reversed());
			break;
		case 3:
			list.sort(Comparator.comparing(Assessment::getUserInSystemDisplayName));
			break;
		case 4:
			list.sort(Comparator.comparing(Assessment::getUserInSystemDisplayName).reversed());
			break;
		case 5:
			list.sort(Comparator.comparing(Assessment::getRoleId));
			break;
		case 6:
			list.sort(Comparator.comparing(Assessment::getRoleId).reversed());
			break;
		case 7:
			break;
		}

		return list;
	}

	private List<Long> convertToListLong(List<PathSegment> geriatricFactorValueIds) {
		List<Long> gefIds = new ArrayList<Long>(geriatricFactorValueIds.size());
		for (PathSegment segment : geriatricFactorValueIds) {
			gefIds.add(Long.valueOf(segment.toString()));
		}
		return gefIds;
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addForSelectedDataSet")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response addForSelectedDataSet(String json) throws JsonProcessingException, IOException {
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();

		/*
		 * Avoiding to use readerFor method instead because of conflict with
		 * older version of jackson jars in GLASSFISH_HOME/glassfish/modules of
		 * Glassfish 4.1.1 which then would have to be replaced.
		 */
		@SuppressWarnings("deprecation")
		AddAssessmentDeserializer data = objectMapper.reader(AddAssessmentDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);

		UserInRole userInRole = userInRoleRepository.findOne(data.getAuthorId());

		Assessment assessment = new Assessment.AssessmentBuilder().userInRole(userInRole)
				.assessmentComment(data.getComment())
				.riskStatus((data.getRiskStatus() != null) ? data.getRiskStatus().charValue() : null)
				.dataValidity(data.getDataValidity().toChar()).build();

		assessmentRepository.saveAndFlush(assessment);

		for (Long audienceId : data.getAudienceIds())
			assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder()
					.assessmentId(assessment.getId().intValue()).userInRoleId(audienceId.intValue()).build());

		for (Long gefId : data.getGeriatricFactorValueIds())
			assessedGefValueSets.add(new AssessedGefValueSet.AssessedGefValueSetBuilder()
					.assessmentId(assessment.getId().intValue()).gefValueId(gefId.intValue()).build());

		audienceRolesRepository.save(assessmentAudienceRoles);
		assessedGefValuesRepository.save(assessedGefValueSets);

		return Response.ok(objectMapper.writeValueAsString(assessment)).build();
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
		return Response.ok("Deleted").build();
	}

	private OJDiagramLast5Assessment transformToOJ(List<Last5Assessment> lfas) {

		OJDiagramLast5Assessment ojLfa = new OJDiagramLast5Assessment();

		for (Last5Assessment lfa : lfas) {
			ojLfa.getGroups().add(new DataIdValue(lfa.getTimeIntervalId(), lfa.getIntervalStart()));
		}

		ojLfa.getSeries()
				.add(new Serie("Alert", new HashSet<DataIdValueLastFiveAssessment>(), "", "20px", 32, "on", "none"));
		ojLfa.getSeries()
				.add(new Serie("Warning", new HashSet<DataIdValueLastFiveAssessment>(), "", "20px", 32, "on", "none"));
		ojLfa.getSeries()
				.add(new Serie("Comment", new HashSet<DataIdValueLastFiveAssessment>(), "", "20px", 32, "on", "none"));

		for (DataIdValue group : ojLfa.getGroups()) {

			for (int i = 0; i < lfas.size(); i++) {

				if (lfas.get(i).getTimeIntervalId().equals(group.getId())) {

					for (Serie serie : ojLfa.getSeries()) {
						if (lfas.get(i).getId() != null) {
							DataIdValueLastFiveAssessment item = new DataIdValueLastFiveAssessment();
							item.setId(lfas.get(i).getGefId());
							item.setValue(lfas.get(i).getGefValue().toString());
							item.getAssessmentObjects().add(lfas.get(i));

							for (Last5Assessment lfa : lfas) {
								if (lfa.getId() != null && lfa.getTimeIntervalId().equals(group.getId())
										&& lfas.get(i).getGefValue().equals(lfa.getGefValue())
										&& !lfas.get(i).getId().equals(lfa.getId())) {

									item.getAssessmentObjects().add(lfa);
								}
							}

							serie.getItems().add(item);
						}
					}

				}

			}
		}

		return ojLfa;

	}

}
