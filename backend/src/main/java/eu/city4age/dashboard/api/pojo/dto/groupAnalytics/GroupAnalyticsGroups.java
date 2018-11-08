package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

public class GroupAnalyticsGroups {
	
	private String name;
	
	private Object groups;

	/**
	 * 
	 */
	public GroupAnalyticsGroups() {
	}

	/**
	 * @param name
	 * @param groups
	 */
	public GroupAnalyticsGroups(String name, Object groups) {
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
	public Object getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Object groups) {
		this.groups = groups;
	}
}
