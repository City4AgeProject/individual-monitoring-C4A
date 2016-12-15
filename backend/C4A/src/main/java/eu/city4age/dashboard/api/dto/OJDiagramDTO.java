package eu.city4age.dashboard.api.dto;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.domain.DataIdValue;
import eu.city4age.dashboard.api.domain.Serie;
import eu.city4age.dashboard.api.model.TimeInterval;

public class OJDiagramDTO {
	
	List<DataIdValue> groups = new ArrayList<DataIdValue>();
	
	Serie serie;
	
	List<TimeInterval> tis = new ArrayList<TimeInterval>();

	public List<DataIdValue> getGroups() {
		return groups;
	}

	public void setGroups(List<DataIdValue> groups) {
		this.groups = groups;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public List<TimeInterval> getTis() {
		return tis;
	}

	public void setTis(List<TimeInterval> tis) {
		this.tis = tis;
	}

}
