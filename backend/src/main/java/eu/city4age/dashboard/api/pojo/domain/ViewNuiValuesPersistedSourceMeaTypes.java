package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_nui_values_persisted_source_mea_types")
@Immutable
public class ViewNuiValuesPersistedSourceMeaTypes {
	
	@Column(name = "mpdv_id")
	private Long mpdvId;
	
	@Column(name="pilot_code")
	private String pilotCode;
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;

	@Column(name = "mea_id")
	private Long meaId;
	
	@Column(name = "mea_name")
	private String meaName;
	
	@Column(name = "derived_nui_id")
	private Long derivedNuiId;
	
	@Column(name = "derived_nui_name")
	private String derivedNuiName;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="nui_value")
	private BigDecimal nuiValue;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	@Column(name="interval_start")
	private Date intervalStart;
	
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="interval_end")
	private Date intervalEnd;
	
	
	public void setMpdvId(Long mpdvId) {
		this.mpdvId = mpdvId;
	}
	
	public Long getMpdvId() {
		return mpdvId;
	}
	
	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;	
	}
	
	public String getPilotCode() {
		return this.pilotCode;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}
	
	public Long getUserInRoleId() {
		return userInRoleId;
	}
	
	public void setMeaId(Long meaId) {
		this.meaId = meaId;
	}
	
	public Long getMeaId() {
		return meaId;
	}

	public void setMeaName(String meaName) {
		this.meaName = meaName;
	}
	
	public String getMeaName() {
		return this.meaName;
	}

	public void setDerivedNuiId(Long derivedNuiId) {
		this.derivedNuiId = derivedNuiId;
	}
	
	public Long getDerivedNuiId() {
		return derivedNuiId;
	}
	
	public void setDerivedNuiName(String derivedNuiName) {
		this.derivedNuiName = derivedNuiName;
	}
	
	public String getDerivedNuiName() {
		return this.derivedNuiName;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setNuiValue(BigDecimal nuiValue) {
		this.nuiValue = nuiValue;
	}
	
	public BigDecimal getNuiValue() {
		return nuiValue;
	}
	
	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}
	
	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}
	
	public void setIntervalStart(Date intervalStart) {
		this.intervalStart = intervalStart;
	}
	
	public Date getIntervalStart() {
		return intervalStart;
	}
	
	public void setTypicalPeriod(String typicalPeriod) {
		this.meaName = typicalPeriod;
	}
	
	public String getTypicalPeriod() {
		return this.typicalPeriod;
	}
	
	public void setIntervalEnd(Date intervalEnd) {
		this.intervalEnd = intervalEnd;
	}
	
	public Date getIntervalEnd() {
		return intervalEnd;
	}
}

