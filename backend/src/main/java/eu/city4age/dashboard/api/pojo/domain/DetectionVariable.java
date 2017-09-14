package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "cd_detection_variable")
public class DetectionVariable extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -821517967630729430L;
	
	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "detection_variable_name")
	private String detectionVariableName;

	
	@Column(name = "valid_from")
	private Date validFrom;

	
	@Column(name = "valid_to")
	private Date validTo;
	
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "derived_detection_variable_id", referencedColumnName = "id")
	private DetectionVariable derivedDetectionVariable;

	@JsonIgnore	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "detection_variable_type")
	private DetectionVariableType detectionVariableType;
	
	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "default_typical_period")
	private String  defaultTypicalPeriod;
	
	@OneToMany(mappedBy = "detectionVariable", fetch = FetchType.LAZY)
	private Set<PilotDetectionVariable> pilotDetectionVariable = new HashSet<PilotDetectionVariable>();

	@OneToMany(mappedBy = "detectionVariable", fetch = FetchType.LAZY)
	private Set<VariationMeasureValue> variationMeasureValue = new HashSet<VariationMeasureValue>();

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
	public String getDefaultTypicalPeriod() {
		return defaultTypicalPeriod;
	}

	public void setDefaultTypicalPeriod(String defaultTypicalPeriod) {
		this.defaultTypicalPeriod = defaultTypicalPeriod;
	}
	//NEW CODE
			public Set<PilotDetectionVariable> getPilotDetectionVariable() {
				return pilotDetectionVariable;
			}

			public void setPilotDetectionVariable(Set<PilotDetectionVariable> pilotDetectionVariable) {
				this.pilotDetectionVariable = pilotDetectionVariable;
			}
			public Set<VariationMeasureValue> getVariationMeasureValue() {
				return variationMeasureValue;
			}

			public void setVariationMeasureValue(Set<VariationMeasureValue> variationMeasureValue) {
				this.variationMeasureValue = variationMeasureValue;
			}

}
