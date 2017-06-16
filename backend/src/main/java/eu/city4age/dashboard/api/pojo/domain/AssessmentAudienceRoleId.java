package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class AssessmentAudienceRoleId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5972065415877668751L;

	@Column(name="assessment_id")
	private int assessmentId;
	
	@Column(name="role_id")
	private int roleId;

	public AssessmentAudienceRoleId() {
	}

	public AssessmentAudienceRoleId(AssessmentAudienceRoleIdBuilder builder) {
		this.assessmentId = builder.assessmentId;
		this.roleId = builder.roleId;
	}

	public AssessmentAudienceRoleId(int assessmentId, int roleId) {
		this.assessmentId = assessmentId;
		this.roleId = roleId;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SourceEvidenceId))
			return false;
		SourceEvidenceId castOther = (SourceEvidenceId) other;

		return (this.getAssessmentId() == castOther.getGeriatricFactorId())
				&& ((this.getRoleId() == castOther.getRoleId()));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAssessmentId();
		result = 37 * result + this.getRoleId();
		return result;
	}

	public static class AssessmentAudienceRoleIdBuilder {
		private int assessmentId;
		private int roleId;

		public AssessmentAudienceRoleIdBuilder id(int assessmentId, int roleId) {
			this.assessmentId = assessmentId;
			this.roleId = roleId;
			return this;
		}

		public AssessmentAudienceRoleId build() {
			return new AssessmentAudienceRoleId();
		}

	}

}
