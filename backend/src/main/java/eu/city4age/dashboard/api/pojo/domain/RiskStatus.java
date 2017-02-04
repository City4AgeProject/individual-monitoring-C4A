package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cd_risk_status")
public class RiskStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2245819162805203773L;

	@Id
	@Column(name="risk_status")
	private Character riskStatus;
	
	@Column(name="risk_status_description")
	private String riskStatusDescription;
	
	@Column(name="confidence_rating")
	private BigDecimal confidenceRating;
	
	@Column(name="icon_image")
	private byte[] iconImage;
	
	@Column(name="icon_image_path")
	private String iconImagePath;



	public Character getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Character riskStatus) {
		this.riskStatus = riskStatus;
	}

	public String getRiskStatusDescription() {
		return riskStatusDescription;
	}

	public void setRiskStatusDescription(String riskStatusDescription) {
		this.riskStatusDescription = riskStatusDescription;
	}

	public BigDecimal getConfidenceRating() {
		return confidenceRating;
	}

	public void setConfidenceRating(BigDecimal confidenceRating) {
		this.confidenceRating = confidenceRating;
	}

	public byte[] getIconImage() {
		return iconImage;
	}

	public void setIconImage(byte[] iconImage) {
		this.iconImage = iconImage;
	}

	public String getIconImagePath() {
		return iconImagePath;
	}

	public void setIconImagePath(String iconImagePath) {
		this.iconImagePath = iconImagePath;
	}

}
