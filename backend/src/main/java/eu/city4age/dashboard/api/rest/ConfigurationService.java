package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.persist.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
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
	List<Groups> groups;

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("updateFromConfigFile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Assessment.class),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public final Response updateConfigurationService() throws Exception {

		Resource schemaFile = new ClassPathResource("/JsonValidator.json", ConfigurationService.class);
		Resource jsonFile = new ClassPathResource("/json1.json", ConfigurationService.class);

		if (ValidationUtils.isJsonValid(schemaFile.getFile(), jsonFile.getFile())) {
			logger.info("Valid!");
		} else {
			logger.info("NOT valid!");
		}

		FileInputStream fis = null;
		String encoding = "utf-8";
		StringBuilder sb = new StringBuilder();

		try {
			fis = new FileInputStream(jsonFile.getFile());

			BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String json = sb.toString();

		@SuppressWarnings("deprecation")
		ConfigureDailyMeasuresDeserializer data = objectMapper.reader(ConfigureDailyMeasuresDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		inserted = 0;
		updated = 0;
		DetectionVariableType dvt = new DetectionVariableType();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInString = data.getDateUpdated();
		Date validFrom = formatter.parse(dateInString);
		// int beforeInsertInDb =
		// pilotDetectionVariableRepository.findAll().size();

		String pilotCode = data.getPilotCode();
		for (int i = 0; i < data.getGroups().size(); i++) {

			Groups group = data.getGroups().get(i);
			logger.info(" DETECTION_VAR(GFG): " + group.getName() + "  DERIVED_VAR(OVL): " + data.getName());

			dvt.setDetectionVariableType("GFG");
			DetectionVariable gfgdv = detectionVariableRepository
					.findByDetectionVariableNameAndDetectionVariableType(group.getName(), dvt);

			dvt.setDetectionVariableType("OVL");
			DetectionVariable ovldv = detectionVariableRepository
					.findByDetectionVariableNameAndDetectionVariableType(data.getName(), dvt);

		
				
				createOrUpdatePilotDetectionVariable(gfgdv, ovldv, validFrom, pilotCode, group);
	
			for (int j = 0; j < data.getGroups().get(i).getFactors().size(); j++) {

				Gef factor = data.getGroups().get(i).getFactors().get(j);
				logger.info(" DETECTION_VAR(GEF): " + factor.getName() + "  DERIVED_VAR(GFG): " + group.getName());

				dvt.setDetectionVariableType("GEF");
				DetectionVariable gefdv = detectionVariableRepository
						.findByDetectionVariableNameAndDetectionVariableType(factor.getName(), dvt);

				createOrUpdatePilotDetectionVariable(gefdv, gfgdv, validFrom, pilotCode, factor);
				for (int k = 0; k < data.getGroups().get(i).getFactors().get(j).getSubFactors().size(); k++) {

					Ges subFactor = data.getGroups().get(i).getFactors().get(j).getSubFactors().get(k);
					logger.info(
							" DETECTION_VAR(GES): " + subFactor.getName() + "  DERIVED_VAR(GEF): " + factor.getName());

					dvt.setDetectionVariableType("GES");
					DetectionVariable gesdv = detectionVariableRepository
							.findByDetectionVariableNameAndDetectionVariableType(subFactor.getName(), dvt);

					createOrUpdatePilotDetectionVariable(gesdv, gefdv, validFrom, pilotCode, subFactor);
					
					for (int n = 0; n < data.getGroups().get(i).getFactors().get(j).getSubFactors().get(k).getMeasures()
							.size(); n++) {

						Mea measure = data.getGroups().get(i).getFactors().get(j).getSubFactors().get(k).getMeasures()
								.get(n);
						logger.info(" DETECTION_VAR(MEA): " + measure.getName() + "  DERIVED_VAR(GES): "
								+ subFactor.getName());

						List<String> NuiList = Arrays.asList("avg", "std", "delta", "best");

						Map<String, String> NuiListFormula = new HashMap<String, String>() {
							{
								put("avg", "avg_formula");
								put("std", "std_formula");
								put("delta", "delta_formula");
								put("best", "best_formula");
							}
						};
						for (String nui : NuiList) {

							dvt.setDetectionVariableType("NUI");
							DetectionVariable nuidv = detectionVariableRepository
									.findByDetectionVariableNameAndDetectionVariableType(nui + '_' + measure.getName(),
											dvt);

							dvt.setDetectionVariableType("MEA");
							DetectionVariable meadv = detectionVariableRepository
									.findByDetectionVariableNameAndDetectionVariableType(measure.getName(), dvt);

							createOrUpdatePilotDetectionVariable(nuidv, gesdv, validFrom, pilotCode, NuiListFormula,
									nui);

							createOrUpdatePilotDetectionVariable(meadv, nuidv, validFrom, pilotCode, measure);

						}
					}
				}
			}
		}

		List<PilotDetectionVariable> AllFromDB = pilotDetectionVariableRepository.findAll();
		String response;
		response = "$$$(Number of ALL rows for all Pilot Codes and Valid Dates : " + AllFromDB.size()
				+ ") Only For Pilot Code: " + pilotCode + " and Valid From Date: " + validFrom + " : "
				+ " Number of inserted rows from configuration file is: " + inserted
				+ " Number of updated rows from configuration file is: " + updated;

		boolean exists;
		int removed = 0;

		List<String> NUIList = Arrays.asList("avg", "std", "delta", "best");
		for (PilotDetectionVariable one : AllFromDB) {
			exists = true;
			outerLoop: if (one.getPilotCode().equals(data.getPilotCode())
					&& one.getValidFrom().compareTo(validFrom) == 0) {

				exists = false;
				for (int i = 0; i < data.getGroups().size(); i++) {

					if (existPdv(one, data.getGroups().get(i).getName(), data.getName()) == false) {
						exists = false;
						for (int j = 0; j < data.getGroups().get(i).getFactors().size(); j++) {

							if (existPdv(one, data.getGroups().get(i).getFactors().get(j).getName(),
									data.getGroups().get(i).getName()) == false) {
								exists = false;
								for (int k = 0; k < data.getGroups().get(i).getFactors().get(j).getSubFactors()
										.size(); k++) {

									if (existPdv(one,
											data.getGroups().get(i).getFactors().get(j).getSubFactors().get(k)
													.getName(),
											data.getGroups().get(i).getFactors().get(j).getName()) == false) {
										exists = false;

										for (int n = 0; n < data.getGroups().get(i).getFactors().get(j).getSubFactors()
												.get(k).getMeasures().size(); n++) {

											for (String nui : NUIList) {

												if (existPdv(one,
														nui + '_' + data.getGroups().get(i).getFactors().get(j)
																.getSubFactors().get(k).getMeasures().get(n).getName(),
														data.getGroups().get(i).getFactors().get(j).getSubFactors()
																.get(k).getName()) == false) {
													exists = false;

													if (existPdv(one,
															data.getGroups().get(i).getFactors().get(j).getSubFactors()
																	.get(k).getMeasures().get(n).getName(),
															nui + '_'
																	+ data.getGroups().get(i).getFactors().get(j)
																			.getSubFactors().get(k).getMeasures().get(n)
																			.getName()) == false) {
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

		response += " Number of Deleted rows is: " + removed + " Number of rows in DB after update is: "
				+ pilotDetectionVariableRepository.findAll().size();
		return Response.ok(objectMapper.writeValueAsString(response)).build();
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
			if (!((Y.getDerivationWeight().compareTo(BigDecimal.valueOf((double) 1 / nf.size())) == 0)
					&& (Y.getFormula().compareTo(nf.get(n))) == 0)) {
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
			if (!((Y.getDerivationWeight().compareTo(e.getWeight()) == 0)
					&& (Y.getFormula().compareTo(e.getFormula()) == 0))) {
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
