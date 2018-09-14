package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.util.Date;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;

public interface MeasuresService {

	void computeFor1Pilot(String name, String string) throws Exception;
	
	TimeInterval getOrCreateTimeIntervalPilotTimeZone(Date intervalStart, TypicalPeriod typicalPeriod, PilotCode pilotCode);
	
	TimeInterval getOrCreateTimeInterval(Date intervalStart, TypicalPeriod typicalPeriod);
	
	int determineTimeInterval (long start, long end, long differentiator);

	void computeFor1User(UserInRole uir, Timestamp firstMonth);
	
	void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode);
	
	void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) throws Exception;
	
	void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode) throws Exception;

}
