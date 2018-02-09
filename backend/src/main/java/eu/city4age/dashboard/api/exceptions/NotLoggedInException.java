package eu.city4age.dashboard.api.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider	
public class NotLoggedInException extends Exception implements ExceptionMapper<NotLoggedInException> {
	
	static protected Logger logger = LogManager.getLogger(ConfigurationValidityDateException.class);
	
	 private static final long serialVersionUID = 1L;
	 
	public NotLoggedInException() {
		super("NotLoggedInException");
	}
	
	public NotLoggedInException(String message) {
		super(message);
	}
	
	@Override 
	public Response toResponse(NotLoggedInException ex) {
		
		logger.info(new StringBuilder("EXCEPTION:\n").append(ex.getMessage()));
		
		return Response.status(Response.Status.UNAUTHORIZED)
				.type(MediaType.TEXT_PLAIN)
				.entity(ex.getMessage())
				.build();
	}

}
