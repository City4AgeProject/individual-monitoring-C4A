package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "user_in_role")
@SequenceGenerator(name = "default_gen", sequenceName = "user_in_role_seq", allocationSize = 1)
public class UserInRole extends AbstractBaseEntity<Long> implements Comparable<AbstractBaseEntity<Long>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1957422483462322553L;
	
	@Column(name = "pilot_code")
	@Type(type = "PilotEnumUserType")
	private Pilot.PilotCode pilotCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pilot_code", updatable = false, insertable = false)
	@JsonIgnore
	private Pilot pilot;

	@JsonIgnore
	@Column(name = "valid_from")
	private Date validFrom;

	@JsonIgnore
	@Column(name = "valid_to")
	private Date validTo;

	@JsonView(View.AssessmentView.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_system_id")
	private UserInSystem userInSystem;

	@Column(name = "cd_role_id")
	private Short roleId;

	@JsonManagedReference
	@OneToOne(mappedBy = "userInRole", cascade = CascadeType.ALL, orphanRemoval = true)
	private CrProfile crProfile;

	@JsonManagedReference
	@OneToOne(mappedBy = "userInRole", cascade = CascadeType.ALL, orphanRemoval = true)
	private CareProfile careProfile;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "userInRole", fetch = FetchType.LAZY)
	@OrderBy("changed DESC")
	private Set<FrailtyStatusTimeline> frailtyStatusTimeline = new HashSet<FrailtyStatusTimeline>();
	
	@Column (name = "firebase_token")
	private String token;
	

	public UserInRole() {
		
	}

	public UserInRole(Pilot.PilotCode pilotCode, Date validFrom, Date validTo, UserInSystem userInSystem, Short roleId,
			CrProfile crProfile, CareProfile careProfile, Set<FrailtyStatusTimeline> frailtyStatusTimeline) {
		this.pilotCode = pilotCode;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.userInSystem = userInSystem;
		this.roleId = roleId;
		this.crProfile = crProfile;
		this.careProfile = careProfile;
		this.frailtyStatusTimeline = frailtyStatusTimeline;
	}
	

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public UserInSystem getUserInSystem() {
		return userInSystem;
	}

	public void setUserInSystem(UserInSystem userInSystem) {
		this.userInSystem = userInSystem;
	}

	public Short getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public CrProfile getCrProfile() {
		return crProfile;
	}

	public void setCrProfile(CrProfile crProfile) {
		this.crProfile = crProfile;
	}

	public CareProfile getCareProfile() {
		return careProfile;
	}

	public void setCareProfile(CareProfile careProfile) {
		this.careProfile = careProfile;
	}

	public Set<FrailtyStatusTimeline> getFrailtyStatusTimeline() {
		return frailtyStatusTimeline;
	}

	public void setFrailtyStatusTimeline(Set<FrailtyStatusTimeline> frailtyStatusTimeline) {
		this.frailtyStatusTimeline = frailtyStatusTimeline;
	}

	public Pilot.PilotCode getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(Pilot.PilotCode pilotCode) {
		this.pilotCode = pilotCode;
	}
	
	public Pilot getPilot() {
		return pilot;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}

	@Override
	public int compareTo(AbstractBaseEntity<Long> o) {
		return this.getId().compareTo(o.getId());
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
