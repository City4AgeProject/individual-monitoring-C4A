package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cd_typical_period")
public class TypicalPeriod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8619826258153292282L;

	@Id
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="period_description")
	private String periodDescription;
	
	@Column(name="typical_duration")
	private Date typicalDuration;

	public TypicalPeriod() {
	}

	public TypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}

	public TypicalPeriod(String typicalPeriod, String periodDescription, Date typicalDuration) {
		this.typicalPeriod = typicalPeriod;
		this.periodDescription = periodDescription;
		this.typicalDuration = typicalDuration;
	}

	public String getTypicalPeriod() {
		return this.typicalPeriod;
	}

	public void setTypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}

	public String getPeriodDescription() {
		return this.periodDescription;
	}

	public void setPeriodDescription(String periodDescription) {
		this.periodDescription = periodDescription;
	}

	public Date getTypicalDuration() {
		return this.typicalDuration;
	}

	public void setTypicalDuration(Date typicalDuration) {
		this.typicalDuration = typicalDuration;
	}

}
