package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresData {
	
	private List<ClusteredMeasuresGroups> groups;
	
	private List<ClusteredMeasuresSeries> series;
	
	private List<ClusteredMeasuresLegend> legend;

	/**
	 * 
	 */
	public ClusteredMeasuresData() {
	}

	/**
	 * @param groups
	 * @param series
	 * @param legend
	 */
	public ClusteredMeasuresData(List<ClusteredMeasuresGroups> groups, List<ClusteredMeasuresSeries> series,
			List<ClusteredMeasuresLegend> legend) {
		this.groups = groups;
		this.series = series;
		this.legend = legend;
	}

	/**
	 * @return the groups
	 */
	public List<ClusteredMeasuresGroups> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<ClusteredMeasuresGroups> groups) {
		this.groups = groups;
	}

	/**
	 * @return the series
	 */
	public List<ClusteredMeasuresSeries> getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(List<ClusteredMeasuresSeries> series) {
		this.series = series;
	}

	/**
	 * @return the items
	 */
	public List<ClusteredMeasuresLegend> getLegend() {
		return legend;
	}

	/**
	 * @param items the items to set
	 */
	public void setLegend(List<ClusteredMeasuresLegend> legend) {
		this.legend = legend;
	}
}
