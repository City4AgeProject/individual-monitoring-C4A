package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

public class AssessedGefValueSetId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8125604156214807737L;

	private int gefValueId;

	private int assessmentId;
	
	public AssessedGefValueSetId() {
	}

	public AssessedGefValueSetId(AssessedGefValueSetIdBuilder builder) {
		this.gefValueId = builder.gefValueId;
		this.assessmentId = builder.assessmentId;
	}

	public AssessedGefValueSetId(int gefValueId, int assessmentId) {
		this.gefValueId = gefValueId;
		this.assessmentId = assessmentId;
	}

	public int getGefValueId() {
		return gefValueId;
	}

	public void setGefValueId(int gefValueId) {
		this.gefValueId = gefValueId;
	}

	public int getAssessmentId() {
		return this.assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AssessedGefValueSetId))
			return false;
		AssessedGefValueSetId castOther = (AssessedGefValueSetId) other;

		return (this.getGefValueId() == castOther.getGefValueId())
				&& ((this.getGefValueId() == castOther.getGefValueId())
				&& (this.getAssessmentId() == castOther.getAssessmentId()));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getGefValueId();
		result = 37 * result + this.getAssessmentId();
		return result;
	}

	public static class AssessedGefValueSetIdBuilder {
		private int gefValueId;
		private int assessmentId;
		
		public AssessedGefValueSetIdBuilder id(int gefValueId, int assessmentId) {
			this.gefValueId = gefValueId;
			this.assessmentId = assessmentId;
			return this;		
		}
		
		public AssessedGefValueSetId build() {
			return new AssessedGefValueSetId();
		}

	}

}
