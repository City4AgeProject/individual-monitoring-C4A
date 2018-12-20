package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresSeries {
	
	private String name;
	
	private List<ClusteredMeasuresItems> items;
	
	private String markerDisplayed;
	
	private String color;
	
	private String lineWidth;
	
	private String lineStyle;
	
	private String displayInLegend;

	/**
	 * 
	 */
	public ClusteredMeasuresSeries() {
	}

	/**
	 * @param name
	 * @param items
	 * @param markerDisplayed
	 * @param color
	 * @param lineWidth
	 * @param lineStyle
	 * @param displayInLegend
	 */
	public ClusteredMeasuresSeries(String name, List<ClusteredMeasuresItems> items, String markerDisplayed,
			String color, String lineWidth, String lineStyle, String displayInLegend) {
		this.name = name;
		this.items = items;
		this.markerDisplayed = markerDisplayed;
		this.color = color;
		this.lineWidth = lineWidth;
		this.lineStyle = lineStyle;
		this.displayInLegend = displayInLegend;
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
	 * @return the items
	 */
	public List<ClusteredMeasuresItems> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<ClusteredMeasuresItems> items) {
		this.items = items;
	}

	/**
	 * @return the markerDisplayed
	 */
	public String getMarkerDisplayed() {
		return markerDisplayed;
	}

	/**
	 * @param markerDisplayed the markerDisplayed to set
	 */
	public void setMarkerDisplayed(String markerDisplayed) {
		this.markerDisplayed = markerDisplayed;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the lineWidth
	 */
	public String getLineWidth() {
		return lineWidth;
	}

	/**
	 * @param lineWidth the lineWidth to set
	 */
	public void setLineWidth(String lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * @return the lineStyle
	 */
	public String getLineStyle() {
		return lineStyle;
	}

	/**
	 * @param lineStyle the lineStyle to set
	 */
	public void setLineStyle(String lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * @return the displayInLegend
	 */
	public String getDisplayInLegend() {
		return displayInLegend;
	}

	/**
	 * @param displayInLegend the displayInLegend to set
	 */
	public void setDisplayInLegend(String displayInLegend) {
		this.displayInLegend = displayInLegend;
	}
}
