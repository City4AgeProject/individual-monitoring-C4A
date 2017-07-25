package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "frailty_status_timeline")
@IdClass(FrailtyStatusTimelineId.class)
public class FrailtyStatusTimeline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276917963969525493L;

	@Id
	@Column(name = "time_interval_id", insertable = false, updatable = false)
	private Long timeIntervalId;

	@Id
	@Column(name = "user_in_role_id", insertable = false, updatable = false)
	private Long userInRoleId;

	@ManyToOne
	@JoinColumn(name = "time_interval_id", insertable = false, updatable = false, referencedColumnName = "id")
	private TimeInterval timeInterval;

	@ManyToOne
	@JoinColumn(name = "user_in_role_id", insertable = false, updatable = false)
	private UserInRole userInRole;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "changed_by")
	private UserInRole changedBy;

	@Column(name = "frailty_notice")
	private String frailtyNotice;

	@JsonIgnore
	@Id
	private Date changed;

	@Id
	private String frailtyStatus;

	@ManyToOne
	@JoinColumn(name = "frailty_status", insertable = false, updatable = false)
	private FrailtyStatus cdFrailtyStatus;

	public FrailtyStatusTimeline() {
	}

	public FrailtyStatusTimeline(TimeInterval timeInterval, UserInRole userInRole, UserInRole changedBy) {
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.changedBy = changedBy;
	}

	public FrailtyStatusTimeline(TimeInterval timeInterval, UserInRole userInRole, UserInRole changedBy,
			String frailtyNotice) {
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.changedBy = changedBy;
		this.frailtyNotice = frailtyNotice;
	}

	public TimeInterval getTimeInterval() {
		return this.timeInterval;
	}

	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public UserInRole getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(UserInRole changedBy) {
		this.changedBy = changedBy;
	}

	public String getFrailtyNotice() {
		return this.frailtyNotice;
	}

	public void setFrailtyNotice(String frailtyNotice) {
		this.frailtyNotice = frailtyNotice;
	}

	public Date getChanged() {
		return changed;
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public FrailtyStatus getCdFrailtyStatus() {
		return cdFrailtyStatus;
	}

	public void setCdFrailtyStatus(FrailtyStatus cdFrailtyStatus) {
		this.cdFrailtyStatus = cdFrailtyStatus;
	}

	public Long getTimeIntervalId() {
		return timeIntervalId;
	}

	public void setTimeIntervalId(Long timeIntervalId) {
		this.timeIntervalId = timeIntervalId;
	}

	public Long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public String getFrailtyStatus() {
		return frailtyStatus;
	}

	public void setFrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}
}
