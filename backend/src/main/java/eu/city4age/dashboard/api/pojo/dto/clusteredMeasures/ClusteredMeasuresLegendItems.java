package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresLegendItems {
	
	private String text;
	
	private String markerShape;
	
	private String markerSize;
	
	private String color;
	
	private String drilling;
	
	private String symbolType;
	
	private String source;
	
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
	 * @param symbolType
	 * @param source
	 * @param categories
	 */
	public ClusteredMeasuresLegendItems(String text, String markerShape, String markerSize, String color,
			String drilling, String symbolType, String source, List<String> categories) {
		this.text = text;
		this.markerShape = markerShape;
		this.markerSize = markerSize;
		this.color = color;
		this.drilling = drilling;
		this.symbolType = symbolType;
		this.source = source;
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
	 * @return the symbolType
	 */
	public String getSymbolType() {
		return symbolType;
	}

	/**
	 * @param symbolType the symbolType to set
	 */
	public void setSymbolType(String symbolType) {
		this.symbolType = symbolType;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
