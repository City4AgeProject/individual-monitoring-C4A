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
		 
		 StringBuilder message = new StringBuilder();
		 
		 if(ex.getMessage().contains("no JSON")){
			 message.append("JSON is missing!\nPlease insert JSON into field for body of POST request.");
		 }
		 else if(ex.getMessage().contains("Unexpected")||ex.getMessage().contains("Unrecognized")){
			 message.append("JSON is invalid!\nSyntax error at line: ");
			 message.append(ex.getLocation().getLineNr());
			 message.append(" and column: ");
			 message.append(ex.getLocation().getColumnNr());
			 message.append(" \n");
			 message.append(ex.getOriginalMessage());
		 }
		 else if(ex.getMessage().contains("trailing")){
			 
			 message.append("Check if input has trailing data after line: ");
			 message.append(ex.getLocation().getLineNr());
			 message.append(" and column: ");
			 message.append(ex.getLocation().getColumnNr());
			 message.append("\n and remove that then send JSON again.");
			 message.append("\n Or check if JSON has first '{' curly brace and fix it and send JSON again.");
		 }
		 logger.info(new StringBuilder(message).insert(0,"\nEXCEPTION:\n").append("\nORIGINAL MESSAGE: ").append(ex.getOriginalMessage()).toString());
			    return Response
		                .status(Response.Status.BAD_REQUEST)
		                .type(MediaType.TEXT_PLAIN)
		                .entity(message.toString())
		                .build();
		 }
		
		 

}
