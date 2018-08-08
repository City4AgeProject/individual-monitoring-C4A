package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;

import eu.city4age.dashboard.api.exceptions.DirectGESException;
import eu.city4age.dashboard.api.exceptions.MissingKeyException;
import eu.city4age.dashboard.api.exceptions.TypicalPeriodException;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;

public interface PilotDetectionVariableService {

	void setConfiguration(Pilot.PilotCode pilotCode,Configuration configuration, Timestamp validFrom, StringBuilder response) throws TypicalPeriodException, MissingKeyException, DirectGESException;
	
	
	
}
