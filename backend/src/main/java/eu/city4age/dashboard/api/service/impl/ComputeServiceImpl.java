package eu.city4age.dashboard.api.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.service.ComputeService;
import eu.city4age.dashboard.api.service.MeasuresService;

@Component
public class ComputeServiceImpl implements ComputeService {
	
	@Autowired
	private MeasuresService measuresService;

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeAllFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode,
			List<NumericIndicatorValue> nuiList, List<GeriatricFactorValue> gfvList) throws Exception {
		
		measuresService.computeNuisFor1Month(startOfMonth, endOfMonth, pilotCode, nuiList);
		measuresService.computeGESsFor1Month(startOfMonth, endOfMonth, pilotCode, gfvList);
		measuresService.computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCode, gfvList);
		measuresService.computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCode, gfvList);
		measuresService.computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCode, gfvList);
				
	}
	
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeAllFor1UserFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir) throws Exception {
		
		measuresService.computeNuisFor1User(startOfMonth, endOfMonth, uir);
		measuresService.computeGessFor1User(startOfMonth, endOfMonth, uir);
		measuresService.computeFor1MonthFor1User(DetectionVariableType.GEF, startOfMonth, endOfMonth, uir);
		measuresService.computeFor1MonthFor1User(DetectionVariableType.GFG, startOfMonth, endOfMonth, uir);
		measuresService.computeFor1MonthFor1User(DetectionVariableType.OVL, startOfMonth, endOfMonth, uir);
	}


}
