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
		
		message.append("Exception: ");
		message.append(ex.getClass().getSimpleName());
		message.append("Property: \"");
		message.append(ex.getPropertyName());
		message.append("\" unrecognized!");

		
		logger.info(message.toString());
		
		 return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
				.entity(message.toString())
				.build();
	}

}
