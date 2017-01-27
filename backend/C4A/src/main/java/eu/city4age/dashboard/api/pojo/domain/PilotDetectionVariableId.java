package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

@Embeddable
public class PilotDetectionVariableId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6498525084713067182L;

	@GeneratedValue
	@Column(name="pilot_id")
	private int pilotId;

	@GeneratedValue
	@Column(name="detection_variable_id")
	private short detectionVariableId;

	public PilotDetectionVariableId() {
	}

	public PilotDetectionVariableId(int pilotId, short detectionVariableId) {
		this.pilotId = pilotId;
		this.detectionVariableId = detectionVariableId;
	}

	public int getPilotId() {
		return this.pilotId;
	}

	public void setPilotId(int pilotId) {
		this.pilotId = pilotId;
	}

	public short getDetectionVariableId() {
		return this.detectionVariableId;
	}

	public void setDetectionVariableId(short detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PilotDetectionVariableId))
			return false;
		PilotDetectionVariableId castOther = (PilotDetectionVariableId) other;

		return (this.getPilotId() == castOther.getPilotId())
				&& (this.getDetectionVariableId() == castOther.getDetectionVariableId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPilotId();
		result = 37 * result + this.getDetectionVariableId();
		return result;
	}

}
