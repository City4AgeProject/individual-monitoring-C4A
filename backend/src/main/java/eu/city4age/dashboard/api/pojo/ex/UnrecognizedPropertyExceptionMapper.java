package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

	static protected Logger logger = LogManager.getLogger(UnrecognizedPropertyExceptionMapper.class);

	@Override
	public Response toResponse(UnrecognizedPropertyException ex) {
		
		
		StringBuilder message = new StringBuilder();
		message.append("Property: \"");
		message.append(ex.getPropertyName());
		message.append("\" unrecognized!\n At line:");
		message.append(ex.getLocation().getLineNr());
		message.append(" and column:");
		message.append(ex.getLocation().getColumnNr());

		
		logger.info(new StringBuilder(message).insert(0, "\nEXCEPTION:\n").append("\nORIGINAL MESSAGE: ").append(ex.getOriginalMessage()).toString());
		
		 return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
				.entity(message.toString())
				.build();
	}

}
