package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

public interface GroupAnalyticGroups extends List<Object> {
	
	void setName(String name);
	
	void setGroups(List<Object> groups);
	
}
