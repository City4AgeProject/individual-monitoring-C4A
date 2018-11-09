package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

public class GroupAnalyticsGroups {
	
	private String name;
	
	// will either be a List<String> or List<GroupAnalyticsGroups>
	private List<?> groups;

	/**
	 * 
	 */
	public GroupAnalyticsGroups() {
	}

	/**
	 * @param name
	 * @param groups
	 */
	public GroupAnalyticsGroups(String name, List<?> groups) {
		this.name = name;
		this.groups = groups;
	}

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
	public List<?> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<?> groups) {
		this.groups = groups;
	}
}
