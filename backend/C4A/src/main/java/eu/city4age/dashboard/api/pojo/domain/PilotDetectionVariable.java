package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cd_pilot_detection_variable")
public class PilotDetectionVariable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7337284221184594172L;

	@EmbeddedId
	private PilotDetectionVariableId cdPilotDetectionVariableId;

	@Column(name="detection_variable_id",insertable=false,updatable=false)
	private DetectionVariable cdDetectionVariable;
	
    @ManyToOne
    @JoinColumn(name="pilot_id",insertable=false,updatable=false)
	private Pilot pilot;

    @Column(name="detection_variable_description_formula")
	private String detectionVariableDescriptionFormula;

	public PilotDetectionVariable() {
	}

	public PilotDetectionVariable(DetectionVariable cdDetectionVariable, Pilot pilot,
			String detectionVariableDescriptionFormula) {
		this.cdDetectionVariable = cdDetectionVariable;
		this.pilot = pilot;
		this.detectionVariableDescriptionFormula = detectionVariableDescriptionFormula;
	}

	public PilotDetectionVariableId getCdPilotDetectionVariableId() {
		return cdPilotDetectionVariableId;
	}

	public void setCdPilotDetectionVariableId(PilotDetectionVariableId cdPilotDetectionVariableId) {
		this.cdPilotDetectionVariableId = cdPilotDetectionVariableId;
	}

	public DetectionVariable getCdDetectionVariable() {
		return this.cdDetectionVariable;
	}

	public void setCdDetectionVariable(DetectionVariable cdDetectionVariable) {
		this.cdDetectionVariable = cdDetectionVariable;
	}

	public Pilot getPilot() {
		return this.pilot;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}

	public String getDetectionVariableDescriptionFormula() {
		return this.detectionVariableDescriptionFormula;
	}

	public void setDetectionVariableDescriptionFormula(String detectionVariableDescriptionFormula) {
		this.detectionVariableDescriptionFormula = detectionVariableDescriptionFormula;
	}

}
