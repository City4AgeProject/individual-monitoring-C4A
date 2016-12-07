/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.dao.DetectionVariableDAO;
import eu.city4age.dashboard.api.dao.TimeIntervalDAO;
import eu.city4age.dashboard.api.dto.DiagramDataDTO;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import eu.city4age.dashboard.api.ws.jet.dto.DataSet;

/**
 *
 * @author mnou2
 */
@Path(OJDataSet.PATH)
public class OJDataSet {
    
    public static final String PATH = "OJDataSet";
    
    static protected Logger logger = Logger.getLogger(OJDataSet.class);

    @Autowired
    private AssessmentDAO assessmentDAO;

    @Autowired
    private DetectionVariableDAO detectionVariableDAO;

    @Autowired
    private TimeIntervalDAO timeIntervalDAO;
    
    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@DefaultValue("") @QueryParam(value = "selectGEF") String selectGEF,
                            @DefaultValue("Month") @QueryParam(value = "period") String period,
                            @DefaultValue("0") @QueryParam(value = "dateRangeStart") Integer dateRangeStart,
                            @DefaultValue("10000") @QueryParam(value = "dateRangeEnd") Integer dateRangeEnd) throws JsonProcessingException {
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
		
        DataSet result = new DataSet(dto);
        return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(result)).build();
         
    }
    
    	private List<String> createMonthLabels(List<Object[]> months) {
		List<String> monthLabels = new ArrayList<String>();
    	
    	for (int i = 0; i < months.size() ; i++) {
    		monthLabels.add(((Timestamp)months.get(i)[0]).toString());
    	}
    	
    	monthLabels.add(((Timestamp)months.get(months.size()-1)[1]).toString());
		return monthLabels;
	}
    
}
