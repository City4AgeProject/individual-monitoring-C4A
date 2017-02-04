package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "care_profile")
public class CareProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8190256926195219280L;

	@Id
	@Column(name = "user_in_role_id")
	private Long userInRoleId;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_in_role_id", insertable = false, updatable = false)
	private UserInRole userInRole;

	@ManyToOne
	@JoinColumn(name = "last_updated_by")
	private UserInRole userInRoleByLastUpdatedBy;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private UserInRole userInRoleByCreatedBy;

	@Column(name = "individual_summary")
	private String individualSummary;

	@Column(name = "attention_status")
	private Character attentionStatus;

	@Column(name = "intervention_status")
	private Character interventionStatus;

	@JsonIgnore
	@Column(name = "last_intervention_date")
	private Date lastInterventionDate;

	@JsonIgnore
	private Date created;

	@JsonIgnore
	@Column(name = "last_updated")
	private Date lastUpdated;

	public CareProfile() {
	}

	public CareProfile(UserInRole userInRoleByCreatedBy, String individualSummary, Date created) {
		this.userInRoleByCreatedBy = userInRoleByCreatedBy;
		this.individualSummary = individualSummary;
		this.created = created;
	}

	public CareProfile(UserInRole userInRoleByLastUpdatedBy, UserInRole userInRoleByCreatedBy, String individualSummary,
			Character attentionStatus, Character interventionStatus, Date lastInterventionDate, Date created,
			Date lastUpdated, UserInRole userInRole) {
		this.userInRoleByLastUpdatedBy = userInRoleByLastUpdatedBy;
		this.userInRoleByCreatedBy = userInRoleByCreatedBy;
		this.individualSummary = individualSummary;
		this.attentionStatus = attentionStatus;
		this.interventionStatus = interventionStatus;
		this.lastInterventionDate = lastInterventionDate;
		this.created = created;
		this.lastUpdated = lastUpdated;
		this.userInRole = userInRole;
	}

	public Long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public UserInRole getUserInRole() {
		return userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public UserInRole getUserInRoleByLastUpdatedBy() {
		return this.userInRoleByLastUpdatedBy;
	}

	public void setUserInRoleByLastUpdatedBy(UserInRole userInRoleByLastUpdatedBy) {
		this.userInRoleByLastUpdatedBy = userInRoleByLastUpdatedBy;
	}

	public UserInRole getUserInRoleByCreatedBy() {
		return this.userInRoleByCreatedBy;
	}

	public void setUserInRoleByCreatedBy(UserInRole userInRoleByCreatedBy) {
		this.userInRoleByCreatedBy = userInRoleByCreatedBy;
	}

	public String getIndividualSummary() {
		return this.individualSummary;
	}

	public void setIndividualSummary(String individualSummary) {
		this.individualSummary = individualSummary;
	}

	public Character getAttentionStatus() {
		return this.attentionStatus;
	}

	public void setAttentionStatus(Character attentionStatus) {
		this.attentionStatus = attentionStatus;
	}

	public Character getInterventionStatus() {
		return this.interventionStatus;
	}

	public void setInterventionStatus(Character interventionStatus) {
		this.interventionStatus = interventionStatus;
	}

	public Date getLastInterventionDate() {
		return this.lastInterventionDate;
	}

	public void setLastInterventionDate(Date lastInterventionDate) {
		this.lastInterventionDate = lastInterventionDate;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
