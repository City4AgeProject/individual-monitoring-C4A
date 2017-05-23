package eu.city4age.dashboard.api.pojo.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.Serie;
import eu.city4age.dashboard.api.pojo.json.view.View;

public class OJDiagramLast5Assessment {

	@JsonView(View.TimeIntervalView.class)
	private Set<DataIdValue> groups = new HashSet<DataIdValue>();

	@JsonView(View.TimeIntervalView.class)
	private Set<Serie> series = new HashSet<Serie>();

	public Set<DataIdValue> getGroups() {
		return groups;
	}

	public void setGroups(Set<DataIdValue> groups) {
		this.groups = groups;
	}

	public Set<Serie> getSeries() {
		return series;
	}

	public void setSeries(Set<Serie> series) {
		this.series = series;
	}

}
