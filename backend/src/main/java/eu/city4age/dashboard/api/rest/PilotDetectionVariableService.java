package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.persist.ViewPilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.ex.ConfigurationValidityDateException;
import eu.city4age.dashboard.api.pojo.ex.MissingKeyException;
import eu.city4age.dashboard.api.pojo.ex.NotAuthorizedException;
import eu.city4age.dashboard.api.pojo.ex.NotLoggedInException;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;
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
@Transactional("transactionManager")
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
		try {
			
			ClassPathResource resource = new ClassPathResource("PilotConfigurationJsonValidator.json");
			InputStream jsonValidator = resource.getInputStream();
			String validator = IOUtils.toString(new BufferedReader(new InputStreamReader(jsonValidator)));
			JSONObject rawSchema = new JSONObject(new JSONTokener(validator));
			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(new JSONObject(json)); // throws a ValidationException if this object is invalid
			
			ConfigureDailyMeasuresDeserializer data = objectMapper.readerFor(ConfigureDailyMeasuresDeserializer.class)
			.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);


			for (int z = 0; z < data.getConfigurations().size(); z++) {
				Configuration configuration = data.getConfigurations().get(z);
				
				String pilotCode = configuration.getPilotCode();
				String password = configuration.getPassword();
				String username = configuration.getUsername();
				
				UserInRole uir;
				try {
					uir = userInRoleRepository.findBySystemUsernameAndPassword(username,password);
					
					if(uir.getPilotCode().equals(pilotCode)) {
						setConfiguration(pilotCode,configuration,response);
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append("You are not authorized !\nUser: ");
						sb.append(username);
						sb.append(" can't make changes for: ");
						sb.append(pilotCode);
						sb.append(" pilot configuration.");
						throw new NotAuthorizedException(sb.toString());
					}
					
				} catch (NullPointerException e) {
					StringBuilder sb = new StringBuilder();
					sb.append("Invalid login!\n");
					sb.append("You must provide valid Username and Password\n");
					sb.append("in the coresponding fields of configuration file.\n");
					sb.append("You are not logged in!\nInvalid username: ");
					sb.append(username);
					sb.append(" and/or password: ");
					sb.append(password);
					throw new NotLoggedInException(sb.toString());
				}

			}
		} 
		catch(IOException e){
			logger.info("Not a valid Json String: " + e.getMessage());
		}
		
		return JerseyResponse.buildTextPlain(response.toString());

	}

	private void setConfiguration(String pilotCode,Configuration configuration, StringBuilder response) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ConfigurationCounter confCounter = new ConfigurationCounter();
		
		String validFromInString = configuration.getValidFrom();
		String validToInString = configuration.getValidTo();
		Date validFrom;
		Date validTo;

		try {
			validFrom = formatter.parse(validFromInString);
			validTo = formatter.parse(validToInString);
		} catch (ParseException ex) {
			throw new ConfigurationValidityDateException();
		}
		
		
		

		try {
			Pilot pilot = pilotRepository.findByPilotCode(pilotCode);
			
			logger.info("pilotRepository: " + pilotRepository);
			
			pilot.setLatestConfigurationUpdate(new Date());
			pilotRepository.save(pilot);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			throw new MissingKeyException(
					"MissingKeyException:\n\tName for property : pilotCode(pilot_code): "+pilotCode+"\n\tin JSON file doesn't match to any key\n\tin corresponding table: pilot.\n\t");
		}

		for (int i = 0; i < configuration.getGroups().size(); i++) {

			Groups group = configuration.getGroups().get(i);

			logger.info(" DETECTION_VAR(GFG): " + group.getName() + "  DERIVED_VAR(OVL): "
					+ configuration.getName());

			DetectionVariable gfgDetectionVariable = findDetectionVariableOfType(group.getName(),
					DetectionVariableType.GFG);

			DetectionVariable ovlDetectionVariable = findDetectionVariableOfType(configuration.getName(),
					DetectionVariableType.OVL);

			createOrUpdatePilotDetectionVariable(gfgDetectionVariable, ovlDetectionVariable, validFrom, validTo,
					pilotCode, group.getFormula(), group.getWeight(), confCounter);

			for (int j = 0; j < group.getFactors().size(); j++) {

				Gef factor = group.getFactors().get(j);
				logger.info(
						" DETECTION_VAR(GEF): " + factor.getName() + "  DERIVED_VAR(GFG): " + group.getName());

				DetectionVariable gefDetectionVariable = findDetectionVariableOfType(factor.getName(),
						DetectionVariableType.GEF);

				createOrUpdatePilotDetectionVariable(gefDetectionVariable, gfgDetectionVariable, validFrom,
						validTo, pilotCode, factor.getFormula(), factor.getWeight(), confCounter);

				for (int k = 0; k < factor.getSubFactors().size(); k++) {

					Ges subFactor = factor.getSubFactors().get(k);
					logger.info(" DETECTION_VAR(GES): " + subFactor.getName() + "  DERIVED_VAR(GEF): "
							+ factor.getName());

					DetectionVariable gesDetectionVariable = findDetectionVariableOfType(subFactor.getName(),
							DetectionVariableType.GES);
					createOrUpdatePilotDetectionVariable(gesDetectionVariable, gefDetectionVariable, validFrom,
							validTo, pilotCode, subFactor.getFormula(), subFactor.getWeight(), confCounter);

					for (int n = 0; n < subFactor.getMeasures().size(); n++) {

						Mea measure = subFactor.getMeasures().get(n);
						logger.info(" DETECTION_VAR(MEA): " + measure.getName() + "  DERIVED_VAR(GES): "
								+ subFactor.getName());

						DetectionVariable meaDetectionVariable = findDetectionVariableOfType(measure.getName(),
								DetectionVariableType.MEA);
						List<String> NuiList = Arrays.asList("avg", "std", "delta", "best");
						Map<String, String> NuiListFormula = new HashMap<String, String>() {

							private static final long serialVersionUID = 1L;

							{
								put("avg", "avg");
								put("std", "std");
								put("delta", "delta");
								put("best", "best");
							}
						};

						createOrUpdatePilotDetectionVariable(meaDetectionVariable, gesDetectionVariable,
								validFrom, validTo, pilotCode, null, measure.getWeight(), confCounter);

						if (meaDetectionVariable.getDefaultTypicalPeriod().equals("1WK")
								|| meaDetectionVariable.getDefaultTypicalPeriod().equals("DAY")) {

							BigDecimal nuiWeight = BigDecimal.valueOf((double) 1 / NuiListFormula.size());
							String nuiFormula;
							for (String nui : NuiList) {

								DetectionVariable nuiDetectionVariable = findDetectionVariableOfType(
										nui + '_' + measure.getName(), DetectionVariableType.NUI);

								nuiFormula = NuiListFormula.get(nui);
								logger.info(" DETECTION_VAR(NUI): " + nui + '_' + measure.getName()
										+ "  DERIVED_VAR(GES): " + subFactor.getName());
								logger.info(" DETECTION_VAR(MEA): " + measure.getName() + "  DERIVED_VAR(GES): "
										+ nui + '_' + measure.getName());

								createOrUpdatePilotDetectionVariable(nuiDetectionVariable, gesDetectionVariable,
										validFrom, validTo, pilotCode, null, nuiWeight, confCounter);

								createOrUpdatePilotDetectionVariable(meaDetectionVariable, nuiDetectionVariable,
										validFrom, validTo, pilotCode, nuiFormula, nuiWeight, confCounter);
							}
						}

					}
				}
			}
		}

		List<PilotDetectionVariable> AllFromDB = pilotDetectionVariableRepository.findAll();

		response.append("\n\tNumber of rows only For Pilot Code: ");
		response.append(pilotCode);
		response.append("\nand Valid From Date: ");
		response.append(validFrom);
		response.append(" : \n\tNumber of inserted rows from configuration file is: ");
		response.append(confCounter.getInserted());
		response.append("\n\tNumber of updated rows from configuration file is: ");
		response.append(confCounter.getUpdated());

		boolean exists;
		int removed = 0;

		List<String> NUIList = Arrays.asList("avg", "std", "delta", "best");
		for (PilotDetectionVariable one : AllFromDB) {
			exists = true;
			outerLoop: if (one.getPilotCode().equals(configuration.getPilotCode())
					&& one.getValidFrom().compareTo(validFrom) == 0) {

				exists = false;
				for (int i = 0; i < configuration.getGroups().size(); i++) {

					Groups group = configuration.getGroups().get(i);

					if (existPdv(one, group.getName(), configuration.getName()) == false) {

						exists = false;
						for (int j = 0; j < group.getFactors().size(); j++) {

							Gef factor = group.getFactors().get(j);

							if (existPdv(one, factor.getName(), group.getName()) == false) {

								exists = false;
								for (int k = 0; k < factor.getSubFactors().size(); k++) {

									Ges subFactor = factor.getSubFactors().get(k);

									if (existPdv(one, subFactor.getName(), factor.getName()) == false) {

										exists = false;
										for (int n = 0; n < subFactor.getMeasures().size(); n++) {

											Mea measure = subFactor.getMeasures().get(n);

											DetectionVariable meaDetectionVariable = findDetectionVariableOfType(
													measure.getName(), DetectionVariableType.MEA);

											if (existPdv(one, measure.getName(),
													subFactor.getName()) == false) {
												exists = false;

											} else {
												exists = true;
												break outerLoop;
											}

											if (meaDetectionVariable.getDefaultTypicalPeriod().equals("1WK")
													|| meaDetectionVariable.getDefaultTypicalPeriod()
															.equals("DAY")) {

												for (String nui : NUIList) {

													if (existPdv(one, nui + '_' + measure.getName(),
															subFactor.getName()) == false) {
														exists = false;

														if (existPdv(one, measure.getName(),
																nui + '_' + measure.getName()) == false) {
															exists = false;

														} else {
															exists = true;
															break outerLoop;
														}

													} else {
														exists = true;
														break outerLoop;
													}
												}

											}
										}

									} else {
										exists = true;
										break outerLoop;
									}
								}
							} else {
								exists = true;
								break outerLoop;
							}
						}
					} else {
						exists = true;
						break outerLoop;
					}
				}
			}

			if (exists == false) {
				pilotDetectionVariableRepository.delete(one);
				removed++;
			}
		}
		response.append("\n\tNumber of deleted rows is: ");
		response.append(removed);
		response.append("\n\tNumber of rows in DB after update is: ");
		response.append(pilotDetectionVariableRepository.findAll().size());
		
	}

	private DetectionVariable findDetectionVariableOfType(String dvName, DetectionVariableType dvt)
			throws MissingKeyException {
		DetectionVariable dv = detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(dvName,
				dvt);
		if (dv == null) {
			StringBuilder sb = new StringBuilder("MissingKeyException:\n\tName of property : name(detection_variable_name): ");
			sb.append(dvName);
			sb.append("\n\tin JSON file doesn't match to any key\n\tin corresponding table: cd_detection_variable.\n\t");
			throw new MissingKeyException(sb.toString());
		}
		return dv;
	}

	private void createOrUpdatePilotDetectionVariable(DetectionVariable dv, DetectionVariable ddv, Date validFrom,
			Date validTo, String pilotCode, String nuiFormula, BigDecimal nuiWeight, ConfigurationCounter cfc) {

		PilotDetectionVariable pdv1 = pilotDetectionVariableRepository
				.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode, dv.getId(),
						ddv.getId());

		if (pdv1 != null) {
			if (!((pdv1.getDerivationWeight().compareTo(nuiWeight) == 0)
					&& (pdv1.getFormula() == null
							|| (pdv1.getFormula() != null && pdv1.getFormula().trim().compareTo(nuiFormula) == 0))
					&& (pdv1.getValidFrom().compareTo(validFrom) == 0)
					&& (pdv1.getValidTo().compareTo(validTo) == 0))) {

				pdv1.setDerivationWeight(nuiWeight);
				if (nuiFormula != null)
					pdv1.setFormula(nuiFormula.equals("") ? null : nuiFormula);
				pdv1.setValidFrom(validFrom);
				pdv1.setValidTo(validTo);

				pilotDetectionVariableRepository.save(pdv1);
				cfc.incrementUpdated();

			}

		} else {
			PilotDetectionVariable pdv2 = new PilotDetectionVariable();
			pdv2.setValidFrom(validFrom);
			pdv2.setValidTo(validTo);
			pdv2.setPilotCode(pilotCode);
			if (nuiFormula != null)
				pdv2.setFormula(nuiFormula.equals("") ? null : nuiFormula);
			pdv2.setDerivationWeight(nuiWeight);
			pdv2.setDetectionVariable(dv);
			pdv2.setDerivedDetectionVariable(ddv);

			pilotDetectionVariableRepository.save(pdv2);
			cfc.incrementInserted();
		}

	}

	private boolean existPdv(PilotDetectionVariable pdv, String dvName, String ddvName) {
		if (pdv.getDetectionVariable().getDetectionVariableName().equals(dvName)
				&& pdv.getDerivedDetectionVariable().getDetectionVariableName().equals(ddvName))
			return true;
		else
			return false;
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
