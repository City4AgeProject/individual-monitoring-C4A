package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "gef_prediction")
@SequenceGenerator(name = "default_gen", sequenceName = "gef_prediction_seq", allocationSize = 1)
public class GeriatricFactorPredictionValue extends AbstractBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6152936223492331766L;

	@Column(name = "user_in_role_id")
	private Long userInRoleId;

	@JsonView(View.TimeIntervalView.class)
	@Column(name = "gef_value")
	private BigDecimal gefValue;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "time_interval_id", referencedColumnName = "id")
	private TimeInterval timeInterval;
	
	@Column(name = "gef_type_id")
	private Long detectionVariableId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gef_type_id", insertable = false, updatable = false)
	private DetectionVariable detectionVariable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_role_id", insertable = false, updatable = false)
	private UserInRole userInRole;

	@Column(name = "data_source_type")
	private String dataSourceType;

	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;

	@JsonView(View.TimeIntervalView.class)
	@OneToMany(mappedBy = "geriatricFactorValue", fetch = FetchType.LAZY)
	private Set<Assessment> assessments = new HashSet<Assessment>();

	public GeriatricFactorPredictionValue() {
	}

	public GeriatricFactorPredictionValue(BigDecimal gefValue, TimeInterval timeInterval, DetectionVariable detectionVariable,
			UserInRole userInRole, String dataSourceType, Set<Assessment> assessments) {
		this.gefValue = gefValue;
		this.timeInterval = timeInterval;
		this.detectionVariable = detectionVariable;
		this.userInRole = userInRole;
		this.dataSourceType = dataSourceType;
		this.assessments = assessments;
	}

	public BigDecimal getGefValue() {
		return this.gefValue;
	}

	public void setGefValue(BigDecimal gefValue) {
		this.gefValue = gefValue;
	}

	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;

	}

	public DetectionVariable getDetectionVariable() {
		return detectionVariable;
	}

	public void setDetectionVariable(DetectionVariable detectionVariable) {
		this.detectionVariable = detectionVariable;
	}

	public UserInRole getUserInRole() {
		return userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public String getDataSourceType() {
		return this.dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}

	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}

	public Set<Assessment> getAssessments() {
		return assessments;
	}

	public void setAssessments(Set<Assessment> assessments) {
		this.assessments = assessments;
	}

	public void addAssessment(Assessment assessment) {
		this.assessments.add(assessment);
	}

	public DetectionVariable getGefTypeId() {
		return this.detectionVariable;
	}

	public Long getUserInRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(Long userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}

}