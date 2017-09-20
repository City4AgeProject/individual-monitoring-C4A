package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

	static protected Logger logger = LogManager.getLogger(UnrecognizedPropertyExceptionMapper.class);

	@SuppressWarnings("deprecation")
	@Override
	public Response toResponse(UnrecognizedPropertyException ex) {
		
		String message = "Property: \"" + ex.getUnrecognizedPropertyName() + "\" unrecognized!\n At line:"
				+ ex.getLocation().getLineNr() + " and column:" + ex.getLocation().getColumnNr();
		
		logger.info("\nEXCEPTION:\n"+message+"\nORIGINAL MESSAGE: "+ex.getOriginalMessage());
		
		 return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
				.entity(message)
				.build();
	}

}
