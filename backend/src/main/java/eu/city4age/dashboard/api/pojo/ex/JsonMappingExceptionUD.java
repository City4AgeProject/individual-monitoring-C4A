package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;


@Provider
public class JsonMappingExceptionUD extends Exception  implements ExceptionMapper<JsonMappingExceptionUD> {

	static protected Logger logger = LogManager.getLogger(JsonMappingExceptionUD.class);
	private static final long serialVersionUID = 1L;
	
	private StringBuilder message;
	
	public JsonMappingExceptionUD(String string) {
		super(string);
	}
	public JsonMappingExceptionUD() {
		super("JsonMappingExceptionUD");
	}

	public JsonMappingExceptionUD(JsonMappingException e) {
		logger.info(e.getPathReference());
		StringBuilder message = new StringBuilder();
		message.append("Error: ");
		if (e.getLocalizedMessage().contains("Missing required creator property")) message.append(e.getOriginalMessage()).append(" at line ").append (e.getLocation().getLineNr());
		else if (e.getLocalizedMessage().contains("Unexpected character")) message.append("Unexpected character at line ").append (e.getLocation().getLineNr());
		//super("JsonMappingExceptionUD");
		
		//message.append("Exception: ");
		//message.append(e.getClass().getSimpleName());
		//message.append("\n");
		else message.append(e.getOriginalMessage());
		this.message = message;
	}
	
	@Override
	public Response toResponse(JsonMappingExceptionUD e) {
		
		logger.info(e.message.toString());
		
		return Response.status(Response.Status.BAD_REQUEST)
				.type(MediaType.TEXT_PLAIN)
				.entity(e.message.toString()).build();
	}
	
}


