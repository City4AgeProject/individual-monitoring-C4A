package eu.city4age.dashboard.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CdRiskStatus implements Serializable {
	
	
	
	private String riskStatus;
	
	private String riskStatusDescription;
	
	private BigDecimal confidenceRating;
	
	private byte[] iconImage;

	
	
	public String getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
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
	


}
