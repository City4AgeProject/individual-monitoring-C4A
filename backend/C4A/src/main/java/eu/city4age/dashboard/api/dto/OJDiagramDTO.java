package eu.city4age.dashboard.api.dto;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.domain.DataIdValue;
import eu.city4age.dashboard.api.domain.Serie;

public class OJDiagramDTO {
	
	List<DataIdValue> groups = new ArrayList<DataIdValue>();
	
	List<Serie> series = new ArrayList<Serie>();



	public List<DataIdValue> getGroups() {
		return groups;
	}

	public void setGroups(List<DataIdValue> groups) {
		this.groups = groups;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}


}
