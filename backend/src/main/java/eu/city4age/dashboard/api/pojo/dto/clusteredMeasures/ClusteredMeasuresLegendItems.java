package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresLegendItems {
	
	private String text;
	
	private String markerShape;
	
	private String markerSize;
	
	private String color;
	
	private String drilling;
	
	private List<String> categories;

	/**
	 * 
	 */
	public ClusteredMeasuresLegendItems() {
	}

	/**
	 * @param text
	 * @param markerShape
	 * @param markerSize
	 * @param color
	 * @param drilling
	 * @param categories
	 */
	public ClusteredMeasuresLegendItems(String text, String markerShape, String markerSize, String color,
			String drilling, List<String> categories) {
		this.text = text;
		this.markerShape = markerShape;
		this.markerSize = markerSize;
		this.color = color;
		this.drilling = drilling;
		this.categories = categories;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the markerShape
	 */
	public String getMarkerShape() {
		return markerShape;
	}

	/**
	 * @param markerShape the markerShape to set
	 */
	public void setMarkerShape(String markerShape) {
		this.markerShape = markerShape;
	}

	/**
	 * @return the markerSize
	 */
	public String getMarkerSize() {
		return markerSize;
	}

	/**
	 * @param markerSize the markerSize to set
	 */
	public void setMarkerSize(String markerSize) {
		this.markerSize = markerSize;
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
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @return the drilling
	 */
	public String getDrilling() {
		return drilling;
	}

	/**
	 * @param drilling the drilling to set
	 */
	public void setDrilling(String drilling) {
		this.drilling = drilling;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
