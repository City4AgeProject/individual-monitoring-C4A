package eu.city4age.dashboard.api.service.impl;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.service.ComputeService;
import eu.city4age.dashboard.api.service.MeasuresService;

@Component
public class ComputeServiceImpl implements ComputeService {
	
	@Autowired
	private MeasuresService measuresService;

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeAllFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) throws Exception {
		
		measuresService.computeNuisFor1Month(startOfMonth, endOfMonth, pilotCode);
		measuresService.computeGESsFor1Month(startOfMonth, endOfMonth, pilotCode);
		measuresService.computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCode);
		measuresService.computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCode);
		measuresService.computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCode);
				
	}


}
