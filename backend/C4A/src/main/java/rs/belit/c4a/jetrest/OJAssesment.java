/*
 * City4Age Project
 * Horizon 2020  * 
 */
package rs.belit.c4a.jetrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import rs.belit.c4a.jetdto.AssesmentForLastFives;
import rs.belit.c4a.jetdto.Assessment;
import rs.belit.c4a.jetdto.DataSet;
import rs.belit.c4a.jetdto.Group;
import rs.belit.c4a.jetdto.Item;
import rs.belit.c4a.jetdto.Serie;

/**
 *
 * @author mnou2
 */
@Path(OJAssesment.PATH)
public class OJAssesment {

    public static final String PATH = "OJAssesment";

    @Autowired
    private SessionFactory sessionFactory;

    @GET
    @Path("lastFiveForInterval")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lastFiveForInterval(@QueryParam(value = "intervalStart") String intervalStart,
            @QueryParam(value = "intervalEnd") String intervalEnd,
            @QueryParam(value = "userInRoleId") Long userInRoleId) throws JsonProcessingException {
        Session session = sessionFactory.openSession();
        Query q = session.createSQLQuery("SELECT distinct ti.id, ti.interval_start, gfv.id as gef_id, gef_value, assessment_id, assessment_comment, risk_status, data_validity_status ,author_id "
                + "FROM time_interval AS ti "
                + "JOIN (geriatric_factor_value AS gfv "
                + "JOIN (assessed_gef_value_set AS agvs "
                + "INNER JOIN assessment AS aa "
                + "ON agvs.assessment_id = aa.id "
                + ") "
                + "ON agvs.gef_value_id = gfv.id "
                + ") "
                + "ON gfv.time_interval_id=ti.id "
                + "WHERE ti.interval_start >= cast ( :intervalStart as timestamp ) "
                + "AND ti.interval_end <= cast ( :intervalEnd as timestamp ) "
                + "AND "
                + "(aa.id IN (SELECT id FROM "
                + "(SELECT DISTINCT a1.id,a1.created "
                + "FROM assessment a1 "
                + "INNER JOIN assessed_gef_value_set AS agvs1 "
                + "ON agvs1.assessment_id = a1.id "
                + "WHERE agvs1.gef_value_id = agvs.gef_value_id "
                + "ORDER BY a1.created DESC FETCH FIRST 5 ROWS ONLY) "
                + "t) "
                + "OR aa.id IS NULL) "
                + "AND ( "
                + "gfv.user_in_role_id = cast ( :userInRoleId as bigint ) "
                + "OR gfv.id IS NULL "
                + ") "
                + "ORDER BY ti.id");
        q.setParameter("intervalStart", intervalStart);
        q.setParameter("intervalEnd", intervalEnd);
        q.setParameter("userInRoleId", userInRoleId);
        List<AssesmentForLastFives> toLose = new ArrayList<AssesmentForLastFives>();
        for (Object[] objects : (List<Object[]>) q.list()) {
            AssesmentForLastFives aflf = new AssesmentForLastFives();
            aflf.setId(((BigInteger) objects[0]).longValue());
            aflf.setInterval_start(((Timestamp) objects[1]).toString());
            aflf.setGef_id(((BigInteger) objects[2]).longValue());
            aflf.setGef_value(((BigDecimal) objects[3]).floatValue());
            aflf.setAssessment_id(((Integer) objects[4]).longValue());
            aflf.setAssessment_comment((String) objects[5]);
            aflf.setRisk_status(String.valueOf((Character) objects[6]));
            aflf.setData_validity_status(String.valueOf((Character) objects[7]));
            aflf.setAuthor_id(((BigInteger) objects[8]).longValue());
            toLose.add(aflf);
        }

        DataSet forClient = new DataSet();

        Set<Group> groups = new HashSet<Group>();

        for (AssesmentForLastFives assesmentForLastFives : toLose) {
            Group g = new Group();
            g.setId(assesmentForLastFives.getId());
            g.setName(assesmentForLastFives.getInterval_start());
            groups.add(g);
        }

        List<Serie> series = new ArrayList<Serie>();

        Serie alertSerie = new Serie();
        alertSerie.setImgSize("20px");
        alertSerie.setLineType("none");
        alertSerie.setMarkerDisplayed("on");
        alertSerie.setMarkerSize(16);
        alertSerie.setName("Alert");
        alertSerie.setSource("images/flag-red.png");
        series.add(alertSerie);

        Serie warningSerie = new Serie();
        warningSerie.setImgSize("20px");
        warningSerie.setLineType("none");
        warningSerie.setMarkerDisplayed("on");
        warningSerie.setMarkerSize(16);
        warningSerie.setName("Warning");
        warningSerie.setSource("images/flag-beige.png");
        series.add(warningSerie);

        Serie commentSerie = new Serie();
        commentSerie.setImgSize("20px");
        commentSerie.setLineType("none");
        commentSerie.setMarkerDisplayed("on");
        commentSerie.setMarkerSize(16);
        commentSerie.setName("Comment");
        commentSerie.setSource("images/flag-gray.png");
        series.add(commentSerie);

        for (Group g : groups) {
            
            for (AssesmentForLastFives as : toLose) {
                Assessment assesment = new Assessment();
                assesment.setRiskStatus(as.getRisk_status());
                assesment.setDataValidity(as.getData_validity_status());
                assesment.setFrom(String.valueOf(as.getAuthor_id()));
                assesment.setComment(as.getAssessment_comment());
                assesment.setId(as.getAssessment_id());
                Item item = new Item();
                item.setId(as.getGef_id());
                item.setValue(as.getGef_value());
                if (as.getId().equals(g.getId())) {
                    item.getAssessmentObjects().add(assesment);
                    if ("A".equals(assesment.getRiskStatus())) {
                        alertSerie.getItems().add(item);
                        assesment.setRiskStatusDesc("Alert");
                        assesment.setRiskStatusImage("images/risk_alert.png");
                    }else if ("W".equals(assesment.getRiskStatus())) {
                        warningSerie.getItems().add(item);
                        assesment.setRiskStatusDesc("Warning");
                        assesment.setRiskStatusImage("images/risk_warning.png");
                    }else{
                        commentSerie.getItems().add(item);
                        assesment.setRiskStatusDesc("Comment");
                        assesment.setRiskStatusImage("images/comment.png");
                    }

                    if ("F".equals(assesment.getDataValidity())) {
                        assesment.setDataValidityDesc("Faulty data");
                        assesment.setDataValidityImage("images/faulty_data.png");
                    }
                    if ("Q".equals(assesment.getDataValidity())) {
                        assesment.setDataValidityDesc("Questionable data");
                        assesment.setDataValidityImage("images/questionable_data.png");
                    }
                    if ("V".equals(assesment.getDataValidity())) {
                        assesment.setDataValidityDesc("Valid data");
                        assesment.setDataValidityImage("images/valid_data.png");
                    }
                }

            }

        }

        forClient.setGroups(new ArrayList<Group>(groups));
        forClient.setSeries(series);

        return Response.ok(new ObjectMapper().writeValueAsString(forClient)).build();
    }

}
