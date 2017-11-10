package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Provider
public class NotAuthorizedException extends Exception implements ExceptionMapper<NotAuthorizedException>{
	
	static protected Logger logger = LogManager.getLogger(NotAuthorizedException.class);
	
	private static final long serialVersionUID = 1L;
	
	public NotAuthorizedException() {
		super("NotAuthorizedException");
	}
	
	public NotAuthorizedException(String message) {
		super(message);
	}
	
	@Override
	public Response toResponse(NotAuthorizedException ex) {
		
		 logger.info(new StringBuilder("EXCEPTION:\n").append(ex.getMessage()));
		 
		return Response.status(Response.Status.UNAUTHORIZED)
				.type(MediaType.TEXT_PLAIN)
				.entity(ex.getMessage())
				.build();
		
	}
	

}
