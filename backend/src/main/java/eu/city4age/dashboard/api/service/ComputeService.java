package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;

public interface ComputeService {
	
	void computeAllFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode,
			List<NumericIndicatorValue> nuiList, List<GeriatricFactorValue> gfvList) throws Exception;
	
	public void computeAllFor1UserFor1Month (Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir) throws Exception;

}
