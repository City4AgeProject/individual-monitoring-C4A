package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresLegend {
	
	private List<ClusteredMeasuresLegendItems> items;

	/**
	 * 
	 */
	public ClusteredMeasuresLegend() {
	}

	/**
	 * @param items
	 */
	public ClusteredMeasuresLegend(List<ClusteredMeasuresLegendItems> items) {
		this.items = items;
	}

	/**
	 * @return the items
	 */
	public List<ClusteredMeasuresLegendItems> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<ClusteredMeasuresLegendItems> items) {
		this.items = items;
	}

}
