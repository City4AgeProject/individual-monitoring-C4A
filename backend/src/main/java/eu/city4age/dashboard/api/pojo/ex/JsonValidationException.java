package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class JsonValidationException extends Exception implements ExceptionMapper<JsonValidationException> {

	static protected Logger logger = LogManager.getLogger(JsonValidationException.class);

	private static final long serialVersionUID = 1L;

	public JsonValidationException() {
		super("JsonValidationException!");
	}

	public JsonValidationException(String message) {
		super(message);
		}
	

	@Override
	public Response toResponse(JsonValidationException ex) {
		
		logger.info("\nEXCEPTION:\n"+ex.getMessage());
		 
		return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();

	}


}
