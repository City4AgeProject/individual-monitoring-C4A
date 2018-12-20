package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewGroupAnalyticsDataKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2917340197405709759L;
	
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;
	
	@Column(name = "detection_variable_id")
	private Long detectionVariableId;
	
	@Column(name = "time_interval_id")
	private Long timeIntervalId;
	

	/**
	 * @return the userInRoleId
	 */
	public Long getUserInRoleId() {
		return userInRoleId;
	}

	/**
	 * @return the detectionVariableId
	 */
	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	/**
	 * @return the timeIntervalId
	 */
	public Long getTimeIntervalId() {
		return timeIntervalId;
	}
	
	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewGroupAnalyticsDataKey))
			return false;
		ViewGroupAnalyticsDataKey castOther = (ViewGroupAnalyticsDataKey) other;

		return (this.getUserInRoleId().equals(castOther.getUserInRoleId())
				&& this.getDetectionVariableId().equals(castOther.getDetectionVariableId())
				&& this.getTimeIntervalId().equals(castOther.getTimeIntervalId()));
	}
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserInRoleId().intValue();
		result = 37 * result + this.getDetectionVariableId().intValue();
		result = 37 * result + this.getTimeIntervalId().intValue();
		return result;
	}

}
