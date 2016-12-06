package eu.city4age.dashboard.api.ws;

import java.sql.Timestamp;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.dao.DetectionVariableDAO;
import eu.city4age.dashboard.api.dao.TimeIntervalDAO;
import eu.city4age.dashboard.api.domain.DataPointSet;
import eu.city4age.dashboard.api.dto.DiagramDataDTO;
import eu.city4age.dashboard.api.dto.DiagramDataPoint;
import eu.city4age.dashboard.api.dto.DiagramDataPointSet;
import eu.city4age.dashboard.api.dto.DiagramMonthInterval;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
	
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
	
    @GET
    @Path("/getDiagramDataOld")
    @Produces(MediaType.APPLICATION_JSON)
	public String getDiagramDataOld() throws JsonProcessingException {
    	
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");
    
		List<GeriatricFactorValue> gefs = assessmentDAO.getDiagramDataForUserInRoleId(1, start, end);
		String labelTest = ((GeriatricFactorValue)gefs.get(0)).getCdDetectionVariable().getDetectionVariableName();
    	
        //DataPointSet from domain
        DataPointSet dataPointSet1 = new DataPointSet();
        dataPointSet1.setId(1l);
        dataPointSet1.setLabel(labelTest);
        DataPointSet dataPointSet2 = new DataPointSet();
        dataPointSet2.setId(2l);
        dataPointSet2.setLabel(labelTest);
        DataPointSet dataPointSet3 = new DataPointSet();
        dataPointSet3.setId(3l);
        dataPointSet3.setLabel("Still/Moving");
        DataPointSet dataPointSet4 = new DataPointSet();
        dataPointSet4.setId(4l);
        dataPointSet4.setLabel("Moving across rooms");
        DataPointSet dataPointSet5 = new DataPointSet();
        dataPointSet5.setId(5l);
        dataPointSet5.setLabel("Gait balance");
        DataPointSet dataPointSet6 = new DataPointSet();
        dataPointSet6.setId(6l);
        dataPointSet6.setLabel("Alerts");
        DataPointSet dataPointSet7 = new DataPointSet();
        dataPointSet7.setId(7l);
        dataPointSet7.setLabel("Warnings");
        DataPointSet dataPointSet8 = new DataPointSet();
        dataPointSet8.setId(8l);
        dataPointSet8.setLabel("Comments");
        
        //Diagram DTO data
        DiagramMonthInterval diagramMonthInterval = new DiagramMonthInterval();
        diagramMonthInterval.setStart(24180); 
        diagramMonthInterval.setEnd(24182);         
        
        DiagramDataPointSet diagramDataPointSet1 = new DiagramDataPointSet(dataPointSet1.getId(), dataPointSet1.getLabel());
        diagramDataPointSet1.setId(dataPointSet1.getId());
        diagramDataPointSet1.setLabel(dataPointSet1.getLabel());
        DiagramDataPointSet diagramDataPointSet2 = new DiagramDataPointSet(dataPointSet2.getId(), dataPointSet2.getLabel());
        diagramDataPointSet2.setId(dataPointSet2.getId());
        diagramDataPointSet2.setLabel(dataPointSet2.getLabel());
        DiagramDataPointSet diagramDataPointSet3 = new DiagramDataPointSet(dataPointSet3.getId(), dataPointSet3.getLabel());
        diagramDataPointSet3.setId(dataPointSet3.getId());
        diagramDataPointSet3.setLabel(dataPointSet3.getLabel());
        DiagramDataPointSet diagramDataPointSet4 = new DiagramDataPointSet(dataPointSet4.getId(), dataPointSet4.getLabel());
        diagramDataPointSet4.setId(dataPointSet4.getId());
        diagramDataPointSet4.setLabel(dataPointSet4.getLabel());
        DiagramDataPointSet diagramDataPointSet5 = new DiagramDataPointSet(dataPointSet5.getId(), dataPointSet5.getLabel());
        diagramDataPointSet5.setId(dataPointSet5.getId());
        diagramDataPointSet5.setLabel(dataPointSet5.getLabel());
        DiagramDataPointSet diagramDataPointSet6 = new DiagramDataPointSet(dataPointSet6.getId(), dataPointSet6.getLabel());
        diagramDataPointSet6.setId(dataPointSet6.getId());
        diagramDataPointSet6.setLabel(dataPointSet6.getLabel());
        DiagramDataPointSet diagramDataPointSet7 = new DiagramDataPointSet(dataPointSet7.getId(), dataPointSet7.getLabel());
        diagramDataPointSet7.setId(dataPointSet7.getId());
        diagramDataPointSet7.setLabel(dataPointSet7.getLabel());
        DiagramDataPointSet diagramDataPointSet8 = new DiagramDataPointSet(dataPointSet8.getId(), dataPointSet8.getLabel());
        diagramDataPointSet8.setId(dataPointSet8.getId());
        diagramDataPointSet8.setLabel(dataPointSet8.getLabel());
        
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet1);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet2);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet3);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet4);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet5);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet6);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet7);
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet8);

        ///get list of data points from the model
        ///////.......TODO        
        
        ///E.G. Filling diagram DTO objects
        //First point     
        diagramDataPointSet1.getDiagramDataPoints().add(new DiagramDataPoint(1l, 3.0f, null));
        diagramDataPointSet2.getDiagramDataPoints().add(new DiagramDataPoint(2l, 3.0f, null));
        diagramDataPointSet3.getDiagramDataPoints().add(new DiagramDataPoint(3l, 3.0f, null));
        diagramDataPointSet4.getDiagramDataPoints().add(new DiagramDataPoint(4l, 3.0f, null));
        diagramDataPointSet5.getDiagramDataPoints().add(new DiagramDataPoint(5l, 3.0f, null));
        diagramDataPointSet6.getDiagramDataPoints().add(null);
        diagramDataPointSet7.getDiagramDataPoints().add(null);
        diagramDataPointSet8.getDiagramDataPoints().add(null);

        //Second point
        diagramDataPointSet1.getDiagramDataPoints().add(new DiagramDataPoint(6l, 1.5f, null));
        diagramDataPointSet2.getDiagramDataPoints().add(new DiagramDataPoint(7l, 4.2f, null));
        diagramDataPointSet3.getDiagramDataPoints().add(new DiagramDataPoint(8l, 5.0f, null));
        diagramDataPointSet4.getDiagramDataPoints().add(new DiagramDataPoint(9l, 3.3f, null));
        diagramDataPointSet5.getDiagramDataPoints().add(new DiagramDataPoint(10l, 2.8f, null));
        diagramDataPointSet6.getDiagramDataPoints().add(new DiagramDataPoint(11l, 1.5f, null));
        diagramDataPointSet7.getDiagramDataPoints().add(null);
        diagramDataPointSet8.getDiagramDataPoints().add(null);

        //Third point
        diagramDataPointSet1.getDiagramDataPoints().add(new DiagramDataPoint(12l, 1.0f, null));
        diagramDataPointSet2.getDiagramDataPoints().add(new DiagramDataPoint(13l, 2.8f, null));
        diagramDataPointSet3.getDiagramDataPoints().add(new DiagramDataPoint(14l, 3.7f, null));
        diagramDataPointSet4.getDiagramDataPoints().add(new DiagramDataPoint(15l, 3.8f, null));
        diagramDataPointSet5.getDiagramDataPoints().add(new DiagramDataPoint(16l, 2.8f, null));
        diagramDataPointSet6.getDiagramDataPoints().add(new DiagramDataPoint(17l, 1.0f, null));
        diagramDataPointSet7.getDiagramDataPoints().add(null);
        diagramDataPointSet8.getDiagramDataPoints().add(new DiagramDataPoint(18l, 2.8f, null));
        
        //4th point
        diagramDataPointSet1.getDiagramDataPoints().add(new DiagramDataPoint(19l, 2.2f, null));
        diagramDataPointSet2.getDiagramDataPoints().add(new DiagramDataPoint(20l, 2.2f, null));
        diagramDataPointSet3.getDiagramDataPoints().add(new DiagramDataPoint(21l, 4.6f, null));
        diagramDataPointSet4.getDiagramDataPoints().add(new DiagramDataPoint(22l, 5.0f, null));
        diagramDataPointSet5.getDiagramDataPoints().add(new DiagramDataPoint(23l, 3.2f, null));
        diagramDataPointSet6.getDiagramDataPoints().add(null);
        diagramDataPointSet7.getDiagramDataPoints().add(null);
        diagramDataPointSet8.getDiagramDataPoints().add(null);
        
        //5th point
        diagramDataPointSet1.getDiagramDataPoints().add(new DiagramDataPoint(24l, 1.8f, null));
        diagramDataPointSet2.getDiagramDataPoints().add(new DiagramDataPoint(25l, 3.3f, null));
        diagramDataPointSet3.getDiagramDataPoints().add(new DiagramDataPoint(26l, 4.5f, null));
        diagramDataPointSet4.getDiagramDataPoints().add(new DiagramDataPoint(27l, 4.5f, null));
        diagramDataPointSet5.getDiagramDataPoints().add(new DiagramDataPoint(28l, 2.9f, null));
        diagramDataPointSet6.getDiagramDataPoints().add(null);
        diagramDataPointSet7.getDiagramDataPoints().add(new DiagramDataPoint(29l, 1.8f, null));
        diagramDataPointSet8.getDiagramDataPoints().add(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String dtoAsString = objectMapper.writeValueAsString(diagramMonthInterval);
        
        return dtoAsString;
	}
    
    @GET
    @Path("/getDiagramData")
    @Produces(MediaType.APPLICATION_JSON)
	public String getDiagramData() throws JsonProcessingException {
    	
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");
    	
    	DiagramDataDTO dto = new DiagramDataDTO();
    	
    
    	List<Object[]> months = timeIntervalDAO.getTimeIntervalsForPeriod(start, end);
    	
    	List<String> monthLabels = createMonthLabels(months);
    	
    	dto.setMonthLabels(monthLabels);
    
    	
    	List<String> gefLables = detectionVariableDAO.getAllDetectionVariableNamesForParentId(Short.valueOf("4"));
    	
		dto.setGefLabels(gefLables);
    	
    	List<GeriatricFactorValue> gefs = assessmentDAO.getDiagramDataForUserInRoleId(1, start, end);
    	
		dto.setGefData(gefs);		
		
        ObjectMapper objectMapper = new ObjectMapper();
        
		String dtoAsString = objectMapper.writeValueAsString(dto);
        
        return dtoAsString;		

    }


	
    @POST
    @Path("/getAssessmentsForSelectedDataSet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public List<Assessment> getAssessmentsForSelectedDataSet(String test) {
    	
    	YearMonth ym = YearMonth.of(2016, Month.JANUARY);

    	List<String> geriatricFactorIds = new ArrayList<String>();
    	
    	List<Assessment> assessments = new ArrayList<Assessment>();
    	
		for (String geriatricFactorId : geriatricFactorIds) {

			assessments.addAll(getAssessmentsForGeriatricFactorId(Long.valueOf(geriatricFactorId)));

		}
		
		return assessments;
	}

    @POST
    @Path("/getAssessmentsByFiler")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Assessment> getAssessmentsByFiler(String test) {
    	List<Assessment> assessments = new ArrayList<Assessment>();
    	String filter = "";
		Long geriatricFactorId = Long.valueOf(1);
		return assessmentDAO.getAssessmentsByFilter(geriatricFactorId, filter);
    }

    @POST
    @Path("/addAssessmentsForSelectedDataSet")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addAssessmentsForSelectedDataSet(String test) {
 
    	YearMonth ym = YearMonth.of(2016, Month.JANUARY);

    	List<String> geriatricFactorIds = new ArrayList<String>();
    	
    	List<Assessment> assessments = new ArrayList<Assessment>();
    	
		for (String geriatricFactorId : geriatricFactorIds) {

			assessments.addAll(getAssessmentsForGeriatricFactorId(Long.valueOf(geriatricFactorId)));

		}
		
    	for (int i = 0; i < assessments.size(); i++) {
    		assessmentDAO.insertOrUpdate(assessments.get(i));
    	}
  
    }

	private List<Assessment> getAssessmentsForGeriatricFactorId(Long geriatricFactorId) {
		return assessmentDAO.getAssessmentsForGeriatricFactorId(geriatricFactorId);
	}
	
	private List<String> createMonthLabels(List<Object[]> months) {
		List<String> monthLabels = new ArrayList<String>();
    	
    	for (int i = 0; i < months.size() ; i++) {
    		monthLabels.add(((Timestamp)months.get(i)[0]).toString());
    	}
    	
    	monthLabels.add(((Timestamp)months.get(months.size()-1)[1]).toString());
		return monthLabels;
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
