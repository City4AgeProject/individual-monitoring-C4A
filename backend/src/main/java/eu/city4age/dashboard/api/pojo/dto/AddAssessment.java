package eu.city4age.dashboard.api.pojo.dto;

import eu.city4age.dashboard.api.pojo.domain.UserInRole;

public class AddAssessment {
	
	private Long id;
	
	private UserInRole userInRole;
	
	private String assessmentComment;
	
	private Character riskStatus;
	
	private Character dataValidity;
	
	private String userInSystemDisplayName;
	
	private String dateAndTime;
	
	private String riskStatusDesc;
	
	private String dataValidityDesc;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserInRole getUserInRole() {
		return userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public String getAssessmentComment() {
		return assessmentComment;
	}

	public void setAssessmentComment(String assessmentComment) {
		this.assessmentComment = assessmentComment;
	}

	public Character getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Character riskStatus) {
		this.riskStatus = riskStatus;
	}

	public Character getDataValidity() {
		return dataValidity;
	}

	public void setDataValidity(Character dataValidity) {
		this.dataValidity = dataValidity;
	}

	public String getUserInSystemDisplayName() {
		return userInSystemDisplayName;
	}

	public void setUserInSystemDisplayName(String userInSystemDisplayName) {
		this.userInSystemDisplayName = userInSystemDisplayName;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getRiskStatusDesc() {
		return riskStatusDesc;
	}

	public void setRiskStatusDesc(String riskStatusDesc) {
		this.riskStatusDesc = riskStatusDesc;
	}

	public String getDataValidityDesc() {
		return dataValidityDesc;
	}

	public void setDataValidityDesc(String dataValidityDesc) {
		this.dataValidityDesc = dataValidityDesc;
	}
	
	
	

}
