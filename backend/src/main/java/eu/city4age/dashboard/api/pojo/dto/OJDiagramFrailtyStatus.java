package eu.city4age.dashboard.api.pojo.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie;

public class OJDiagramFrailtyStatus {

	Set<DataIdValue> months = new HashSet<DataIdValue>();

	List<Serie> series = new ArrayList<Serie>();

	public Set<DataIdValue> getMonths() {
		return months;
	}

	public void setMonths(Set<DataIdValue> months) {
		this.months = months;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

}
