package eu.city4age.dashboard.api.dao;

import java.util.List;

public interface DetectionVariableDAO extends BaseDAO {
	
	List<String> getAllDetectionVariableNamesForParentId(Short parentId);

}
