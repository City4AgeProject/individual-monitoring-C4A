package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_gef_values_persisted_source_ges_types")
@Immutable
public class ViewGefValuesPersistedSourceGesTypes {
	
	@Column(name = "mpdv_id")
	private Long mpdvId;
	
	@Column(name = "detection_variable_name")
	private String gesName;
	
	@Column(name = "detection_variable_type")
	private String gesType;
	
	@Column(name = "derived_detection_variable_name")
	private String derivedGefName;
	
	@Column(name = "derived_detection_variable_type")
	private String derivedGefType;
	
	@EmbeddedId
	private ViewPilotDetectionVariableKey id;
	
	@Column(name="gef_value")
	private BigDecimal gefValue;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	@Column(name="interval_start")
	private Date intervalStart;
	
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="interval_end")
	private Date intervalEnd;

	public Long getMpdvId() {
		return mpdvId;
	}

	public void setMpdvId(Long mpdvId) {
		this.mpdvId = mpdvId;
	}

	public String getGesName() {
		return gesName;
	}

	public void setGesName(String gesName) {
		this.gesName = gesName;
	}

	public String getGesType() {
		return gesType;
	}

	public void setGesType(String gesType) {
		this.gesType = gesType;
	}

	public String getDerivedGefName() {
		return derivedGefName;
	}

	public void setDerivedGefName(String derivedGefName) {
		this.derivedGefName = derivedGefName;
	}

	public String getDerivedGefType() {
		return derivedGefType;
	}

	public void setDerivedGefType(String derivedGefType) {
		this.derivedGefType = derivedGefType;
	}

	public ViewPilotDetectionVariableKey getId() {
		return id;
	}

	public void setId(ViewPilotDetectionVariableKey id) {
		this.id = id;
	}

	public BigDecimal getGefValue() {
		return gefValue;
	}

	public void setGefValue(BigDecimal gefValue) {
		this.gefValue = gefValue;
	}

	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}

	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}

	public Date getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Date intervalStart) {
		this.intervalStart = intervalStart;
	}

	public String getTypicalPeriod() {
		return typicalPeriod;
	}

	public void setTypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}

	public Date getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(Date intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

}

