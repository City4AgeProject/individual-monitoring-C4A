package eu.city4age.dashboard.api.pojo.dto;

import java.io.Serializable;
import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;

public class AssessmentJson implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7708891445622455265L;

	Assessment assessment;
	
	List<ViewGefCalculatedInterpolatedPredictedValues> gfvs;

	public AssessmentJson(Assessment assessment, List<ViewGefCalculatedInterpolatedPredictedValues> gefs) {
		this.assessment = assessment;
		this.gfvs = gefs;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public List<ViewGefCalculatedInterpolatedPredictedValues> getGfvs() {
		return gfvs;
	}

	public void setGfvs(List<ViewGefCalculatedInterpolatedPredictedValues> gfvs) {
		this.gfvs = gfvs;
	}

}
