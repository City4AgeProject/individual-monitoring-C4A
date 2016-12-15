package eu.city4age.dashboard.api.domain;

import java.io.Serializable;
import java.util.List;

public class Serie implements Serializable {
	
	String name;
	
	String color;
	
	String source;
	
	List<DataIdValue> items;
	
	String lineType;
	
	String markerDisplayed;
	
	Integer markerSize;


	public Serie(String name, String color, String source, List<DataIdValue> items, String lineType,
			String markerDisplayed, Integer markerSize) {
		this.name = name;
		this.color = color;
		this.source = source;
		this.items = items;
		this.lineType = lineType;
		this.markerDisplayed = markerDisplayed;
		this.markerSize = markerSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataIdValue> getItems() {
		return items;
	}

	public void setItems(List<DataIdValue> items) {
		this.items = items;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getMarkerDisplayed() {
		return markerDisplayed;
	}

	public void setMarkerDisplayed(String markerDisplayed) {
		this.markerDisplayed = markerDisplayed;
	}

	public Integer getMarkerSize() {
		return markerSize;
	}

	public void setMarkerSize(Integer markerSize) {
		this.markerSize = markerSize;
	}

}
