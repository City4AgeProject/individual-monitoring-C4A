package eu.city4age.dashboard.api.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class ConfigurationValidityDateException extends Exception implements ExceptionMapper<ConfigurationValidityDateException>{
	
	static protected Logger logger = LogManager.getLogger(ConfigurationValidityDateException.class);
	
	 private static final long serialVersionUID = 1L;
	 

	 @Override
	    public Response toResponse(ConfigurationValidityDateException ex) {
		 
		 StringBuilder message = new StringBuilder("ConfigurationValidityDateException:\n\tCheck are Date properties in your JSON formatted correctly.");
		 
		 logger.info(new StringBuilder(message).insert(0,"EXCEPTION:\n").toString());
		 
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message.toString())
		                .build();
		 }
		
		 

}
