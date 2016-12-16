package eu.city4age.dashboard.api.external;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.domain.DataIdValue;

public class FrailtyStatusDTO {
	
	List<DataIdValue> months = new ArrayList<DataIdValue>();

	List<Serie> series = new ArrayList<Serie>();

	
	public List<DataIdValue> getMonths() {
		return months;
	}

	public void setMonths(List<DataIdValue> months) {
		this.months = months;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

}
