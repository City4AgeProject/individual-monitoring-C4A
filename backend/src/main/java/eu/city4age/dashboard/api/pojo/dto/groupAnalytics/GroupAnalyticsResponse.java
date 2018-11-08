package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

import eu.city4age.dashboard.api.pojo.dto.Response;

public class GroupAnalyticsResponse implements Response {

	private List<GroupAnalyticsSeries> series;
	
	private List<Object> groups;
	
	public GroupAnalyticsResponse() {
		
	}

	public GroupAnalyticsResponse(List<GroupAnalyticsSeries> series, List<Object> groups) {
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
	public List<Object> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Object> groups) {
		this.groups = groups;
	}
}
