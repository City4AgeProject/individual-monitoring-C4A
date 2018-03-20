package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewGefCalculatedInterpolatedPredictedValuesKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7202639888035678614L;
	
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;
	
	@Column(name = "time_interval_id")
	private Long timeIntervalId;
	
	@Column(name = "gef_type_id")
	private Long detectionVariableId;
	
	@Column(name = "data_type")
	private String dataType;
	

	public ViewGefCalculatedInterpolatedPredictedValuesKey() {
		
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public Long getTimeIntervalId() {
		return timeIntervalId;
	}

	public void setTimeIntervalId(Long timeIntervalId) {
		this.timeIntervalId = timeIntervalId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}
	
}
