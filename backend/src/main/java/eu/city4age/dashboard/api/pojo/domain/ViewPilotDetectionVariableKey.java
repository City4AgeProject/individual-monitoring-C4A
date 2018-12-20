package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewPilotDetectionVariableKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7357062828329651354L;

	@Column(name = "pilot_code")
	private String pilotCode;
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;
	
	@Column(name = "detection_variable_id")
	private Long detectionVariableId;
	
	@Column(name = "derived_detection_variable_id")
	private Long derivedDetectionVariableId;
	
	public ViewPilotDetectionVariableKey() {}
	
	public ViewPilotDetectionVariableKey(String pilotCode, Long userInRoleId, Long detectionVariableId, Long derivedDetectionVariableId) {
		this.pilotCode = pilotCode;
		this.userInRoleId = userInRoleId;
		this.detectionVariableId = detectionVariableId;
		this.derivedDetectionVariableId = derivedDetectionVariableId;
	}
	
	public String getPilotCode() {
		return this.pilotCode;
	}

	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}
	
	public Long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}
	
	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}
	
	public Long getDerivedDetectionVariableId() {
		return derivedDetectionVariableId;
	}

	public void setDerivedDetectionVariableId(Long derivedDetectionVariableId) {
		this.derivedDetectionVariableId = derivedDetectionVariableId;
	}
	
	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewPilotDetectionVariableKey))
			return false;
		ViewPilotDetectionVariableKey castOther = (ViewPilotDetectionVariableKey) other;

		return (this.getUserInRoleId().equals(castOther.getUserInRoleId())
				&& this.getDetectionVariableId().equals(castOther.getDetectionVariableId())
				&& this.getDerivedDetectionVariableId().equals(castOther.getDerivedDetectionVariableId()));
	}
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserInRoleId().intValue();
		result = 37 * result + this.getDetectionVariableId().intValue();
		result = 37 * result + this.getDerivedDetectionVariableId().intValue();
		return result;
	}
}
