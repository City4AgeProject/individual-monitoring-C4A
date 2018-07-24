package eu.city4age.dashboard.api.service;

import java.util.Date;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;

public interface MeasuresService {

	void computeFor1Pilot(String name, String string) throws Exception;
	
	TimeInterval getOrCreateTimeIntervalPilotTimeZone(Date intervalStart, TypicalPeriod typicalPeriod, PilotCode pilotCode);
	
	TimeInterval getOrCreateTimeInterval(Date intervalStart, TypicalPeriod typicalPeriod);
	
	int determineTimeInterval (long start, long end, long differentiator);

}
