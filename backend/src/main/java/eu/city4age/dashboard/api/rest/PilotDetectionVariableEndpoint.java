package eu.city4age.dashboard.api.rest;

import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.exceptions.JsonEmptyException;
import eu.city4age.dashboard.api.exceptions.JsonMappingExceptionUD;
import eu.city4age.dashboard.api.exceptions.NotAuthorizedException;
import eu.city4age.dashboard.api.exceptions.NotLoggedInException;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewPilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.PilotDetectionVariableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**
 * @author Andrija Petrovic
 *
 */
@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Api(value = "configuration", produces = "application/json")
@Path(PilotDetectionVariableEndpoint.PATH)
public class PilotDetectionVariableEndpoint {

	public static final String PATH = "configuration";
	
	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableEndpoint.class);

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private ViewPilotDetectionVariableRepository viewPilotDetectionVariableRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private PilotDetectionVariableService pilotDetectionVariableService;
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	

	@POST
	@ApiOperation("Insert configuration from Pilot Configuration Json into md_pilot_detection_variable table.")
	@Produces(MediaType.TEXT_PLAIN)
	@Path("updateFromConfigFile")	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response updateConfigurationService(@ApiParam(name = "json", value = "configuration string from JSON file", required = true, examples = @Example (value = {
			@ExampleProperty (value = "{\"configurations\":[{\"name\":\"overall\",\"level\":0,\"validFrom\":\"2016-01-01 00:00:01\",\"validTo\":\"2020-09-07 00:00:00\",\"pilotCode\":\"MAD\",\"groups\":[{\"name\":\"contextual\",\"level\":1,\"weight\":0.5,\"formula\":\"\",\"factors\":[]},{\"name\":\"behavioural\",\"level\":1,\"weight\":0.5,\"formula\":\"\",\"factors\":[{\"name\":\"motility\",\"level\":2,\"weight\":0.13,\"formula\":\"\",\"subFactors\":[{\"name\":\"walking\",\"level\":3,\"weight\":1,\"formula\":\"\",\"measures\":[{\"name\":\"walk_distance\",\"level\":4,\"weight\":0.4},{\"name\":\"walk_time_outdoor\",\"level\":4,\"weight\":0.3}]}]}]}]}]}" )
	})) @RequestBody String json) throws Exception {
		
		StringBuilder response = new StringBuilder();
		//boolean doneSomething = false;
		Timestamp validFrom = new Timestamp (System.currentTimeMillis());
		
		ConfigureDailyMeasuresDeserializer data = null;
		try {
			data = objectMapper.readerFor(ConfigureDailyMeasuresDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
					.readValue(json);
		} catch (JsonMappingException e) {
			logger.info(e.getClass().getName());
				if(e instanceof UnrecognizedPropertyException) throw e;
				else if (json.equals("")) throw new JsonEmptyException ("Configuration JSON is empty");				
				else throw new JsonMappingExceptionUD(e);
		}
		
		if (data != null) {
			for (Configuration configuration : data.getConfigurations()) {
				Pilot.PilotCode pilotCode = configuration.getPilotCode();
				String password = configuration.getPassword();
				String username = configuration.getUsername();
				
				logger.info("url: " + configuration.getPersonalProfileDataUrl());

				UserInRole uir;
				try {
					uir = userInRoleRepository.findBySystemUsernameAndPassword(username, password);
					StringBuilder sb;
					if (uir != null) {
						if (uir.getPilotCode().equals(pilotCode)) {
							pilotDetectionVariableService.setConfiguration(pilotCode, configuration, validFrom, response);
						} else {
							sb = new StringBuilder();
							sb.append("You are not authorized to make changes for pilot: ");
							sb.append(pilotRepository.findByPilotCode(pilotCode).getName());
							throw new NotAuthorizedException(sb.toString());
						}
					} else {
						sb = new StringBuilder();
						sb.append("Invalid login! ");
						sb.append("You must provide valid username and password ");
						sb.append("in the coresponding fields of configuration file.");
						throw new NotLoggedInException(sb.toString());
					}
				} catch (NullPointerException e) {
					logger.info("NULLPINTEREX::" + e.getMessage());
				}
			} 
		}
		response.append("\n\tNumber of rows in db after configuration update is: ").append(pilotDetectionVariableRepository.count());
		return JerseyResponse.buildTextPlain(response.toString());

	}	
	
	@GET
	@ApiOperation("Get all Geriatric Subfactors for specific user.")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findAllGes/{userId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "id of user", required = false, dataType = "long", paramType = "path", defaultValue = "10")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ViewPilotDetectionVariable.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response findAllGes(@ApiParam(hidden = true) @PathParam("userId") Long userId) throws JsonProcessingException {
		List<ViewPilotDetectionVariable> list = viewPilotDetectionVariableRepository.findAllGes(userId);
		return JerseyResponse.build(objectMapper.writeValueAsString(list));
	}
	
	@GET
	@ApiOperation("Get all Geriatric Factors for specific user.")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findAllGef/{userId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "id of user", required = false, dataType = "long", paramType = "path", defaultValue = "10")})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ViewPilotDetectionVariable.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public Response findAllGef(@ApiParam(hidden = true) @PathParam("userId") Long userId) throws JsonProcessingException {
		List<ViewPilotDetectionVariable> list = viewPilotDetectionVariableRepository.findAllGef(userId);
		return JerseyResponse.build(objectMapper.writeValueAsString(list));
	}

}
