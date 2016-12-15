package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.dao.DetectionVariableDAO;
import eu.city4age.dashboard.api.dao.TimeIntervalDAO;
import eu.city4age.dashboard.api.domain.DataIdValue;
import eu.city4age.dashboard.api.domain.Serie;
import eu.city4age.dashboard.api.dto.DiagramDataDTO;
import eu.city4age.dashboard.api.dto.OJDiagramDTO;
import eu.city4age.dashboard.api.json.AddAssessmentWrapper;
import eu.city4age.dashboard.api.json.GetAssessmentsByFilterWrapper;
import eu.city4age.dashboard.api.json.GetDiagramDataWrapper;
import eu.city4age.dashboard.api.json.GetLastFiveAssessmentsWrapper;
import eu.city4age.dashboard.api.model.AssessedGefValueSet;
import eu.city4age.dashboard.api.model.AssessedGefValueSetId;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.AssessmentAudienceRole;
import eu.city4age.dashboard.api.model.AssessmentAudienceRoleId;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;
import eu.city4age.dashboard.api.model.UserInRole;

@Transactional("transactionManager")
@Path("/assessments")
public class AssessmentsService {

    static protected Logger logger = Logger.getLogger(AssessmentsService.class);

    @Autowired
    private AssessmentDAO assessmentDAO;

	@Autowired
	private DetectionVariableDAO detectionVariableDAO;
	
	@Autowired
	private TimeIntervalDAO timeIntervalDAO;
	

	@POST
    @Path("/getDiagramData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getDiagramData(String json) throws Exception {
    	
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate3Module());
		
    	ObjectReader objectReader = objectMapper.reader(GetDiagramDataWrapper.class);
    	
    	GetDiagramDataWrapper data = objectReader.readValue(json);

		Timestamp start = Timestamp.valueOf(data.getTimestampStart());
		Timestamp end = Timestamp.valueOf(data.getTimestampEnd());
    	
    	DiagramDataDTO dto = new DiagramDataDTO();
    	
    	List<Object[]> months = timeIntervalDAO.getTimeIntervalsForPeriod(start, end);
    	
    	List<String> monthLabels = createMonthLabels(months);
    	
    	dto.setMonthLabels(monthLabels);
    	
    	List<String> gefLables = detectionVariableDAO.getAllDetectionVariableNamesForParentId(data.getDvParentId());
    	
		dto.setGefLabels(gefLables);
    	
    	List<TimeInterval> tis = assessmentDAO.getDiagramDataForUserInRoleId(data.getCrId(), data.getDvParentId(), start, end);
    	
    	dto.setTis(tis);
		
		String dtoAsString = objectMapper.writeValueAsString(dto);
        
