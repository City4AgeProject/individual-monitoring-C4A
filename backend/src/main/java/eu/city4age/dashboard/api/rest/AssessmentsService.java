package eu.city4age.dashboard.api.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.AssessmentRepository;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.TimeIntervalRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.DiagramData;
import eu.city4age.dashboard.api.pojo.dto.LastFiveAssessment;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramLastFiveAssessment;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValueLastFiveAssessment;
import eu.city4age.dashboard.api.pojo.dto.oj.Serie;
import eu.city4age.dashboard.api.pojo.json.AddAssessmentDeserializer;
import eu.city4age.dashboard.api.pojo.json.GetDiagramDataDeserializer;
import eu.city4age.dashboard.api.pojo.json.view.View;

@Transactional("transactionManager")
@Path(AssessmentsService.PATH)
public class AssessmentsService {

	public static final String PATH = "assessment";

	static protected Logger logger = Logger.getLogger(AssessmentsService.class);

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private GenericRepository<AssessmentAudienceRole, Long> audienceRolesRepository;

	@Autowired
	private GenericRepository<AssessedGefValueSet, Long> assessedGefValuesRepository;

	@Context
	private ContextResolver<ObjectMapper> mapperResolver;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@POST
	@Path("getDiagramData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiagramData(String json) throws JsonProcessingException, IOException {

		ObjectMapper objectMapper = mapperResolver.getContext(TimeInterval.class);

		GetDiagramDataDeserializer data = objectMapper.readerFor(GetDiagramDataDeserializer.class).readValue(json);

		Timestamp start = Timestamp.valueOf(data.getTimestampStart());
		Timestamp end = Timestamp.valueOf(data.getTimestampEnd());

		DiagramData dto = new DiagramData();

		List<TimeInterval> months = timeIntervalRepository.findByPeriod(start, end);

		List<String> monthLabels = createMonthLabels(months);

		dto.setMonthLabels(monthLabels);

		List<String> gefLables = detectionVariableRepository.findNameByParentId(data.getDvParentId());

		dto.setGefLabels(gefLables);

		List<Object[]> tis = timeIntervalRepository.findByUserInRoleId(Long.valueOf(data.getCrId()),
				Long.valueOf(data.getDvParentId()), start, end);

		dto.setTis(tis);

		return Response.ok(objectMapper.writeValueAsString(dto)).build();
	}

