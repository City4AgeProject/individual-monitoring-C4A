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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

/**
 * VariationMeasureValue generated by hbm2java
 */
@Entity
@Table(name = "variation_measure_value")
@SequenceGenerator(name = "default_gen", sequenceName = "variation_measure_value_seq", allocationSize = 1)
public class VariationMeasureValue extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2579000073949031381L;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "executed_activity_id")
	private ExecutedActivity activity;
	
	@JsonView(View.VariationMeasureValueView.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measure_type_id", referencedColumnName = "id")
	private DetectionVariable detectionVariable;
	
	@JsonView(View.VariationMeasureValueView.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "time_interval_id", referencedColumnName = "id")
	private TimeInterval timeInterval;
	
	@JsonView(View.VariationMeasureValueView.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_role_id")
	private UserInRole userInRole;

	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "measure_value", precision = 30, scale = 10)
	private BigDecimal measureValue;

	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "extra_information", length = 1000)
	private String extraInformation;

	@Column(name = "data_source_type", length = 1000)
	private String dataSourceType;	
		
	@JsonView(View.VariationMeasureValueView.class)
	@JsonInclude ()
	@OneToOne (mappedBy = "value", fetch = FetchType.LAZY)
	private ValueEvidenceNotice valueEvidenceNotice; 
	
	@OneToMany(mappedBy = "vmv", fetch = FetchType.LAZY)
	private Set<VmvFiltering> vmvFiltering = new HashSet<VmvFiltering>();


	public VariationMeasureValue() {
		
	}

	public VariationMeasureValue(DetectionVariable detectionVariable, TimeInterval timeInterval,
			UserInRole userInRole) {
		this.detectionVariable = detectionVariable;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
	}

	public VariationMeasureValue(ExecutedActivity activity, DetectionVariable detectionVariable, TimeInterval timeInterval,
			UserInRole userInRole, BigDecimal measureValue, String dataSourceType, String extraInformation) {
		this.activity = activity;
		this.detectionVariable = detectionVariable;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.measureValue = measureValue;
		this.dataSourceType = dataSourceType;
		this.extraInformation = extraInformation;
	}

	
	public ExecutedActivity getActivity() {
		return this.activity;
	}

	public void setActivity(ExecutedActivity activity) {
		this.activity = activity;
	}

	public DetectionVariable getDetectionVariable() {
		return this.detectionVariable;
	}

	public void setDetectionVariable(DetectionVariable detectionVariable) {
		this.detectionVariable = detectionVariable;
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

	public BigDecimal getMeasureValue() {
		return this.measureValue;
	}

	public void setMeasureValue(BigDecimal measureValue) {
		this.measureValue = measureValue;
	}

	public String getDataSourceType() {
		return this.dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getExtraInformation() {
		return this.extraInformation;
	}

	public void setExtraInformation(String extraInformation) {
		this.extraInformation = extraInformation;
	}

	public ValueEvidenceNotice getValueEvidenceNotice() {
		return valueEvidenceNotice;
	}

	public void setValueEvidenceNotice (ValueEvidenceNotice valueEvidenceNotice) {
		this.valueEvidenceNotice = valueEvidenceNotice;
	}

}
