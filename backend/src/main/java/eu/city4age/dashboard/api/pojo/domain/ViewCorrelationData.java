package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vw_correlation_data")
@Immutable
public class ViewCorrelationData implements Comparable<ViewCorrelationData> {
	
	@EmbeddedId
	private ViewCorrelationDataKey id;
	
	@Column(name = "value")
	private BigDecimal value;
	
	@Column(name="interval_start")
	private Date intervalStart;
	
	@Column(name = "derived_detection_variable_id")
	private Long derivedDetectionVariableId;
	
	@Column(name = "user_in_role_id")
	private Long userInRoleId;
	
	@Column(name = "ovl_value")
	private BigDecimal ovlValue;
	
	@Column(name = "detection_variable_id")
	private Long detectionVariableId;

	/**
	 * 
	 */
	public ViewCorrelationData() {
	}

	/**
	 * @return the id
	 */
	public ViewCorrelationDataKey getId() {
		return id;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @return the intervalStart
	 */
	public Date getIntervalStart() {
		return intervalStart;
	}

	/**
	 * @return the derivedDetectionVariableId
	 */
	public Long getDerivedDetectionVariableId() {
		return derivedDetectionVariableId;
	}

	/**
	 * @return the userInRoleId
	 */
	public Long getUserInRoleId() {
		return userInRoleId;
	}

	/**
	 * @return the ovlValue
	 */
	public BigDecimal getOvlValue() {
		return ovlValue;
	}

	/**
	 * @return the detectionVariableId
	 */
	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	@Override
	public int compareTo(ViewCorrelationData o) {
		return this.getIntervalStart().compareTo(o.getIntervalStart());
	}

}
