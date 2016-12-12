package eu.city4age.dashboard.api.dto;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

public class DiagramDataDTO {

	
	List<String> gefLabels;
	
	List<String> monthLabels;
	
	List<GeriatricFactorValue> gefs = new ArrayList<GeriatricFactorValue>();
	
	List<TimeInterval> tis = new ArrayList<TimeInterval>();
	

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

	public List<GeriatricFactorValue> getGefs() {
		return gefs;
	}

	public void setGefs(List<GeriatricFactorValue> gefs) {
		this.gefs = gefs;
	}

	public List<TimeInterval> getTis() {
		return tis;
	}

	public void setTis(List<TimeInterval> tis) {
		this.tis = tis;
	}

}
