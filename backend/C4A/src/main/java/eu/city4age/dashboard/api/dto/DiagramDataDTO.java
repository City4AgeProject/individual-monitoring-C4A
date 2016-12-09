package eu.city4age.dashboard.api.dto;

import java.util.List;

public class DiagramDataDTO {

	
	List<String> gefLabels;
	
	List<String> monthLabels;
	
	List<Object[]> data;
	
	

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

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
		this.data = data;
	}

}
