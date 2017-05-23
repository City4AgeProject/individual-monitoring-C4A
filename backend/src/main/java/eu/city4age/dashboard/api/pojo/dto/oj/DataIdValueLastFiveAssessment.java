package eu.city4age.dashboard.api.pojo.dto.oj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.dto.Last5Assessment;
import eu.city4age.dashboard.api.pojo.json.view.View;

public class DataIdValueLastFiveAssessment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8783099818555295641L;

	@JsonView(View.TimeIntervalView.class)
	private Long id;

	@JsonView(View.TimeIntervalView.class)
	private String value;
	
	@JsonView(View.TimeIntervalView.class)
	private List<Last5Assessment> assessmentObjects = new ArrayList<Last5Assessment>();
	

	public DataIdValueLastFiveAssessment() {
	}

	public DataIdValueLastFiveAssessment(Long id, String value, List<Last5Assessment> assessmentObjects) {
		this.id = id;
		this.value = value;
		this.assessmentObjects = assessmentObjects;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Last5Assessment> getAssessmentObjects() {
		return assessmentObjects;
	}

	public void setAssessmentObjects(List<Last5Assessment> assessmentObjects) {
		this.assessmentObjects = assessmentObjects;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataIdValueLastFiveAssessment other = (DataIdValueLastFiveAssessment) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