	@GET
	@Path("getLastFiveForDiagram/userInRoleId/{userInRoleId}/intervalStart/{intervalStart}/intervalEnd/{intervalEnd}")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.AssessmentView.class)
	public Response getLastFiveForDiagram(@PathParam("userInRoleId") Long userInRoleId,
			@PathParam("intervalStart") String intervalStart, @PathParam("intervalEnd") String intervalEnd)
			throws JsonProcessingException {

		Timestamp intervalStartTimestamp = Timestamp.valueOf(intervalStart.concat(" 00:00:00"));
		Timestamp intervalEndTimestamp = Timestamp.valueOf(intervalEnd.concat(" 00:00:00"));

		List<LastFiveAssessment> lfa = timeIntervalRepository.findLastFiveForDiagram(userInRoleId,
				intervalStartTimestamp, intervalEndTimestamp);

		OJDiagramLastFiveAssessment ojLfa = transformToOJ(lfa);

		return Response.ok(objectMapper.writeValueAsString(ojLfa)).build();
	}

	@GET
	@Path("findForSelectedDataSet/geriatricFactorValueIds/{geriatricFactorValueIds : .+}")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView(View.AssessmentView.class)
	public Response findForSelectedDataSet(
			@PathParam("geriatricFactorValueIds") List<PathSegment> geriatricFactorValueIds,
			@QueryParam("authorRoleId") Long authorRoleId, @QueryParam("riskStatusWarning") Boolean riskStatusWarning,
			@QueryParam("riskStatusAlert") Boolean riskStatusAlert,
			@QueryParam("dataValidityQuestionable") Boolean dataValidityQuestionable,
			@QueryParam("dataValidityFaulty") Boolean dataValidityFaulty,
			@QueryParam("assessmentComment") Boolean assessmentComment, @QueryParam("orderBy") String orderBy)
			throws JsonProcessingException {

		Map<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", convertToListLong(geriatricFactorValueIds));

		Map<String, Object> inFilterParams = new HashMap<String, Object>();
		inFilterParams.put("orderByDateAsc", false);
		inFilterParams.put("orderByDateDesc", false);
		inFilterParams.put("orderByAuthorNameAsc", false);
		inFilterParams.put("orderByAuthorNameDesc", false);
		inFilterParams.put("orderByAuthorRoleAsc", false);
		inFilterParams.put("orderByAuthorRoleDesc", false);
		inFilterParams.put("riskStatusWarning", false);
		inFilterParams.put("riskStatusAlert", false);
		inFilterParams.put("dataValidityQuestionable", false);
		inFilterParams.put("dataValidityFaulty", false);
		inFilterParams.put("assessmentComment", false);

		if (authorRoleId != null)
			inFilterParams.put("userInRoleId", authorRoleId);

		if (riskStatusWarning != null)
			inFilterParams.put("riskStatusWarning", true);

		if (riskStatusAlert != null)
			inFilterParams.put("riskStatusAlert", true);

		if (dataValidityQuestionable != null)
			inFilterParams.put("dataValidityQuestionable", true);

		if (dataValidityFaulty != null)
			inFilterParams.put("dataValidityFaulty", true);

		if (assessmentComment != null)
			inFilterParams.put("assessmentComment", true);

		if (orderBy != null) {
			switch (orderBy) {
			case "orderByDateAsc":
				inFilterParams.put("orderByDateAsc", true);
				break;
			case "orderByDateDesc":
				inFilterParams.put("orderByDateDesc", true);
				break;
			case "orderByAuthorNameAsc":
				inFilterParams.put("orderByAuthorNameAsc", true);
				break;
			case "orderByAuthorNameDesc":
				inFilterParams.put("orderByAuthorNameDesc", true);
				break;
			case "orderByAuthorRoleAsc":
				inFilterParams.put("orderByAuthorRoleAsc", true);
				break;
			case "orderByAuthorRoleDesc":
				inFilterParams.put("orderByAuthorRoleDesc", true);
				break;
			}
		}

		List<Assessment> aaList = assessmentRepository.doQueryWithFilter("filterByAll", "findForSelectedDataSet",
				inFilterParams, inQueryParams);

		return Response.ok(objectMapper.writerWithView(View.AssessmentView.class)
				.writeValueAsString(new HashSet<Assessment>(aaList))).build();
	}

	private List<Long> convertToListLong(List<PathSegment> geriatricFactorValueIds) {
		List<Long> gefIds = new ArrayList<Long>(0);
		for (PathSegment segment : geriatricFactorValueIds) {
			gefIds.add(Long.valueOf(segment.toString()));
		}
		return gefIds;
	}

	@POST
	@Path("addForSelectedDataSet")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addForSelectedDataSet(String json) throws JsonProcessingException, IOException {
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();

		AddAssessmentDeserializer data = objectMapper.readerFor(AddAssessmentDeserializer.class)
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

	}

	private List<String> createMonthLabels(List<TimeInterval> months) {
		List<String> monthLabels = new ArrayList<String>();

		for (int i = 0; i < months.size(); i++)
			monthLabels.add(months.get(i).getStart());

		return monthLabels;
	}

	private OJDiagramLastFiveAssessment transformToOJ(List<LastFiveAssessment> lfas) {

		OJDiagramLastFiveAssessment ojLfa = new OJDiagramLastFiveAssessment();

		for (LastFiveAssessment lfa : lfas) {
			ojLfa.getGroups().add(new DataIdValue(lfa.getTimeIntervalId(), lfa.getIntervalStart()));
		}

		ojLfa.getSeries().add(new Serie("Alert", new HashSet<DataIdValueLastFiveAssessment>(), "images/flag-red.png",
				"20px", 16, "on", "none"));
		ojLfa.getSeries().add(new Serie("Warning", new HashSet<DataIdValueLastFiveAssessment>(),
				"images/flag-beige.png", "20px", 16, "on", "none"));
		ojLfa.getSeries().add(new Serie("Comment", new HashSet<DataIdValueLastFiveAssessment>(), "images/flag-gray.png",
				"20px", 16, "on", "none"));

		for (DataIdValue group : ojLfa.getGroups()) {

			for (int i = 0; i < lfas.size(); i++) {

				if (lfas.get(i).getTimeIntervalId().equals(group.getId())) {

					for (Serie serie : ojLfa.getSeries()) {
						if (lfas.get(i).getId() != null) {
							DataIdValueLastFiveAssessment item = new DataIdValueLastFiveAssessment();
							item.setId(lfas.get(i).getGefId());
							item.setValue(lfas.get(i).getGefValue().toString());
							item.getAssessmentObjects().add(lfas.get(i));

							for (LastFiveAssessment lfa : lfas) {
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