package eu.city4age.dashboard.api.pojo.ex;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException>{
	
	static protected Logger logger = LogManager.getLogger(JsonParseExceptionMapper.class);

	 @Override
	    public Response toResponse(JsonParseException ex) {
		 
		 String message="";
		 if(ex.getMessage().contains("no JSON")){
			 message="JSON is missing!\nPlease insert JSON into field for body of POST request.";
		 }
		 else if(ex.getMessage().contains("Unexpected")||ex.getMessage().contains("Unrecognized")){
			 message = "JSON is invalid!\nSyntax error at line: "+ex.getLocation().getLineNr()
					 +" and column: "+ex.getLocation().getColumnNr()+" \n"+ex.getOriginalMessage();
		 }
		 else if(ex.getMessage().contains("trailing")){
			 message="Check if input has trailing data after line: "+ex.getLocation().getLineNr()
				 +" and column: "+ex.getLocation().getColumnNr() + "\n and remove that then send JSON again."
				 +"\n Or check if JSON has first '{' curly brace and fix it and send JSON again.";
		 }
		 logger.info("\nEXCEPTION:\n"+message+"\nORIGINAL MESSAGE: "+ex.getOriginalMessage());
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message)
		                .build();
		 }
		
		 

}
