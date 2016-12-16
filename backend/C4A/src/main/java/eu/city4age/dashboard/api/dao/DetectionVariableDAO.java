package eu.city4age.dashboard.api.dao;

import java.util.List;

import eu.city4age.dashboard.api.model.CdDetectionVariable;

public interface DetectionVariableDAO extends BaseDAO {
	
	List<String> getAllDetectionVariableNamesForParentId(Long parentId);
	
	List<CdDetectionVariable> getAllDetectionVariableNames(Long parentId);

}
