package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresItems {
	
	private String id;
	
	private String value;
	
	private String markerShape;
	
	private String markerSize;
	
	private String color;
	
	private String shortDesc;
	
	private List<String> categories;

	/**
	 * 
	 */
	public ClusteredMeasuresItems() {
	}

	/**
	 * @param id
	 * @param value
	 * @param markerShape
	 * @param markerSize
	 * @param color
	 * @param shortDesc
	 * @param categories
	 */
	public ClusteredMeasuresItems(String id, String value, String markerShape, String markerSize, String color,
			String shortDesc, List<String> categories) {
		this.id = id;
		this.value = value;
		this.markerShape = markerShape;
		this.markerSize = markerSize;
		this.color = color;
		this.shortDesc = shortDesc;
		this.categories = categories;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}

	/**
	 * @param shortDesc the shortDesc to set
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
