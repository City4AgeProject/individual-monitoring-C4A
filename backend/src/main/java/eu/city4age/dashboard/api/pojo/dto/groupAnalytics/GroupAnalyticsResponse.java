package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

import eu.city4age.dashboard.api.pojo.dto.Response;

public class GroupAnalyticsResponse implements Response {

	private List<GroupAnalyticsSeries> series;
	
	private List<GroupAnalyticsGroups> groups;
	
	private List<String> lastGroup;
	
	public GroupAnalyticsResponse() {
		
	}

	public GroupAnalyticsResponse(List<GroupAnalyticsSeries> series, List<GroupAnalyticsGroups> groups) {
		super();
		this.series = series;
		this.groups = groups;
	}

	/**
	 * @return the series
	 */
	public List<GroupAnalyticsSeries> getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(List<GroupAnalyticsSeries> series) {
		this.series = series;
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
