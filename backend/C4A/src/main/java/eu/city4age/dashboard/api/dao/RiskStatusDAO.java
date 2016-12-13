package eu.city4age.dashboard.api.dao;

import java.util.List;

import eu.city4age.dashboard.api.model.CdRiskStatus;

public interface RiskStatusDAO extends BaseDAO {
	
	List<CdRiskStatus> getAllRiskStatus();

}
