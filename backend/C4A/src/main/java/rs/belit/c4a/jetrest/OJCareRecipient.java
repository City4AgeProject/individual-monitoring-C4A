/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.belit.c4a.jetrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mnou2
 */
@Path(OJCareRecipient.PATH)
public class OJCareRecipient {
    
    public static final String PATH = "OJCareRecipient";

    @Autowired
    private SessionFactory sessionFactory;
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lastFiveForInterval(@PathParam(value = "id") Long id) throws JsonProcessingException {
        Session session = sessionFactory.openSession();
        Query q = session.createSQLQuery("SELECT * from cr_profile where id = " + String.valueOf(id));
        return Response.ok(new ObjectMapper().writeValueAsString(q.list())).build();
    }
    
}
