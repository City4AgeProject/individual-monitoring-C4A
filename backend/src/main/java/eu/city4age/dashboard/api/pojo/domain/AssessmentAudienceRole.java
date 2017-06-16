package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="assessment_audience_role")
@IdClass(AssessmentAudienceRoleId.class)
public class AssessmentAudienceRole implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 667853055034089248L;

	@Id
	@Column(name="assessment_id",insertable=false,updatable=false)
	private int assessmentId;
	
	@Id
	@Column(name="role_id",insertable=false,updatable=false)
	private int roleId;
	
	@Column(name="assigned",nullable=false)
	private Timestamp assigned;

	public AssessmentAudienceRole() {
	}

	public AssessmentAudienceRole(AssessmentAudienceRoleBuilder builder) {
		this.assessmentId = builder.assessmentId;
		this.roleId = builder.roleId;
		this.assigned = builder.assigned;
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

	public AssessmentAudienceRole(Timestamp assigned) {
		this.assigned = assigned;
	}
	
	public Timestamp getAssigned() {
		return assigned;
	}

	public void setAssigned(Timestamp assigned) {
		this.assigned = assigned;
	}

	public static class AssessmentAudienceRoleBuilder {
		private int assessmentId = 0;
		private int roleId = 0;
		private Timestamp assigned = new Timestamp((new Date()).getTime());
		
		public AssessmentAudienceRoleBuilder assessmentId(int assessmentId) {
			this.assessmentId = assessmentId;
			return this;
		}
		
		public AssessmentAudienceRoleBuilder userInRoleId(int roleId) {
			this.roleId = roleId;
			return this;
		}
		
		public AssessmentAudienceRoleBuilder assigned(Timestamp assigned) {
			this.assigned = assigned;
			return this;
		}
		
		public AssessmentAudienceRole build() {
			return new AssessmentAudienceRole(this);
		}

	}

}
