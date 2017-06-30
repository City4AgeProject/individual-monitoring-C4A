package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class FrailtyStatusTimelineId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5201735138977385731L;

	@Column(name="time_interval_id")
	private long timeIntervalId;

	@Column(name="changed")
	private Date changed;

	@Column(name="user_in_role_id")
	private long userInRoleId;
	
	@Column(name="frailty_status")
	private String frailtyStatus;

	
	public FrailtyStatusTimelineId() {
	}

	public FrailtyStatusTimelineId(long timeIntervalId, Date changed, long userInRoleId, String frailtyStatus) {
		this.timeIntervalId = timeIntervalId;
		this.changed = changed;
		this.userInRoleId = userInRoleId;
		this.frailtyStatus = frailtyStatus;
	}

	public long getTimeIntervalId() {
		return timeIntervalId;
	}

	public void setTimeIntervalId(long timeIntervalId) {
		this.timeIntervalId = timeIntervalId;
	}

	public Date getChanged() {
		return changed;
	}

	public void setChanged(Date changed) {
		this.changed = changed;
	}

	public long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public String getFrailtyStatus() {
		return frailtyStatus;
	}

	public void setFrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}
	

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FrailtyStatusTimelineId))
			return false;
		FrailtyStatusTimelineId castOther = (FrailtyStatusTimelineId) other;

		return (this.getTimeIntervalId() == castOther.getTimeIntervalId())
				&& (this.getChanged() == castOther.getChanged())
				&& (this.getUserInRoleId() == castOther.getUserInRoleId())
				&& (this.getFrailtyStatus() == castOther.getFrailtyStatus());
	}

	public int hashCode() {
	    StringBuilder builder = new StringBuilder();
	    builder.append(String.valueOf(timeIntervalId));
	    builder.append(changed.toString());
	    builder.append(String.valueOf(userInRoleId));
	    builder.append(frailtyStatus.toString());
	    return builder.toString().hashCode();
	}

}
