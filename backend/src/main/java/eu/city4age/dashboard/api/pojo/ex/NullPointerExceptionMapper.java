package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException>{
	//TODO make this exception rollback transaction ?
	static protected Logger logger = LogManager.getLogger(NullPointerExceptionMapper.class);

	 @Override
	    public Response toResponse(NullPointerException ex) {
		 
		 String message="NullPointerException:\n\tSome names for properties : name(detection_variable_name) or pilotCode(pilot_code) \n\tin JSON file doesn't match key(keys)\n\tin corresponding tables: cd_detection_variable or pilot.\n\t";
		 
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message)
		                .build();
		 }
		
		 

}
