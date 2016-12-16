package eu.city4age.dashboard.api.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Stakeholder implements Serializable {
	
	
	private String stakeholderAbbreviation;
	
	private String stakeholderName;
	
	private String stakeholderDescription;
	
	@JsonIgnore
	private Timestamp validFrom;
	
	@JsonIgnore
	private Timestamp validTo;

	
	
	public String getStakeholderAbbreviation() {
		return stakeholderAbbreviation;
	}

	public void setStakeholderAbbreviation(String stakeholderAbbreviation) {
		this.stakeholderAbbreviation = stakeholderAbbreviation;
	}

	public String getStakeholderName() {
		return stakeholderName;
	}

	public void setStakeholderName(String stakeholderName) {
		this.stakeholderName = stakeholderName;
	}

	public String getStakeholderDescription() {
		return stakeholderDescription;
	}

	public void setStakeholderDescription(String stakeholderDescription) {
		this.stakeholderDescription = stakeholderDescription;
	}

	public Timestamp getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}

	public Timestamp getValidTo() {
		return validTo;
	}

	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
	

}
