package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;

import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;

public interface ComputeService {
	
	void computeAllFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) throws Exception;

}
