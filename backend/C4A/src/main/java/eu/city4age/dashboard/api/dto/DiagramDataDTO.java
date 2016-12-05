package eu.city4age.dashboard.api.dto;

import java.util.List;

import eu.city4age.dashboard.api.model.GeriatricFactorValue;

public class DiagramDataDTO {
	
	List<String> gefLabels;
	
	List<String> monthLabels;
	
	List<GeriatricFactorValue> gefData;
	
	

	public List<String> getGefLabels() {
		return gefLabels;
	}

	public void setGefLabels(List<String> gefLabels) {
		this.gefLabels = gefLabels;
	}

	public List<String> getMonthLabels() {
		return monthLabels;
	}

	public void setMonthLabels(List<String> monthLabels) {
		this.monthLabels = monthLabels;
	}

	public List<GeriatricFactorValue> getGefData() {
		return gefData;
	}

	public void setGefData(List<GeriatricFactorValue> gefData) {
		this.gefData = gefData;
	}
	

}
