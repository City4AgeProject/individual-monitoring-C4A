package eu.city4age.dashboard.api.rest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.AssessmentRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.C4AAndroidResponse;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author milos.holclajtner
 *
 */
@Component
@Transactional("transactionManager")
@Path(AndroidService.PATH)
public class AndroidService {

	public static final String PATH = "android";

	static protected Logger logger = LogManager.getLogger(MeasuresService.class);
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Autowired
	UserInRoleRepository userInRoleRepository;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("postFromAndroid")
	public Response postFromAndroid(String json) throws JsonProcessingException, IOException, ProcessingException {

		@SuppressWarnings("deprecation")
		AndroidActivitiesDeserializer data = objectMapper.reader(AndroidActivitiesDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		
		C4AAndroidResponse response = new C4AAndroidResponse();
		List<UserInRole> uir = userInRoleRepository.findByUserInSystemId(data.getId());
		//response.setId(data.getId());
		
		response.setResult(0);
		if(uir.size()<1){
			response.setMessage("There are no users in role with this ID !");
		}
		else if(uir.size()>1){
			response.setMessage("There are more than ONE user in role with same ID !");
		}
		else if(uir.get(0)!=null){
			response.setResult(1);	
			logger.info("@ FROM ANDROID  data:"+data+"  @");
			logger.info("@ FROM ANDROID  json:"+json+"  @");
			logger.info("@ FROM ANDROID  data.getDate():"+data.getDate()+"  @");
			logger.info("@ FROM ANDROID@  data.getId():"+data.getId()+"  @");
			
			
			//Files.write(Paths.get("C:\\Users\\Andrija.Petrovic\\Desktop\\ANDROID_JSON.txt"), json.getBytes(), StandardOpenOption.APPEND);
			
		}
	
		
		

		return Response.ok(objectMapper.writeValueAsString(response)).build();
	}

}