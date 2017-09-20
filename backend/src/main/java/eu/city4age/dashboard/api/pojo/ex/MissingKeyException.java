package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class MissingKeyException extends Exception implements ExceptionMapper<MissingKeyException>{
	//TODO make this exception rollback transaction ?
	static protected Logger logger = LogManager.getLogger(MissingKeyException.class);

	 private static final long serialVersionUID = 1L;
	 
	    public MissingKeyException() {
	        super("MissingKeyException: Missing key in DB Exception!");
	    }
	 
	    public MissingKeyException(String string) {
	    	super(string);
	    	 
	    }
	    
	 @Override
	    public Response toResponse(MissingKeyException ex) {
		 
		 logger.info("EXCEPTION:\n"+ex.getMessage());
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(ex.getMessage())
		                .build();
		 }
		
		 

}
