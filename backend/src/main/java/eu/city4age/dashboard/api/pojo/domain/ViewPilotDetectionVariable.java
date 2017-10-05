package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_detection_variable_derivation_per_user_in_role")
@Immutable
public class ViewPilotDetectionVariable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6447327289295618307L;

	@EmbeddedId
	private ViewPilotDetectionVariableKey id;

	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "mpdv_id")
	private Long mpdvId;
	
	@Column(name = "detection_variable_name")
	private String detectionVariableName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "detection_variable_type")
	private DetectionVariableType detectionVariableType;
	
	@Column(name = "derived_detection_variable_name")
	private String derivedDetectionVariableName;
	
	@Column(name = "derived_detection_variable_type")
	private String derivedDetectionVariableType;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	@Column(name = "derivation_function_formula")
	private String formula;

	@Column(name = "detection_variable_default_period")
	private String defaultTypicalPeriod;
	
	@Column(name = "detection_variable_id", insertable = false, updatable = false, nullable = false)
	private Long detectionVariableId;
	
	/*@OneToMany(mappedBy="vpdv", fetch=FetchType.LAZY)
	private Set<VariationMeasureValue> variationMeasureValues = new HashSet<VariationMeasureValue>();*/
	

	public void setId(ViewPilotDetectionVariableKey id) {
		this.id = id;
	}
	
	public ViewPilotDetectionVariableKey getId() {
		return id;
	}
	
	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}
	
	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}
	
	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	public DetectionVariableType getDetectionVariableType() {
		return detectionVariableType;
	}

	public void setDetectionVariableType(DetectionVariableType detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	public String getDerivedDetectionVariableType() {
		return this.formula;
	}

	public void setDerivedDetectionVariableType(String derivedDetectionVariableType) {
		this.derivedDetectionVariableType = derivedDetectionVariableType;
	}
	
	public String getDetectionVariableName() {
		return this.detectionVariableName;
	}

	public void setDetectionVariableName(String detectionVariableName) {
		this.detectionVariableName = detectionVariableName;
	}
	
	public String getDerivedDetectionVariableName() {
		return this.derivedDetectionVariableName;
	}

	public void setDerivedDetectionVariableName(String derivedDetectionVariableName) {
		this.derivedDetectionVariableName = derivedDetectionVariableName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public Long getMpdvId() {
		return mpdvId;
	}

	public void setMpdvId(Long mpdvId) {
		this.mpdvId = mpdvId;
	}

	public String getDefaultTypicalPeriod() {
		return defaultTypicalPeriod;
	}

	public void setDefaultTypicalPeriod(String defaultTypicalPeriod) {
		this.defaultTypicalPeriod = defaultTypicalPeriod;
	}

	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}

	/*public Set<VariationMeasureValue> getVariationMeasureValues() {
		return variationMeasureValues;
	}

	public void setVariationMeasureValues(Set<VariationMeasureValue> variationMeasureValues) {
		this.variationMeasureValues = variationMeasureValues;
	}*/


}
