package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="location")
public class Location extends AbstractBaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3078496828665625658L;

	@ManyToOne
    @JoinColumn(name="pilot_id")
	private Pilot pilot;

    @Column(name="location_name")
	private String locationName;

	private Short indoor;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="location",fetch=FetchType.LAZY)
	private Set<ExecutedAction> executedActions = new HashSet<ExecutedAction>(0);

	public Location() {
	}

	public Location(Pilot pilot, String locationName, Short indoor, Set<ExecutedAction> executedActions) {
		this.pilot = pilot;
		this.locationName = locationName;
		this.indoor = indoor;
		this.executedActions = executedActions;
	}

	public Pilot getPilot() {
		return this.pilot;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Short getIndoor() {
		return this.indoor;
	}

	public void setIndoor(Short indoor) {
		this.indoor = indoor;
	}

	public Set<ExecutedAction> getExecutedActions() {
		return this.executedActions;
	}

	public void setExecutedActions(Set<ExecutedAction> executedActions) {
		this.executedActions = executedActions;
	}

}
