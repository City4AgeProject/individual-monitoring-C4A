package eu.city4age.dashboard.api.pojo.dto.oj;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

public class Serie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -398226081290950273L;

	@JsonView(View.TimeIntervalView.class)
	String name;

	@JsonView(View.TimeIntervalView.class)
	Set<DataIdValueLastFiveAssessment> items;

	@JsonView(View.TimeIntervalView.class)
	String source;

	@JsonView(View.TimeIntervalView.class)
	String imgSize = "20px";

	@JsonView(View.TimeIntervalView.class)
	Integer markerSize = 16;

	@JsonView(View.TimeIntervalView.class)
	String markerDisplayed = "on";

	@JsonView(View.TimeIntervalView.class)
	String lineType;

	public Serie(String name, Set<DataIdValueLastFiveAssessment> items, String source, String imgSize, Integer markerSize,
			String markerDisplayed, String lineType) {
		this.name = name;
		this.items = items;
		this.source = source;
		this.imgSize = imgSize;
		this.markerSize = markerSize;
		this.markerDisplayed = markerDisplayed;
		this.lineType = lineType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DataIdValueLastFiveAssessment> getItems() {
		return items;
	}

	public void setItems(Set<DataIdValueLastFiveAssessment> items) {
		this.items = items;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
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
