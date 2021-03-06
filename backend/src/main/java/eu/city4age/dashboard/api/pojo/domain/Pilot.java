package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pilot")
public class Pilot implements Serializable {
	
	static protected Logger logger = LogManager.getLogger(Pilot.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267893598090303628L;
	
	public enum PilotCode {
		MAD("MAD"), LCC("LCC"), SIN("SIN"), MPL("MPL"), ATH("ATH"), BHX("BHX");
		
		private final String name;
		
		PilotCode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};	

	@Column(name = "pilot_name")
	private String name;

	@Id
	@Column(name = "pilot_code")
	@Type(type = "PilotEnumUserType")
	private Pilot.PilotCode pilotCode;

	@Column(name = "population_size")
	private Double populationSize;

	@Column(name = "latest_data_submission_completed")
	private Date latestSubmissionCompleted;

	@Column(name = "latest_derived_detection_variables_computed")
	private Date latestVariablesComputed;
	
	@Column(name = "latest_configuration_update")
	private Date latestConfigurationUpdate;
	
	@Column(name = "newest_submitted_data")
	private Date newestSubmittedData;
	
	@Column(name = "time_of_computation")
	private Date timeOfComputation;
	
	@Column(name = "personal_profile_data_url")
	private String personalProfileDataUrl;

	@Transient
	private YearMonth lastSubmitted;

	@Transient
	private YearMonth lastComputed;
	
	@Transient
	private YearMonth lastConfigured;
	
	@Transient
	private YearMonth startOfComputation;
	
	@Transient
	private YearMonth endOfComputation;

	@Column(name = "time_zone")
	private String timeZone;
	
	@Column(name = "comp_zone")
	private String compZone;

	public Pilot() {
	}

	public Pilot(String name, Pilot.PilotCode pilotCode, Double populationSize) {
		this.name = name;
		this.pilotCode = pilotCode;
		this.populationSize = populationSize;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pilot.PilotCode getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(Pilot.PilotCode pilotCode) {
		this.pilotCode = pilotCode;
	}

	public Double getPopulationSize() {
		return this.populationSize;
	}

	public void setPopulationSize(Double populationSize) {
		this.populationSize = populationSize;
	}

	public YearMonth getLastSubmitted() {
		return lastSubmitted;
	}

	public void setLastSubmitted(YearMonth lastSubmitted) {
		this.lastSubmitted = lastSubmitted;
	}

	public void setLastComputed(YearMonth lastComputed) {
		this.lastComputed = lastComputed;
	}

	public YearMonth getLastConfigured() {
		return lastConfigured;
	}

	public void setLastConfigured(YearMonth lastConfigured) {
		this.lastConfigured = lastConfigured;
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

	public Date getLatestConfigurationUpdate() {
		return latestConfigurationUpdate;
	}

	public void setLatestConfigurationUpdate(Date latestConfigurationUpdate) {
		this.latestConfigurationUpdate = latestConfigurationUpdate;
	}

	public Date getNewestSubmittedData() {
		return newestSubmittedData;
	}

	public void setNewestSubmittedData(Date newestSubmittedData) {
		this.newestSubmittedData = newestSubmittedData;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Date getTimeOfComputation() {
		return timeOfComputation;
	}

	public void setTimeOfComputation(Date timeOfComputation) {
		this.timeOfComputation = timeOfComputation;
	}

	public YearMonth getStartOfComputation() {
		return startOfComputation;
	}

	public void setStartOfComputation(YearMonth startOfComputation) {
		this.startOfComputation = startOfComputation;
	}

	public YearMonth getEndOfComputation() {
		return endOfComputation;
	}

	public void setEndOfComputation(YearMonth endOfComputation) {
		this.endOfComputation = endOfComputation;
	}

	public String getCompZone() {
		return compZone;
	}

	public void setCompZone(String compZone) {
		this.compZone = compZone;
	}

	/**
	 * @return the personalProfileDataUrl
	 */
	public String getPersonalProfileDataUrl() {
		return personalProfileDataUrl;
	}

	/**
	 * @param personalProfileDataUrl the personalProfileDataUrl to set
	 */
	public void setPersonalProfileDataUrl(String personalProfileDataUrl) {
		this.personalProfileDataUrl = personalProfileDataUrl;
	}

}
