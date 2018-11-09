package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.ArrayList;
import java.util.List;

public class GroupAnalyticGroupsRecursive extends ArrayList<Object> implements GroupAnalyticGroups {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8095261755198281478L;

	private String name;
	
	private List<GroupAnalyticGroups> groups = new ArrayList<GroupAnalyticGroups>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the groups
	 */
	public List<GroupAnalyticGroups> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	@Override
	public void setGroups(List<Object> groups) {
		for (Object obj : groups) this.groups.add((GroupAnalyticGroups) obj);
	}

}
