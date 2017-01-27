package eu.city4age.dashboard.api.pojo.dto;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;

public class DiagramData {

	List<String> gefLabels;

	List<String> monthLabels;

	List<GeriatricFactorValue> gefs = new ArrayList<GeriatricFactorValue>();

	List<Object[]> tis = new ArrayList<Object[]>();

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

	public List<Object[]> getTis() {
		return tis;
	}

	public void setTis(List<Object[]> tis) {
		this.tis = tis;
	}

}
