package eu.city4age.dashboard.api.pojo.ex;

import java.text.ParseException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class ParseExceptionMapper implements ExceptionMapper<ParseException>{
	
	static protected Logger logger = LogManager.getLogger(ParseExceptionMapper.class);

	 @Override
	    public Response toResponse(ParseException ex) {
		 
		 String message="ParseException:\n\tCheck are Date properties in your JSON formatted correctly."
				 +"\n\tORIGINAL MESSAGE:"+ex.getMessage();
		 
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message)
		                .build();
		 }
		
		 

}
