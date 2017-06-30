package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="cd_frailty_status")
public class FrailtyStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2600991177908483233L;

	@Id
	@Column(name="frailty_status")
	private String frailtyStatus;

	@Column(name="frailty_status_description")
	private String frailtyStatusDescription;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="cdFrailtyStatus",fetch=FetchType.LAZY)
	private Set<FrailtyStatusTimeline> frailtyStatusTimelines = new HashSet<FrailtyStatusTimeline>(0);

	public FrailtyStatus() {
	}

	public FrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}

	public FrailtyStatus(String frailtyStatus, String frailtyStatusDescription, Set<FrailtyStatusTimeline> frailtyStatusTimelines) {
		this.frailtyStatus = frailtyStatus;
		this.frailtyStatusDescription = frailtyStatusDescription;
		this.frailtyStatusTimelines = frailtyStatusTimelines;
	}

	public String getFrailtyStatus() {
		return this.frailtyStatus;
	}

	public void setFrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}

	public String getFrailtyStatusDescription() {
		return this.frailtyStatusDescription;
	}

	public void setFrailtyStatusDescription(String frailtyStatusDescription) {
		this.frailtyStatusDescription = frailtyStatusDescription;
	}

	public Set<FrailtyStatusTimeline> getFrailtyStatusTimelines() {
		return this.frailtyStatusTimelines;
	}

	public void setFrailtyStatusTimelines(Set<FrailtyStatusTimeline> frailtyStatusTimelines) {
		this.frailtyStatusTimelines = frailtyStatusTimelines;
	}

}
