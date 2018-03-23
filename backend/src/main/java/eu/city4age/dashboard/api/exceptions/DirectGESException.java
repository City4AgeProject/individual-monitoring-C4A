package eu.city4age.dashboard.api.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class DirectGESException extends Exception implements ExceptionMapper<DirectGESException> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 457945933414234734L;
	
	static protected Logger logger = LogManager.getLogger(DirectGESException.class);
	
	public DirectGESException () {
		super("DirectGESException");
	}
	
	public DirectGESException (String message) {
		super(message);
	}

	@Override
	public Response toResponse(DirectGESException ex) {
		
		return Response.status(Response.Status.NOT_ACCEPTABLE)
				.type(MediaType.TEXT_PLAIN)
				.entity(ex.getMessage())
				.build();	
	}

}
