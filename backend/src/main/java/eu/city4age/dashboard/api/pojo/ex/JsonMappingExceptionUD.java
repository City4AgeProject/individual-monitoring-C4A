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
		super("JsonMappingExceptionUD");
		StringBuilder message = new StringBuilder();
		message.append("Exception: ");
		message.append(e.getClass().getSimpleName());
		message.append("\n");
		message.append("\tLocation:\n\tAt Line: ");
		message.append(e.getLocation().getLineNr());
		message.append("\n\tAt column: ");
		message.append(e.getLocation().getColumnNr());
		message.append("\nError message: \n");
		message.append(e.getMessage());
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


