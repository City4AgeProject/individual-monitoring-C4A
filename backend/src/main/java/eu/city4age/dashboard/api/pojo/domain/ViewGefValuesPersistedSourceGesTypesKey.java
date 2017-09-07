package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewGefValuesPersistedSourceGesTypesKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7357062828329651354L;

	@Column(name = "pilot_code")
	private String pilotCode;
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;
	
	@Column(name = "detection_variable_id")
	private Long gesId;
	
	@Column(name = "derived_detection_variable_id")
	private Long gefId;
	
	@Column(name = "id")
	private Long id;

	public ViewGefValuesPersistedSourceGesTypesKey() {
		
	}
	
	public ViewGefValuesPersistedSourceGesTypesKey(String pilotCode, Long userInRoleId, Long gesId, Long gefId, Long id) {
		this.pilotCode = pilotCode;
		this.userInRoleId = userInRoleId;
		this.gesId = gesId;
		this.gefId = gefId;
		this.id = id;
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
		return gesId;
	}

	public void setDetectionVariableId(Long detectionVariableId) {
		this.gesId = detectionVariableId;
	}
	
	public Long getDerivedDetectionVariableId() {
		return gefId;
	}

	public void setDerivedDetectionVariableId(Long derivedDetectionVariableId) {
		this.gefId = derivedDetectionVariableId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
