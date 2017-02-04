package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="stakeholder")
public class Stakeholder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1493165258846574863L;

	@Id
	@Column(name="stakeholder_abbreviation")
	private String stakeholderAbbreviation;
	
	@Column(name="stakeholder_name")
	private String stakeholderName;
	
	@Column(name="stakeholder_description")
	private String stakeholderDescription;
	
	@JsonIgnore
	@Column(name="valid_from")
	private Timestamp validFrom;
	
	@JsonIgnore
	@Column(name="valid_to")
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
