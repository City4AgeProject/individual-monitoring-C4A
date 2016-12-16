package eu.city4age.dashboard.api.dao;

import java.util.List;

import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

public interface ExternalDAO extends BaseDAO {

	List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(List<String> parentFactors);
	
	List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(String parentFactor);

	List<GeriatricFactorValue> getGeriatricFactorValueForDetectionVariableId(Long dvId, Long cdId);

	String getUserInSystemUsername(Long gefId);

	String getParentGroupName(Long gefId);
	
	List<FrailtyStatusTimeline> getFrailtyStatus(List<Long> timeintervalIds, Long uId);

}
