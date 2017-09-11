package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.ex.JsonValidationException;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Element;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;
import eu.city4age.dashboard.api.utils.ValidationUtils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Component
@Transactional("transactionManager")
@Path(ConfigurationService.PATH)
public class ConfigurationService {

	public static final String PATH = "configuration";

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	static protected Logger logger = LogManager.getLogger(ConfigurationService.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	int inserted;
	int updated;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("updateFromConfigFile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public final Response updateConfigurationService(String json) throws FileNotFoundException, IOException,
			ProcessingException, ParseException, ContainerException, JsonParseException, JsonValidationException {

		Resource schemaFile = new ClassPathResource("/JsonValidator.json", ConfigurationService.class);
		String response = "";

		FileInputStream fis = null;
		String encoding = "utf-8";
		StringBuilder sb = new StringBuilder();


		fis = new FileInputStream(schemaFile.getFile());

		BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}

		String jsonSchema = sb.toString();

		if (ValidationUtils.isJsonValid(jsonSchema, json)) {
			logger.info("Configuration Json from Post is valid!");

			@SuppressWarnings("deprecation")
			ConfigureDailyMeasuresDeserializer data = objectMapper.reader(ConfigureDailyMeasuresDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
			inserted = 0;
			updated = 0;

			DetectionVariableType detVarType = new DetectionVariableType();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateInString = data.getDateUpdated();
			Date validFrom = formatter.parse(dateInString);
			int startingNumberOfRows = pilotDetectionVariableRepository.findAll().size();

			String pilotCode = data.getPilotCode();

			
			for (int i = 0; i < data.getGroups().size(); i++) {

				Groups group = data.getGroups().get(i);
				
				logger.info(" DETECTION_VAR(GFG): " + group.getName() + "  DERIVED_VAR(OVL): " + data.getName());

				detVarType.setDetectionVariableType("GFG");
				DetectionVariable gfgDetectionVariable = detectionVariableRepository
						.findByDetectionVariableNameAndDetectionVariableType(group.getName(), detVarType);

				detVarType.setDetectionVariableType("OVL");
				DetectionVariable ovlDetectionVariable = detectionVariableRepository
						.findByDetectionVariableNameAndDetectionVariableType(data.getName(), detVarType);

				createOrUpdatePilotDetectionVariable(gfgDetectionVariable, ovlDetectionVariable, validFrom, pilotCode, group);

				for (int j = 0; j < group.getFactors().size(); j++) {

					Gef factor = group.getFactors().get(j);
					logger.info(" DETECTION_VAR(GEF): " + factor.getName() + "  DERIVED_VAR(GFG): " + group.getName());

					detVarType.setDetectionVariableType("GEF");
					DetectionVariable gefDetectionVariable = detectionVariableRepository
							.findByDetectionVariableNameAndDetectionVariableType(factor.getName(), detVarType);

					createOrUpdatePilotDetectionVariable(gefDetectionVariable, gfgDetectionVariable, validFrom, pilotCode, factor);

					for (int k = 0; k < factor.getSubFactors().size(); k++) {

						Ges subFactor = factor.getSubFactors().get(k);
						logger.info(" DETECTION_VAR(GES): " + subFactor.getName() + "  DERIVED_VAR(GEF): "
								+ factor.getName());

						detVarType.setDetectionVariableType("GES");
						DetectionVariable gesDetectionVariable = detectionVariableRepository
								.findByDetectionVariableNameAndDetectionVariableType(subFactor.getName(), detVarType);

						createOrUpdatePilotDetectionVariable(gesDetectionVariable, gefDetectionVariable, validFrom, pilotCode, subFactor);

						for (int n = 0; n < subFactor.getMeasures().size(); n++) {

							Mea measure = subFactor.getMeasures().get(n);
							logger.info(" DETECTION_VAR(MEA): " + measure.getName() + "  DERIVED_VAR(GES): "
									+ subFactor.getName());
							
							detVarType.setDetectionVariableType("MEA");
							DetectionVariable meaDetectionVariable = detectionVariableRepository
									.findByDetectionVariableNameAndDetectionVariableType(measure.getName(), detVarType);

							List<String> NuiList = Arrays.asList("avg", "std", "delta", "best");

							Map<String, String> NuiListFormula = new HashMap<String, String>() {
								{
									put("avg", "avg");
									put("std", "std");
									put("delta", "delta");
									put("best", "best");
								}
							};
							
							createOrUpdatePilotDetectionVariable(meaDetectionVariable, gesDetectionVariable, validFrom, pilotCode, measure);
							
							if(meaDetectionVariable.getDefaultTypicalPeriod().equals("DAY")){
								for (String nui : NuiList) {

									detVarType.setDetectionVariableType("NUI");
									DetectionVariable nuiDetectionVariable = detectionVariableRepository
											.findByDetectionVariableNameAndDetectionVariableType(
													nui + '_' + measure.getName(), detVarType);

									createOrUpdatePilotDetectionVariable(nuiDetectionVariable, gesDetectionVariable, validFrom, pilotCode, NuiListFormula,
											nui);
									createOrUpdatePilotDetectionVariable(meaDetectionVariable, nuiDetectionVariable, validFrom, pilotCode, measure);
								}
							}
							
						}
					}
				}
			}


			List<PilotDetectionVariable> AllFromDB = pilotDetectionVariableRepository.findAll();

			response = "Number of ALL rows for all Pilot Codes and Valid Dates : " + startingNumberOfRows
					+ "\n\tNumber of rows only For Pilot Code: " + pilotCode + " and Valid From Date: " + validFrom
					+ " : " + "\n\tNumber of inserted rows from configuration file is: " + inserted
					+ "\n\tNumber of updated rows from configuration file is: " + updated;

			boolean exists;
			int removed = 0;

			List<String> NUIList = Arrays.asList("avg", "std", "delta", "best");
			for (PilotDetectionVariable one : AllFromDB) {
				exists = true;
				outerLoop: if (one.getPilotCode().equals(data.getPilotCode())
						&& one.getValidFrom().compareTo(validFrom) == 0) {

					exists = false;
					for (int i = 0; i < data.getGroups().size(); i++) {
						
						Groups group = data.getGroups().get(i);
						
						if (existPdv(one, group.getName(), data.getName()) == false) {
							
							exists = false;
							for (int j = 0; j < group.getFactors().size(); j++) {

								Gef factor = group.getFactors().get(j);
								
								if (existPdv(one, factor.getName(),
										group.getName()) == false) {
									
									exists = false;
									for (int k = 0; k < factor.getSubFactors()
											.size(); k++) {

										Ges subFactor = factor.getSubFactors().get(k);
										
										if (existPdv(one, subFactor.getName(),factor.getName()) == false) {
											
											exists = false;
											for (int n = 0; n < subFactor.getMeasures().size(); n++) {
												
												Mea measure = subFactor.getMeasures().get(n);
												
												detVarType.setDetectionVariableType("MEA");
												DetectionVariable meaDetectionVariable = detectionVariableRepository
														.findByDetectionVariableNameAndDetectionVariableType(measure.getName(), detVarType);

												
												if (existPdv(one, measure.getName(),subFactor.getName()) == false) {
													exists = false;

												} else {
													exists = true;
													break outerLoop;
												}
												
												
												if(meaDetectionVariable.getDefaultTypicalPeriod().equals("DAY")){
											
													for (String nui : NUIList) {

														if (existPdv(one,nui+ '_'+ measure.getName(),subFactor.getName()) == false) {
															exists = false;

															if (existPdv(one, measure.getName(),nui+'_'+ measure.getName()) == false) {
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
				} else {
					logger.info("IT IS PRESENT IN THE CONFIGURATION FILE!");
				}

			}

			response += "\n\tNumber of deleted rows is: " + removed + "\n\tNumber of rows in DB after update is: "
					+ pilotDetectionVariableRepository.findAll().size();

		} else {

			response += "Json is not valid!\nProperty(ies) is/are missing or it has incorrect data type!";

			Iterator itr = ValidationUtils.getReport(jsonSchema, json).iterator();
			ProcessingMessage message;
			while (itr.hasNext()) {

				message = (ProcessingMessage) itr.next();

				String pointer = message.asJson().get("instance").get("pointer").toString();

				if (pointer.length() > 2) {
					pointer = pointer.substring(2, pointer.length() - 1);
					String[] parts = pointer.split("/");
					String c = " ";
					response += "\nError in configuration json file at location:";
					for (String p : parts) {
						c += " ";
						response += "\n" + c + p + ":";
					}
					response = response.substring(0, response.length() - 1);
				} else {
					response += "\nError in configuration json file located:\n at the ROOT of the JSON file";
				}

				if (message.asJson().get("keyword").toString().equals("\"type\"")) {
					response += "\nExpected data type is: "
							+ message.asJson().get("expected").toString().replaceAll("[\\[|\\]]", "");
					response += ", but: " + message.asJson().get("found").toString() + " is found.";

				} else if (message.asJson().get("keyword").toString().equals("\"required\"")) {
					response += "\nMissing property is: "
							+ message.asJson().get("missing").toString().replaceAll("[\\[|\\]]", "");

				} else {
					logger.info("Other type of JsonValidationException!");
				}

				throw new JsonValidationException(response);

			}

		}
		return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(response).build();

	}

	private void createOrUpdatePilotDetectionVariable(DetectionVariable dv, DetectionVariable ddv, Date validFrom,
			String pilotCode, Map<String, String> nf, String n) {

		PilotDetectionVariable X = new PilotDetectionVariable();
		X.setValidFrom(validFrom);
		X.setPilotCode(pilotCode);
		X.setDerivationWeight(BigDecimal.valueOf((double) 1 / nf.size()));
		X.setFormula(nf.get(n));
		X.setDetectionVariable(dv);
		X.setDerivedDetectionVariable(ddv);

		PilotDetectionVariable Y = pilotDetectionVariableRepository
				.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableIdAndValidFrom(pilotCode,
						dv.getId(), ddv.getId(), validFrom);
		if (Y != null) {

			// if formula column in DB is CHAR size N, it will return FORMULA +
			// ( N - FORMULA length) ' ' spaces !!!
			// REMOVE .trim() when formula column in md_pilot_det_var changes
			// from CHAR(1000) TO VARCHAR
			if (!((Y.getDerivationWeight().compareTo(BigDecimal.valueOf((double) 1 / nf.size())) == 0)
					&& (Y.getFormula().trim().compareTo(nf.get(n))) == 0)) {


				Y.setDerivationWeight(BigDecimal.valueOf((double) 1 / nf.size()));
				Y.setFormula(nf.get(n));
				pilotDetectionVariableRepository.save(Y);
				updated++;
			}
		} else {
			pilotDetectionVariableRepository.save(X);
			inserted++;
		}

	}

	private void createOrUpdatePilotDetectionVariable(DetectionVariable dv, DetectionVariable ddv, Date validFrom,
			String pilotCode, Element e) {
		PilotDetectionVariable X = new PilotDetectionVariable();
		X.setValidFrom(validFrom);
		X.setPilotCode(pilotCode);
		X.setDerivationWeight(e.getWeight());
		X.setFormula(e.getFormula());
		X.setDetectionVariable(dv);
		X.setDerivedDetectionVariable(ddv);

		PilotDetectionVariable Y = pilotDetectionVariableRepository
				.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableIdAndValidFrom(pilotCode,
						dv.getId(), ddv.getId(), validFrom);
		if (Y != null) {

			// if formula column in DB is CHAR size N, it will return FORMULA +
			// ( N - FORMULA length) ' ' spaces !!!
			// REMOVE .trim() when formula column in md_pilot_det_var changes
			// from CHAR(1000) TO VARCHAR
			if (!((Y.getDerivationWeight().compareTo(e.getWeight()) == 0)
					&& (Y.getFormula().trim().compareTo(e.getFormula()) == 0))) {


				Y.setDerivationWeight(e.getWeight());
				Y.setFormula(e.getFormula());
				pilotDetectionVariableRepository.save(Y);
				updated++;
			}
		} else {
			pilotDetectionVariableRepository.save(X);
			inserted++;
		}
	}

	private boolean existPdv(PilotDetectionVariable p, String e, String de) {

		if (p.getDetectionVariable().getDetectionVariableName().equals(e)
				&& p.getDerivedDetectionVariable().getDetectionVariableName().equals(de)) {
			return true;
		} else {
			return false;
		}

	}

}
