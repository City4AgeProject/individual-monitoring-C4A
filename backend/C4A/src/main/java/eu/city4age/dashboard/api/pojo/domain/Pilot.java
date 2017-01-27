package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="pilot")
public class Pilot extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267893598090303628L;

	private String name;
	
	@Column(name="pilot_code")
	private String pilotCode;
	
	@Column(name="population_size")
	private Double populationSize;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="pilot",fetch=FetchType.LAZY)
	private Set<Location> locations = new HashSet<Location>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="pilot",fetch=FetchType.LAZY)
	private Set<PilotDetectionVariable> cdPilotDetectionVariables = new HashSet<PilotDetectionVariable>(0);

	public Pilot() {
	}

	public Pilot(String name, String pilotCode, Double populationSize, Set<Location> locations, Set<PilotDetectionVariable> cdPilotDetectionVariables) {
		this.name = name;
		this.pilotCode = pilotCode;
		this.populationSize = populationSize;
		this.locations = locations;
		this.cdPilotDetectionVariables = cdPilotDetectionVariables;
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

	public Set<PilotDetectionVariable> getCdPilotDetectionVariables() {
		return this.cdPilotDetectionVariables;
	}

	public void setCdPilotDetectionVariables(Set<PilotDetectionVariable> cdPilotDetectionVariables) {
		this.cdPilotDetectionVariables = cdPilotDetectionVariables;
	}

}
