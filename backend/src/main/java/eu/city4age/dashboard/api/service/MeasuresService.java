package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;

public interface MeasuresService {

	void computeFor1Pilot(Pilot pilot) throws Exception;
	
	TimeInterval getOrCreateTimeIntervalPilotTimeZone(Date intervalStart, TypicalPeriod typicalPeriod, PilotCode pilotCode);
	
	TimeInterval getOrCreateTimeInterval(Date intervalStart, TypicalPeriod typicalPeriod);
	
	int determineTimeInterval (long start, long end, long differentiator);

	void computeFor1User(UserInRole uir, Timestamp firstMonth);
	
	void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<NumericIndicatorValue> nuiList);
	
	void computeDmsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<DerivedMeasureValue> dmList);
	
	void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<GeriatricFactorValue> gesList) throws Exception;
	
	void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode, List<GeriatricFactorValue> gfvList) throws Exception;
	
	void computeNuisFor1User(Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir);
	
	void computeGessFor1User(Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir);
	
	void computeFor1MonthFor1User(DetectionVariableType gef, Timestamp startOfMonth, Timestamp endOfMonth,
			UserInRole uir);

}
