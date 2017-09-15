package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_mea_nui_derivation_per_pilots")
@Immutable
public class ViewMeaNuiDerivationPerPilot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "mpdv_id", updatable = false, nullable = false)
	private Long mpdvId;
	
	@Column(name="pilot_code")
	private String pilotCode;
	
	@Column(name = "mea_id")
	private Long meaId;

	@Column(name = "mea_name")
	private String meaName;
	
	@Column(name = "formula")
	private String formula;

	@Column(name = "derived_nui_id")
	private Long derivedNuiId;
	
	@Column(name = "derived_nui_name")
	private String derivedNuiName;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	
	public void setMpdvId(Long mpdvId) {
		this.mpdvId = mpdvId;
	}
	
	public Long getMpdvId() {
		return mpdvId;
	}
	
	public String getPilotCode() {
		return this.pilotCode;
	}

	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;	
	}
	
	public void setMeaId(Long meaId) {
		this.meaId = meaId;
	}
	
	public Long getMeaId() {
		return meaId;
	}
	
	public String getMeaName() {
		return this.meaName;
	}

	public void setMeaName(String meaName) {
		this.meaName = meaName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;

	}

	public void setDerivedNuiId(Long derivedNuiId) {
		this.derivedNuiId = derivedNuiId;
	}
	
	public Long getDerivedNuiId() {
		return derivedNuiId;
	}
	
	public String getDerivedNuiName() {
		return this.derivedNuiName;
	}

	public void setDerivedNuiName(String derivedNuiName) {
		this.derivedNuiName = derivedNuiName;
	}
	
	
	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}
	
	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}
}
