package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cd_detection_variable")
public class DetectionVariable extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -821517967630729430L;

	@Column(name="detection_variable_name")
	private String detectionVariableName;
	
	@JsonIgnore
	@Column(name="valid_from")
	private Date validFrom;
	
	@JsonIgnore
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="derivation_weight")
	private BigDecimal derivationWeight;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="derived_detection_variable_id", referencedColumnName="id")
	private DetectionVariable derivedDetectionVariable;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="detection_variable_type")
	private  DetectionVariableType detectionVariableType;

	public DetectionVariable() {
	}

	public DetectionVariable(String detectionVariableName, DetectionVariableType detectionVariableType, Date validFrom,
			Date validTo, BigDecimal derivationWeight, DetectionVariable derivedDetectionVariable) {
		this.detectionVariableName = detectionVariableName;
		this.detectionVariableType = detectionVariableType;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.derivedDetectionVariable = derivedDetectionVariable;
	}

	public String getDetectionVariableName() {
		return this.detectionVariableName;
	}

	public void setDetectionVariableName(String detectionVariableName) {
		this.detectionVariableName = detectionVariableName;
	}

	public DetectionVariableType getDetectionVariableType() {
		return detectionVariableType;
	}

	public void setDetectionVariableType(DetectionVariableType detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public DetectionVariable getDerivedDetectionVariable() {
		return derivedDetectionVariable;
	}

	public void setDerivedDetectionVariable(DetectionVariable derivedDetectionVariable) {
		this.derivedDetectionVariable = derivedDetectionVariable;
	}

	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}

	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}
	
}
