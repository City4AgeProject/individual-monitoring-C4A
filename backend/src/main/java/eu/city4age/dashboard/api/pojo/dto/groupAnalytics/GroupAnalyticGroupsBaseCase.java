package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.ArrayList;
import java.util.List;

public class GroupAnalyticGroupsBaseCase extends ArrayList<Object> implements GroupAnalyticGroups {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8227028832984247457L;

	private String name;
	
	private List<String> groups = new ArrayList<String>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	@Override
	public void setGroups(List<Object> groups) {
		for (Object obj : groups) this.groups.add(obj.toString());
	}

	
	
	
}