        return dtoAsString;		
	}
 
    @POST
    @Path("/getLastFiveAssessmentsForDiagram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getLastFiveAssessmentsForDiagram(String json) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate3Module());
		
    	ObjectReader objectReader = objectMapper.reader(GetLastFiveAssessmentsWrapper.class);
    	
    	GetLastFiveAssessmentsWrapper data = objectReader.readValue(json);
    	
		Timestamp start = Timestamp.valueOf(data.getTimestampStart());
		Timestamp end = Timestamp.valueOf(data.getTimestampEnd());
		
		List<TimeInterval> tis = assessmentDAO.
						getLastFiveAssessmentsForDiagram(data.getCrId(), start, end);
		
		OJDiagramDTO dto = transformToDto(tis);
    	
		return objectMapper.writeValueAsString(dto);

    }

	@POST
    @Path("/getAssessmentsForSelectedDataSet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssessmentsForSelectedDataSet(String json) throws Exception {

    	
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate3Module());
    	
    	ObjectReader objectReader = objectMapper.reader(GetAssessmentsByFilterWrapper.class);
    	
    	GetAssessmentsByFilterWrapper data = objectReader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);

    	List<GeriatricFactorValue> gefs = assessmentDAO.getAssessmentsForSelectedDataSet(
    						data.getGeriatricFactorValueIds(), data.getStatus(), data.getAuthorRoleId(), data.getOrderBy());

		String dtoAsString = objectMapper.writeValueAsString(gefs);
        
        return dtoAsString;
    }

    @POST
    @Path("/addAssessmentsForSelectedDataSet")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addAssessmentsForSelectedDataSet(String json) throws Exception {
    	
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new Hibernate3Module());
    	
    	ObjectReader objectReader = objectMapper.reader(AddAssessmentWrapper.class);
    	
    	AddAssessmentWrapper data = objectReader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
 
    	Assessment assessment = new Assessment();

		assessment.setUserInRole(new UserInRole());
		
		assessment.getUserInRole().setId(data.getAuthorId());
    	
		assessment.setAssessmentComment(data.getComment());
		
		if(data.getRiskStatus() != null)
			assessment.setRiskStatus(data.getRiskStatus().charValue());
		
		assessment.setDataValidityStatus(data.getDataValidityStatus().toChar());
		
		assessment.setCreated(new Date());
		

		assessmentDAO.insertOrUpdate(assessment);
		assessmentDAO.flush();
		
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
		
		for (int i = 0; i < data.getAudienceIds().size(); i++) {
			
			AssessmentAudienceRole assessmentAudienceRole = new AssessmentAudienceRole();	
			
			assessmentAudienceRole.setAssigned(new Date());
			
			assessmentAudienceRole.setAssessmentAudienceRoleId(new AssessmentAudienceRoleId(assessment.getId().intValue(), data.getAudienceIds().get(i).intValue()));
			
			assessmentAudienceRoles.add(assessmentAudienceRole);
		
		}
		
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();
		
		for (int i=0; i < data.getGeriatricFactorValueIds().size(); i++) {
			
			AssessedGefValueSet assessedGefValueSet = new AssessedGefValueSet();
			
			assessedGefValueSet.setAssessedGefValueSetId(new AssessedGefValueSetId(data.getGeriatricFactorValueIds().get(i).intValue(), assessment.getId().intValue()));
			
			assessedGefValueSets.add(assessedGefValueSet);
			
		}
		
		assessmentDAO.insertOrUpdateAll(assessedGefValueSets);
		
		assessmentDAO.insertOrUpdateAll(assessmentAudienceRoles);
	 
    }

	
	private List<String> createMonthLabels(List<Object[]> months) {
		List<String> monthLabels = new ArrayList<String>();
    	
    	for (int i = 0; i < months.size() ; i++) {
    		monthLabels.add(((Timestamp)months.get(i)[0]).toString());
    	}

		return monthLabels;
	}
	
    private OJDiagramDTO transformToDto(List<TimeInterval> tis) {
		OJDiagramDTO dto = new OJDiagramDTO();
		for(TimeInterval ti: tis) {
			dto.getGroups().add(new DataIdValue(ti.getId(), ti.getStart()));
		}
	    dto.setSerie(new Serie("Assessments","#e83d17","images/flag.png",new ArrayList<DataIdValue>(),"none","on",20));
		for(int i = 0; i < tis.size(); i++) {
			if(tis.get(i).getGeriatricFactorValues().iterator().hasNext()) {
				GeriatricFactorValue gef = tis.get(i).getGeriatricFactorValues().iterator().next();
					if(gef.getAssessments() != null) {
						DataIdValue item = new DataIdValue(gef.getId(), gef.getGefValue().toString());
						dto.getSerie().getItems().add(i, item);
					} 
			} else {
				dto.getSerie().getItems().add(i, null);
			}	
		}
		dto.setTis(tis);
		return dto;
	}

	public void setAssessmentDAO(AssessmentDAO assessmentDAO) {
		this.assessmentDAO = assessmentDAO;
	}


	public void setDetectionVariableDAO(DetectionVariableDAO detectionVariableDAO) {
		this.detectionVariableDAO = detectionVariableDAO;
	}

	public void setTimeIntervalDAO(TimeIntervalDAO timeIntervalDAO) {
		this.timeIntervalDAO = timeIntervalDAO;
	}
	
}
