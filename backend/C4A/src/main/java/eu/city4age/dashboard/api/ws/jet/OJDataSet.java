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
import eu.city4age.dashboard.api.model.AssessedGefValueSet;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.ws.AssessmentsService;
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
import java.util.Iterator;
import java.util.Set;

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

    @Autowired
    private AssessmentsService assessmentsService;
    
    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@DefaultValue("2016-01-01 00:00:00") @QueryParam(value = "start") String startParam,
                            @DefaultValue("2017-01-01 00:00:00") @QueryParam(value = "end") String endParam,
                            @DefaultValue("4") @QueryParam(value = "parentId") Integer parentIdParam,
                            @DefaultValue("1") @QueryParam(value = "userInRoleId") Integer userInRoleIdParam) throws JsonProcessingException {
        
        // Get query parameters and load DiagramDataDTO.
        Timestamp start = Timestamp.valueOf(startParam);
        Timestamp end = Timestamp.valueOf(endParam);
        DiagramDataDTO dto = new DiagramDataDTO();
        List<Object[]> months = timeIntervalDAO.getTimeIntervalsForPeriod(start, end);
        List<String> monthLabels = createMonthLabels(months);
        dto.setMonthLabels(monthLabels);
        List<String> gefLables = detectionVariableDAO.getAllDetectionVariableNamesForParentId(parentIdParam.shortValue());
        dto.setGefLabels(gefLables);
        List<GeriatricFactorValue> gefs = assessmentDAO.getDiagramDataForUserInRoleId(userInRoleIdParam, start, end);
        dto.setGefData(gefs);

        // Initialize resulting DataSet.
        DataSet result = new DataSet(dto);

        // Load related DataSet Assignments.
        List<String> geriatricFactorIds = new ArrayList<String>();
        for (GeriatricFactorValue gefv : dto.getGefData()) {
            geriatricFactorIds.add(String.valueOf(gefv.getId()));
        }
        List<Assessment> assessments = new ArrayList<Assessment>();
        for (String geriatricFactorId : geriatricFactorIds) {
            assessments.addAll(assessmentDAO.getAssessmentsForGeriatricFactorId(Long.valueOf(geriatricFactorId)));
        }

        result.addAssesmentsPointsToSeries(assessments);

        return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(result)).build();
    }

    private List<String> createMonthLabels(List<Object[]> months) {
        List<String> monthLabels = new ArrayList<String>();

        for (int i = 0; i < months.size(); i++) {
            monthLabels.add(((Timestamp) months.get(i)[0]).toString());
        }

        monthLabels.add(((Timestamp) months.get(months.size() - 1)[1]).toString());
        return monthLabels;
    }

}
