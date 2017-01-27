package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cd_detection_variable_type")
public class DetectionVariableType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2367967721511008325L;

	@Id
	@Column(name="detection_variable_type")
	private String detectionVariableType;
	
	@Column(name="detection_variable_type_description")
	private String detectionVariableTypeDescription;

	public DetectionVariableType() {
	}

	public DetectionVariableType(String detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	public DetectionVariableType(String detectionVariableType, String detectionVariableTypeDescription) {
		this.detectionVariableType = detectionVariableType;
		this.detectionVariableTypeDescription = detectionVariableTypeDescription;
	}

	public String getDetectionVariableType() {
		return this.detectionVariableType;
	}

	public void setDetectionVariableType(String detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	public String getDetectionVariableTypeDescription() {
		return this.detectionVariableTypeDescription;
	}

	public void setDetectionVariableTypeDescription(String detectionVariableTypeDescription) {
		this.detectionVariableTypeDescription = detectionVariableTypeDescription;
	}

}
