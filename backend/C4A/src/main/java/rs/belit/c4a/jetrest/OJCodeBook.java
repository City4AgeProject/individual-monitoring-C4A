/*
 * City4Age Project
 * Horizon 2020  * 
 */
package rs.belit.c4a.jetrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 *
 * @author mnou2
 */
@Path(OJCodeBook.PATH)
public class OJCodeBook {
    
    public static final String PATH = "OJCodeBook";
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @GET
    @Path("selectTable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectTable(@QueryParam(value = "tableName") String tableName) throws JsonProcessingException {
        Session session = sessionFactory.openSession();
        Query q = session.createSQLQuery("SELECT * from " + tableName);
        return Response.ok(new ObjectMapper().writeValueAsString(q.list())).build();
    }
    
}
