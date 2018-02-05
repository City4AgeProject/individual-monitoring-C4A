package eu.city4age.dashboard.api.rest;

import java.math.BigDecimal;
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
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewPilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.ex.JsonEmptyException;
import eu.city4age.dashboard.api.pojo.ex.JsonMappingExceptionUD;
import eu.city4age.dashboard.api.pojo.ex.MissingKeyException;
import eu.city4age.dashboard.api.pojo.ex.NotAuthorizedException;
import eu.city4age.dashboard.api.pojo.ex.NotLoggedInException;
import eu.city4age.dashboard.api.pojo.ex.TypicalPeriodException;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;
import eu.city4age.dashboard.api.pojo.json.desobj.Nui;
import eu.city4age.dashboard.api.pojo.other.ConfigurationCounter;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
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
@Path(PilotDetectionVariableService.PATH)
public class PilotDetectionVariableService {

	public static final String PATH = "configuration";
	
	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableService.class);


	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private ViewPilotDetectionVariableRepository viewPilotDetectionVariableRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;

	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	private static final String emptyString = "";

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
		
		ConfigureDailyMeasuresDeserializer data=null;
		try {
			data = objectMapper.readerFor(ConfigureDailyMeasuresDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
					.readValue(json);
		} 
		catch (JsonMappingException e) {
			logger.info(e.getClass().getName());
				if(e instanceof UnrecognizedPropertyException) throw e;
				else if (json.equals("")) throw new JsonEmptyException ("Configuration JSON is empty");				
				else throw new JsonMappingExceptionUD(e);
		}
		
		for (Configuration configuration: data.getConfigurations()) {
			String pilotCode = configuration.getPilotCode();
			String password = configuration.getPassword();
			String username = configuration.getUsername();
			
			UserInRole uir;
			try {
				uir = userInRoleRepository.findBySystemUsernameAndPassword(username, password);
				StringBuilder sb;
				if(uir!=null) {
					if(uir.getPilotCode().equals(pilotCode)) {
						setConfiguration(pilotCode,configuration, validFrom, response);
					}
					else {
						sb = new StringBuilder();
						sb.append("You are not authorized to make changes for pilot: ");
						sb.append(pilotRepository.findByPilotCode(pilotCode).getName());
						throw new NotAuthorizedException(sb.toString());
					}						
				}
				else {
					sb = new StringBuilder();
					sb.append("Invalid login! ");
					sb.append("You must provide valid username and password ");
					sb.append("in the coresponding fields of configuration file.");					
					throw new NotLoggedInException(sb.toString());
				}					
			}
			catch (NullPointerException e) {
					logger.info("NULLPINTEREX::"+e.getMessage());
			}
		}
		
		response.append("\n\tNumber of rows in db after configuration update is: ").append(pilotDetectionVariableRepository.count());
		return JerseyResponse.buildTextPlain(response.toString());

	}

	private void setConfiguration(String pilotCode,Configuration configuration, Timestamp validFrom, StringBuilder response) throws TypicalPeriodException, MissingKeyException {

		ConfigurationCounter confCounter = new ConfigurationCounter();
		
		Pilot pilot = pilotRepository.findByPilotCode(pilotCode);
		if(pilot.equals(null)) {
			throw new MissingKeyException(
					"MissingKeyException:\n\tName for property : pilotCode(pilot_code): "+pilotCode+"\n\tin JSON file doesn't match to any key\n\tin corresponding table: pilot.\n\t");
			}
						
		pilot.setLatestConfigurationUpdate(validFrom);
		pilotRepository.save(pilot);
		
		List<PilotDetectionVariable> currPilotRepository = pilotDetectionVariableRepository.findByPilotCodeOrderByDetectionVariableId(pilotCode);

		for (Groups group : configuration.getGroups()) {			

			DetectionVariable gfgDetectionVariable = findDetectionVariableOfType(group.getName(),
					DetectionVariableType.GFG);

			DetectionVariable ovlDetectionVariable = findDetectionVariableOfType(configuration.getName(),
					DetectionVariableType.OVL);

			createOrUpdatePilotDetectionVariable(gfgDetectionVariable, ovlDetectionVariable, validFrom,
					pilotCode, group.getFormula(), group.getWeight(), confCounter, currPilotRepository);

			for (Gef factor : group.getFactors()) {
				
				DetectionVariable gefDetectionVariable = findDetectionVariableOfType(factor.getName(),
						DetectionVariableType.GEF);

				createOrUpdatePilotDetectionVariable(gefDetectionVariable, gfgDetectionVariable, validFrom,
						pilotCode, factor.getFormula(), factor.getWeight(), confCounter, currPilotRepository);

				for (Ges subFactor : factor.getSubFactors()) {
					
					DetectionVariable gesDetectionVariable = findDetectionVariableOfType(subFactor.getName(),
							DetectionVariableType.GES);
					
					createOrUpdatePilotDetectionVariable(gesDetectionVariable, gefDetectionVariable, validFrom,
							pilotCode, subFactor.getFormula(), subFactor.getWeight(), confCounter, currPilotRepository);

					for (Mea measure : subFactor.getMeasures()) {
						
						DetectionVariable meaDetectionVariable = findDetectionVariableOfType(measure.getName(),
								DetectionVariableType.MEA);
						
						createOrUpdatePilotDetectionVariable(meaDetectionVariable, gesDetectionVariable,
								validFrom, pilotCode, emptyString, measure.getWeight(), confCounter, currPilotRepository);
						
						List<Nui> nuis = measure.getNuis();
						String meaTypicalPeriod = meaDetectionVariable.getDefaultTypicalPeriod();
						
						if (nuis == null || nuis.isEmpty()) {
							
							if (meaTypicalPeriod.equals("day") ||
									meaTypicalPeriod.equals("1wk")) {
								
								StringBuilder sb = new StringBuilder ();
								
								if (meaTypicalPeriod.equals("day")) {
									sb.append("Daily measure exception: Daily measures must have NUIs. Measure ").
										append (gfgDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (gefDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (gesDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (meaDetectionVariable.getDetectionVariableName()).
										append (" in config file doesnt have NUIs");
								}
								else {
									sb.append("Weekly measure exception: Weekly measures must have NUIs. Measure ").
										append (gfgDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (gefDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (gesDetectionVariable.getDetectionVariableName()).append(" -> ").
										append (meaDetectionVariable.getDetectionVariableName()).
										append (" in config file doesnt have NUIs");
								}
									
								throw new TypicalPeriodException (sb.toString());
							}
						}
						
						else {
							
							if (meaTypicalPeriod.equals("mon")) {
								
								StringBuilder sb = new StringBuilder ();
								sb.append("Monthly measure exception: Monthly measures shouldn't have NUIs. Measure ").
									append (gfgDetectionVariable.getDetectionVariableName()).append(" -> ").
									append (gefDetectionVariable.getDetectionVariableName()).append(" -> ").
									append (gesDetectionVariable.getDetectionVariableName()).append(" -> ").
									append (meaDetectionVariable.getDetectionVariableName()).
									append (" in config file has NUIs");
								
								throw new TypicalPeriodException (sb.toString());
							}
							else {
						
								for (Nui nui : nuis) {								
							
									DetectionVariable nuiDetectionVariable = findDetectionVariableOfType(nui.getName(),
											DetectionVariableType.NUI);
														
									createOrUpdatePilotDetectionVariable(nuiDetectionVariable, gesDetectionVariable,
											validFrom, pilotCode, emptyString, nui.getWeight(), confCounter, currPilotRepository);

									createOrUpdatePilotDetectionVariable(meaDetectionVariable, nuiDetectionVariable,
											validFrom, pilotCode, nui.getFormula(), nui.getWeight(), confCounter, currPilotRepository);
								}
							}
						}
					}
				}
			}
		}
		
		response.append("\n\tNumber of rows for Pilot: ");
		response.append(pilotCode);
		
		response.append("\n\tafter configuration update done on: ");
		response.append(validFrom).append (" UTC");
		
		response.append(" : \n\tNumber of inserted rows in configuration is: ");
		response.append(confCounter.getInserted());
		
		response.append("\n\tNumber of updated rows in configuration is: ");
		response.append(confCounter.getUpdated());
		
		response.append("\n\tNumber of inactive rows in configuration is: ");
		response.append(currPilotRepository.size());
		
		for (PilotDetectionVariable pdv : currPilotRepository) {
			pdv.setValidTo(validFrom);
			pilotDetectionVariableRepository.save(pdv);
		}

		response.append("\n\tNumber of rows in DB after update for this pilot is: ");
		response.append(pilotDetectionVariableRepository.findByPilotCodeOrderByDetectionVariableId(pilotCode).size());
		
	}

	private DetectionVariable findDetectionVariableOfType(String dvName, DetectionVariableType dvt)
			throws MissingKeyException {
		DetectionVariable dv = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(dvName,
				dvt);
		if (dv == null) {
			StringBuilder sb = new StringBuilder("MissingKeyException: name of property: ");
			sb.append(dvName);
			sb.append(" in JSON file doesn't match to any key in table: cd_detection_variable.");
			throw new MissingKeyException(sb.toString());
		}
		return dv;
	}

	private void createOrUpdatePilotDetectionVariable(DetectionVariable dv, DetectionVariable ddv, Timestamp validFrom,
			String pilotCode, String formula, BigDecimal weight, ConfigurationCounter cfc,
			List<PilotDetectionVariable> currList) {
		
		long dvID = dv.getId();
		long ddvID = ddv.getId();
		int index = 0;
		if (currList.size() > 0) {
			for (PilotDetectionVariable pdv : currList) {
				
				if (pdv.getDetectionVariable().getId() == dvID && 
						pdv.getDerivedDetectionVariable().getId() == ddvID) {
					
					if (pdv.getDerivationWeight().compareTo(weight) != 0
							|| !pdv.getFormula().equals(formula)
							|| pdv.getValidTo() != null) {
					
						pdv.setFormula(formula);
						pdv.setDerivationWeight(weight);
						pdv.setValidFrom(validFrom);
						pdv.setValidTo(null);
						cfc.incrementUpdated();
						pilotDetectionVariableRepository.save(pdv);		
						//logger.info("uradjen update na: " + pdv.getDetectionVariable().getDetectionVariableName());
					}
					
					currList.remove(index);
					return;				
				}
				else index++ ;
			}
		}
		
		if (!dv.getDetectionVariableType().toString().equals("mea") || !ddv.getDetectionVariableType().toString().equals("nui")) {
			pilotDetectionVariableRepository.save(new PilotDetectionVariable(pilotCode, ddv, dv, formula, weight,  validFrom, null));
			cfc.incrementInserted();
			//logger.info("uradjen insert na: " + dv.getDetectionVariableName());
		}
		else {
			PilotDetectionVariable pdv = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode, dvID, ddvID);
			if (pdv == null) {
				pilotDetectionVariableRepository.save (new PilotDetectionVariable(pilotCode, ddv, dv, formula, weight,  validFrom, null));
				cfc.incrementInserted();
				//logger.info("uradjen insert na: " + dv.getDetectionVariableName());
			}
		}
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
