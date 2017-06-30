package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "pilot")
public class Pilot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267893598090303628L;

	private String name;

	@Id
	@Column(name = "pilot_code")
	private String pilotCode;

	@Column(name = "population_size")
	private Double populationSize;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "pilot", fetch = FetchType.LAZY)
	private Set<Location> locations = new HashSet<Location>(0);

	@Column(name = "latest_data_submission_completed")
	private Date latestSubmissionCompleted;

	@Column(name = "latest_derived_detection_variables_computed")
	private Date latestVariablesComputed;

	@Transient
	private YearMonth lastSubmitted;

	@Transient
	private YearMonth lastComputed;

	public Pilot() {
		lastSubmitted = YearMonth.of(2017, 2);
	}

	public Pilot(String name, String pilotCode, Double populationSize, Set<Location> locations) {
		this.name = name;
		this.pilotCode = pilotCode;
		this.populationSize = populationSize;
		this.locations = locations;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}

	public Double getPopulationSize() {
		return this.populationSize;
	}

	public void setPopulationSize(Double populationSize) {
		this.populationSize = populationSize;
	}

	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public YearMonth getLastSubmitted() {
		return lastSubmitted;
	}

	public void setLastSubmitted(YearMonth lastSubmitted) {
		this.lastSubmitted = lastSubmitted;
	}

	public YearMonth getLastComputed() {
		if (this.latestVariablesComputed != null) {
			return YearMonth
					.from(this.latestVariablesComputed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} else if (this.latestSubmissionCompleted != null) {
			return YearMonth
					.from(this.latestSubmissionCompleted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} else {
			return YearMonth.of(2017, 1);
		}
	}

	public void setLastComputed(YearMonth lastComputed) {
		this.lastComputed = lastComputed;
	}

	public Date getLatestSubmissionCompleted() {
		return latestSubmissionCompleted;
	}

	public void setLatestSubmissionCompleted(Date latestSubmissionCompleted) {
		this.latestSubmissionCompleted = latestSubmissionCompleted;
	}

	public Date getLatestVariablesComputed() {
		return latestVariablesComputed;
	}

	public void setLatestVariablesComputed(Date latestVariablesComputed) {
		this.latestVariablesComputed = latestVariablesComputed;
	}

}
