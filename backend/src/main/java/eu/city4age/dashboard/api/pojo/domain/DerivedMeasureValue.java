package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "derived_measure_value")
@SequenceGenerator(name = "default_gen", sequenceName = "derived_measure_value_seq", allocationSize = 1)
public class DerivedMeasureValue extends AbstractBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3674878218806699832L;

	@Column(name = "user_in_role_id")
	private Long userInRoleId;

	@Column(name = "derived_measure_value")
	private BigDecimal dmValue;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "time_interval_id", referencedColumnName = "id")
	private TimeInterval timeInterval;
	
	@Column(name = "mea_type_id")
	private Long detectionVariableId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mea_type_id", insertable = false, updatable = false)
	private DetectionVariable detectionVariable;
	
	@Column(name = "ges_type_id")
	private Long derivedDetectionVariableId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ges_type_id", insertable = false, updatable = false)
	private DetectionVariable derivedDetectionVariable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_role_id", insertable = false, updatable = false)
	private UserInRole userInRole;

	public DerivedMeasureValue() {
	}

	public DerivedMeasureValue(BigDecimal dmValue, TimeInterval timeInterval, DetectionVariable detectionVariable,
			UserInRole userInRole) {
		this.dmValue = dmValue;
		this.timeInterval = timeInterval;
		this.detectionVariable = detectionVariable;
		this.userInRole = userInRole;
	}

	/**
	 * @return the userInRoleId
	 */
	public Long getUserInRoleId() {
		return userInRoleId;
	}

	/**
	 * @param userInRoleId the userInRoleId to set
	 */
	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	/**
	 * @return the dmValue
	 */
	public BigDecimal getDmValue() {
		return dmValue;
	}

	/**
	 * @param dmValue the dmValue to set
	 */
	public void setDmValue(BigDecimal dmValue) {
		this.dmValue = dmValue;
	}

	/**
	 * @return the timeInterval
	 */
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	/**
	 * @param timeInterval the timeInterval to set
	 */
	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @return the detectionVariableId
	 */
	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	/**
	 * @param detectionVariableId the detectionVariableId to set
	 */
	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}

	/**
	 * @return the detectionVariable
	 */
	public DetectionVariable getDetectionVariable() {
		return detectionVariable;
	}

	/**
	 * @param detectionVariable the detectionVariable to set
	 */
	public void setDetectionVariable(DetectionVariable detectionVariable) {
		this.detectionVariable = detectionVariable;
	}

	/**
	 * @return the userInRole
	 */
	public UserInRole getUserInRole() {
		return userInRole;
	}

	/**
	 * @param userInRole the userInRole to set
	 */
	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	/**
	 * @return the derivedDetectionVariableId
	 */
	public Long getDerivedDetectionVariableId() {
		return derivedDetectionVariableId;
	}

	/**
	 * @param derivedDetectionVariableId the derivedDetectionVariableId to set
	 */
	public void setDerivedDetectionVariableId(Long derivedDetectionVariableId) {
		this.derivedDetectionVariableId = derivedDetectionVariableId;
	}

	/**
	 * @return the derivedDetectionVariable
	 */
	public DetectionVariable getDerivedDetectionVariable() {
		return derivedDetectionVariable;
	}

	/**
	 * @param derivedDetectionVariable the derivedDetectionVariable to set
	 */
	public void setDerivedDetectionVariable(DetectionVariable derivedDetectionVariable) {
		this.derivedDetectionVariable = derivedDetectionVariable;
	}

}