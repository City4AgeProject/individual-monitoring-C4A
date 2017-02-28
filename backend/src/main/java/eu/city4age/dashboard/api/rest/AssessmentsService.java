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
import eu.city4age.dashboard.api.pojo.persist.Filter;

@Transactional("transactionManager")
@Path(AssessmentsService.PATH)
public class AssessmentsService {

	public static final String PATH = "assessment";

	static protected Logger logger = LogManager.getLogger(AssessmentsService.class);

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

		/*
		 * Avoiding to use readerFor method instead because of conflict with
		 * older version of jackson jars in GLASSFISH_HOME/glassfish/modules of
		 * Glassfish 4.1.1 which then would have to be replaced.
		 */
		@SuppressWarnings("deprecation")
		GetDiagramDataDeserializer data = objectMapper.reader(GetDiagramDataDeserializer.class).readValue(json);

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
			@QueryParam("authorRoleId") Long authorRoleId, @QueryParam("orderById") Long orderById,
			@QueryParam("riskStatusWarning") Boolean riskStatusWarning,
			@QueryParam("riskStatusAlert") Boolean riskStatusAlert,
			@QueryParam("riskStatusNoRisk") Boolean riskStatusNoRisk,
			@QueryParam("dataValidityQuestionable") Boolean dataValidityQuestionable,
			@QueryParam("dataValidityFaulty") Boolean dataValidityFaulty,
			@QueryParam("dataValidityValid") Boolean dataValidityValid) throws JsonProcessingException {

		List<Assessment> aaList;

		Map<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", convertToListLong(geriatricFactorValueIds));

		List<Filter> filters = new ArrayList<Filter>();

		if (riskStatusWarning != null || riskStatusAlert != null || riskStatusNoRisk != null) {
			Filter riskStatus = new Filter();
			riskStatus.setName("riskStatus");
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
			riskStatus.getInParams().put("riskStatus", inParams);
			filters.add(riskStatus);
		}

		if (dataValidityFaulty != null || dataValidityQuestionable != null || dataValidityValid != null) {
			Filter dataValidity = new Filter();
			dataValidity.setName("dataValidity");
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
			dataValidity.getInParams().put("dataValidity", inParams);
			filters.add(dataValidity);

		}

		if (authorRoleId != null) {
			Filter userInRoleId = new Filter();
			userInRoleId.setName("userInRoleId");
			userInRoleId.getInParams().put("userInRoleId", authorRoleId);
			filters.add(userInRoleId);
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
			list.sort(Comparator.comparing(Assessment::getUserInRoleId));
			break;
		case 6:
			list.sort(Comparator.comparing(Assessment::getUserInRoleId).reversed());
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

	@POST
	@Path("addForSelectedDataSet")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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

		ojLfa.getSeries().add(new Serie("Alert", new HashSet<DataIdValueLastFiveAssessment>(), "",
				"20px", 32, "on", "none"));
		ojLfa.getSeries().add(new Serie("Warning", new HashSet<DataIdValueLastFiveAssessment>(),
				"", "20px", 32, "on", "none"));
		ojLfa.getSeries().add(new Serie("Comment", new HashSet<DataIdValueLastFiveAssessment>(), "",
				"20px", 32, "on", "none"));

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