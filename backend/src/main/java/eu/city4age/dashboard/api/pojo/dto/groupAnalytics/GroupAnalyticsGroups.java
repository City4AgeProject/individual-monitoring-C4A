package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class GroupAnalyticsGroups {
	
	private String name;
	
	private List<GroupAnalyticsGroups> groups;
	
	private TreeSet<Date> dates;
	
	private List<String> lastGroup;

	/**
	 * 
	 */
	public GroupAnalyticsGroups() {
	}

	/**
	 * @param name
	 * @param groups
	 */
	public GroupAnalyticsGroups(String name, List<GroupAnalyticsGroups> groups) {
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
	public List<GroupAnalyticsGroups> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<GroupAnalyticsGroups> groups) {
		this.groups = groups;
	}

	/**
	 * @return the dates
	 */
	public TreeSet<Date> getDates() {
		return dates;
	}

	/**
	 * @param dates the dates to set
	 */
	public void setDates(TreeSet<Date> dates) {
		this.dates = dates;
	}

	/**
	 * @return the lastGroup
	 */
	public List<String> getLastGroup() {
		return lastGroup;
	}

	/**
	 * @param lastGroup the lastGroup to set
	 */
	public void setLastGroup(List<String> lastGroup) {
		this.lastGroup = lastGroup;
	}

}
