package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.exceptions.DirectGESException;
import eu.city4age.dashboard.api.exceptions.MissingKeyException;
import eu.city4age.dashboard.api.exceptions.TypicalPeriodException;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;
import eu.city4age.dashboard.api.pojo.json.desobj.Nui;
import eu.city4age.dashboard.api.pojo.other.ConfigurationCounter;
import eu.city4age.dashboard.api.service.PilotDetectionVariableService;

@Component
public class PilotDetectionVariableServiceImpl implements PilotDetectionVariableService {
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	private static final String emptyString = "";
	
	
	
	public void setConfiguration(Pilot.PilotCode pilotCode,Configuration configuration, Timestamp validFrom, StringBuilder response) throws TypicalPeriodException, MissingKeyException, DirectGESException {

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
					
					int numOfMeasures = subFactor.getMeasures().size();

					for (Mea measure : subFactor.getMeasures()) {
						
						DetectionVariable meaDetectionVariable = findDetectionVariableOfType(measure.getName(),
								DetectionVariableType.MEA);
						
						if (meaDetectionVariable.getDerivedDetectionVariable() != null) {
							if (numOfMeasures > 1) {
								StringBuilder sb = new StringBuilder ();
								sb.append ("Not allowed to have more than one measure per geriatric subfactor in configuration")
								.append(" if that subfactor is configured to be directly derived.").append('\n')
								.append("Please check declaration of : ").append (ovlDetectionVariable.getDetectionVariableName())
								.append(" -> ").append (gfgDetectionVariable.getDetectionVariableName())
								.append(" -> ").append(gefDetectionVariable.getDetectionVariableName())
								.append(" -> ").append(gesDetectionVariable.getDetectionVariableName());
								throw new DirectGESException(sb.toString());
							}
						}
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
			Pilot.PilotCode pilotCode, String formula, BigDecimal weight, ConfigurationCounter cfc,
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
		//if (!dv.getDetectionVariableType().toString().equals("mea") || !ddv.getDetectionVariableType().toString().equals("nui")) {
		if (!dv.getDetectionVariableType().toString().equals(DetectionVariableType.MEA.toString()) || !ddv.getDetectionVariableType().toString().equals(DetectionVariableType.NUI.toString())) {
		//if (!dv.getDetectionVariableType().equals(DetectionVariableType.MEA) || !ddv.getDetectionVariableType().equals(DetectionVariableType.NUI)) {

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

}
