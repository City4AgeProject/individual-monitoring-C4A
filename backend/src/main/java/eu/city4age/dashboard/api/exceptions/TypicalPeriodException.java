package eu.city4age.dashboard.api.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class TypicalPeriodException extends Exception implements ExceptionMapper<TypicalPeriodException> {
	
	static protected Logger logger = LogManager.getLogger(TypicalPeriodException.class);
	
	private static final long serialVersionUID = 1L;
	 
	public TypicalPeriodException () {
		super("TypicalPeriodException");
	}
	
	public TypicalPeriodException (String message) {
		super(message);
	}
	
	@Override 
	public Response toResponse(TypicalPeriodException ex) {
		
		return Response.status(Response.Status.NOT_ACCEPTABLE)
				.type(MediaType.TEXT_PLAIN)
				.entity(ex.getMessage())
				.build();	
		
	}

}
