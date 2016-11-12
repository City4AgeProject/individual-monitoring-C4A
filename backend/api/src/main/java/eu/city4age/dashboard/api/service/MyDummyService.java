package eu.city4age.dashboard.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.city4age.dashboard.api.domain.DataPointSet;
import eu.city4age.dashboard.api.dto.DiagramDataPoint;
import eu.city4age.dashboard.api.dto.DiagramDataPointSet;
import eu.city4age.dashboard.api.dto.DiagramMonthInterval;

import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/charts")
public class MyDummyService {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() throws JsonProcessingException {
        //DataPointSet from domain
        DataPointSet dataPointSet = new DataPointSet();
        dataPointSet.setId(111l);
        dataPointSet.setLabel("Contextual");
        
        //Diagram DTO data
        DiagramMonthInterval diagramMonthInterval = new DiagramMonthInterval();
        diagramMonthInterval.setStart(24180); 
        diagramMonthInterval.setEnd(24183);         
        
        DiagramDataPointSet diagramDataPointSet = new DiagramDataPointSet(dataPointSet.getId(), dataPointSet.getLabel());
        diagramDataPointSet.setId(dataPointSet.getId());
        diagramDataPointSet.setLabel(dataPointSet.getLabel());
        diagramMonthInterval.getDiagramDataPointSets().add(diagramDataPointSet); 

        ///get list of data points from the model
        ///////.......TODO        
        
        ///E.G. Filling diagram DTO objects
        //First point     
        diagramDataPointSet.getDiagramDataPoints().add(new DiagramDataPoint(12l, 3.0f, null)); 

        //for e.g. second point iz missing 
        diagramDataPointSet.getDiagramDataPoints().add(null);

        //Third point
        diagramDataPointSet.getDiagramDataPoints().add(new DiagramDataPoint(13l, 4.1f, null)); 
        
        //4th point
        diagramDataPointSet.getDiagramDataPoints().add(new DiagramDataPoint(15l, 5.1f, null)); 
        
        //4th point
        diagramDataPointSet.getDiagramDataPoints().add(new DiagramDataPoint(15l, 5.1f, null)); 

        ObjectMapper objectMapper = new ObjectMapper();
        String dtoAsString = objectMapper.writeValueAsString(diagramMonthInterval);
        return dtoAsString;
    }

    @GET
    @Path("/items")
    public String getItem(@PathParam("itemid") String itemid) throws Exception {
        if (1 == 1) {
            throw new EntityNotFoundException("Item, is not found");
        }
        return "i";
    }

    @GET
    @Path("/images/{image}")
    @Produces("image/*")
    public Response getImage(@PathParam("image") String image) {
        System.out.println("Testiranje");
        File f = new File("f:\\" + image);

        if (!f.exists()) {
            throw new WebApplicationException(404);
        }

        String mt = new MimetypesFileTypeMap().getContentType(f);
        return Response.ok(f, mt).build();
    }
}
