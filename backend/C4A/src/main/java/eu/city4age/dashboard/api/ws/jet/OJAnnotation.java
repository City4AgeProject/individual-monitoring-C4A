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
import eu.city4age.dashboard.api.model.Assessment;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mnou2
 */
@Path(OJAnnotation.PATH)
public class OJAnnotation {

    public static final String PATH = "OJAnnotation";

    static Logger logger = Logger.getLogger(OJAnnotation.class);

    @Autowired
    private AssessmentDAO assessmentDAO;

    @GET
    @Path("forDataPoints")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectForDataPoints(@Context UriInfo ui) throws JsonProcessingException {
        List<Assessment> assessments = new ArrayList<Assessment>();
        try {
            MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
            List<String> geriatricFactorIds = new ArrayList<String>();
            for (Map.Entry entry : queryParams.entrySet()) {
                LinkedList value = (LinkedList) entry.getValue();
                geriatricFactorIds.add(value.getFirst().toString());
            }
            for (String geriatricFactorId : geriatricFactorIds) {
                assessments.addAll(assessmentsForGeriatricFactorId(Long.valueOf(geriatricFactorId)));
            }
        } catch (Exception e) {
            logger.error("in selecting annotations for data points ", e);
        }
        return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(assessments)).build();
    }

    private List<Assessment> assessmentsForGeriatricFactorId(Long geriatricFactorId) {
        return assessmentDAO.getAssessmentsForGeriatricFactorId(geriatricFactorId);
    }

}
