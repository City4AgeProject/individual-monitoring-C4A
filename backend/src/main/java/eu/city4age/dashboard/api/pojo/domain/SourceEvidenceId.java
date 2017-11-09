package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

@Embeddable
public class SourceEvidenceId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5908214341407149708L;

	@GeneratedValue
	@Column(name="value_id")
	private int valueId;
	
	@GeneratedValue
	@Column(name="detection_variable_type")
	private String detectionVariableType;

	public SourceEvidenceId() {
	}

	public SourceEvidenceId(int valueId, String detectionVariableType) {
		this.valueId = valueId;
		this.detectionVariableType = detectionVariableType;
	}

	public int getValueId() {
		return valueId;
	}

	public void setValueId(int valueId) {
		this.valueId = valueId;
	}

	public String getDetectionVariableType() {
		return detectionVariableType;
	}

	public void setDetectionVariableType(String detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SourceEvidenceId))
			return false;
		SourceEvidenceId castOther = (SourceEvidenceId) other;

		return (this.getValueId() == castOther.getValueId())
				&& ((this.getDetectionVariableType() == castOther.getDetectionVariableType()));
	}

	public int hashCode() {
	    StringBuilder builder = new StringBuilder();
	    builder.append(String.valueOf(this.getValueId()));
	    builder.append(this.getDetectionVariableType());
	    return builder.toString().hashCode();
	}

}
