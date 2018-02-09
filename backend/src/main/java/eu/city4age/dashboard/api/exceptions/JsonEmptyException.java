package eu.city4age.dashboard.api.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class JsonEmptyException extends Exception implements ExceptionMapper<JsonEmptyException> {
	
	static protected Logger logger = LogManager.getLogger(JsonEmptyException.class);
	
	private static final long serialVersionUID = 1L;
	 
	public JsonEmptyException () {
		super("JsonEmptyException");
	}
	
	public JsonEmptyException (String message) {
		super(message);
	}
	
	@Override 
	public Response toResponse(JsonEmptyException ex) {
		
		return Response.status(Response.Status.BAD_REQUEST)
				.type(MediaType.TEXT_PLAIN)
				.entity(ex.getMessage())
				.build();	
		
	}

}
