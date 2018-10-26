package eu.city4age.dashboard.api.pojo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsGraphView;

public class AnalyticsDiagramResponse {
	
	@JsonView(AnalyticsGraphView.class)
	private List<AnalyticsDiagramData> data;
	
	@JsonView(AnalyticsGraphView.class)
	private String csvFile;

	/**
	 * 
	 */
	public AnalyticsDiagramResponse() {
	}

	/**
	 * @return the data
	 */
	public List<AnalyticsDiagramData> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<AnalyticsDiagramData> data) {
		this.data = data;
	}

	/**
	 * @return the csvFile
	 */
	public String getCsvFile() {
		return csvFile;
	}

	/**
	 * @param csvFile the csvFile to set
	 */
	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

}
