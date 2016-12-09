/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectReader;
import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.json.AddAssessmentWrapper;
import eu.city4age.dashboard.api.model.AbstractBaseEntity;
import eu.city4age.dashboard.api.model.AssessedGefValueSet;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.AssessmentAudienceRole;
import eu.city4age.dashboard.api.model.CdRole;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.UserInRole;
import eu.city4age.dashboard.api.ws.jet.dto.Annotation;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    private static Logger logger = Logger.getLogger(OJAnnotation.class);

    @Autowired
    private AssessmentDAO assessmentDAO;

    @GET
    @Path("forDataPoints")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectForDataPoints(@Context UriInfo ui) throws JsonProcessingException {
            List<Assessment> assessments = new ArrayList<Assessment>();
            try {
                MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
                Set<String> geriatricFactorIds = new HashSet<String>();
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
            
            Set<Annotation> annotations = new HashSet<>();
            for(Assessment assessment : assessments) {
                annotations.add(new Annotation(assessment));
            }
            
            return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(annotations)).build();
    }

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String json) throws URISyntaxException, IOException {
        ObjectReader objectReader = ObjectMapperProvider.produceMapper().reader(AddAssessmentWrapper.class);
        AddAssessmentWrapper data = objectReader.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
        Assessment assessment = new Assessment();
        assessment.setUserInRole(new UserInRole());
        assessment.getUserInRole().setId(data.getAuthorId());
        assessment.setAssessmentComment(data.getComment());
        assessment.setRiskStatus(data.getRiskStatus().toChar());
        assessment.setDataValidityStatus(data.getDataValidityStatus().toChar());
        assessment.setCreated(new Date());
        List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
        for (int i = 0; i < data.getAudienceIds().size(); i++) {
            AssessmentAudienceRole assessmentAudienceRole = new AssessmentAudienceRole();
            assessmentAudienceRole.setAssessment(assessment);
            assessmentAudienceRole.setAssigned(new Date());
            assessmentAudienceRole.setCdRole(new CdRole());
            assessmentAudienceRole.getCdRole().setId(data.getAudienceIds().get(i));
            assessmentAudienceRoles.add(assessmentAudienceRole);
        }
        List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();
        for (int i = 0; i < data.getGeriatricFactorValueIds().size(); i++) {
            AssessedGefValueSet assessedGefValueSet = new AssessedGefValueSet();
            assessedGefValueSet.setAssessment(assessment);
            assessedGefValueSet.setGeriatricFactorValue(new GeriatricFactorValue());
            assessedGefValueSet.getGeriatricFactorValue().setId(data.getGeriatricFactorValueIds().get(i));
            assessedGefValueSets.add(assessedGefValueSet);
        }
        AbstractBaseEntity saved = assessmentDAO.insertOrUpdate(assessment);

        return Response.created(new URI("/" + PATH + "/" + saved.getId())).build();
    }
    
    private List<Assessment> assessmentsForGeriatricFactorId(Long geriatricFactorId) {
        return assessmentDAO.getAssessmentsForGeriatricFactorId(geriatricFactorId);
    }

}
