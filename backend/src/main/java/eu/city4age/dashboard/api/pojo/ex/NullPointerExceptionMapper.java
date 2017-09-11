package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException>{
	
	static protected Logger logger = LogManager.getLogger(NullPointerExceptionMapper.class);

	 @Override
	    public Response toResponse(NullPointerException ex) {
		 
		 logger.info("\nNullPointerException: Check above this log does KEY of that DETECTION_VAR(?): ? \nexists in DATA BASE in cd_detection_variable TABLE" );
		 String message="NullPointerException:\n\tSome name(s) for detection variables in JSON file doesn't match key(keys)\n\t in cd_detection_variable in DATABASE.\n\tCheck log for more info.";
		 
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message)
		                .build();
		 }
		
		 

}
