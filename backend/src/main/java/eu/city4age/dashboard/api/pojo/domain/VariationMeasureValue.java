package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="variation_measure_value")
public class VariationMeasureValue extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2579000073949031381L;

	@ManyToOne
    @JoinColumn(name="activity_id")
	private Activity activity;
	
	/*@ManyToOne
    @JoinColumn(name="data_source_type")
	private DataSourceType cdDataSourceType;*/

	@ManyToOne
    @JoinColumn(name="measure_type_id")
	private DetectionVariable cdDetectionVariable;
	
	@Transient
	@ManyToOne
    @JoinColumn(name="time_interval_id")	
	private TimeInterval timeInterval;

	@ManyToOne
    @JoinColumn(name="user_in_role_id")
	private UserInRole userInRole;

	@Column(name="measure_value")
	private Float measureValue;
	
	/*@Column(name="data_source_type")
	@Type(type="eu.city4age.dashboard.api.persist.convert.IntArrayUserType") 
	private int[] cdDataSourceType;*/
	
	@OneToMany
	@JoinColumn(name="variation_measure_id")
	private Set<NumericIndicatorValue> numericIndicatorValues = new HashSet<NumericIndicatorValue>(0);

	public VariationMeasureValue() {
	}

	public VariationMeasureValue(//int[] cdDataSourceType, 
			DetectionVariable cdDetectionVariable,
			TimeInterval timeInterval, UserInRole userInRole) {
		//this.cdDataSourceType = cdDataSourceType;
		this.cdDetectionVariable = cdDetectionVariable;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
	}

	public VariationMeasureValue(Activity activity, //int[] cdDataSourceType,
			DetectionVariable cdDetectionVariable, TimeInterval timeInterval, UserInRole userInRole,
			Float measureValue, Set<NumericIndicatorValue> numericIndicatorValues) {
		this.activity = activity;
		//this.cdDataSourceType = cdDataSourceType;
		this.cdDetectionVariable = cdDetectionVariable;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.measureValue = measureValue;
		this.numericIndicatorValues = numericIndicatorValues;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/*public int[] getCdDataSourceType() {
		return cdDataSourceType;
	}

	public void setCdDataSourceType(int[] cdDataSourceType) {
		this.cdDataSourceType = cdDataSourceType;
	}*/

	public DetectionVariable getCdDetectionVariable() {
		return this.cdDetectionVariable;
	}

	public void setCdDetectionVariable(DetectionVariable cdDetectionVariable) {
		this.cdDetectionVariable = cdDetectionVariable;
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

	public Float getMeasureValue() {
		return this.measureValue;
	}

	public void setMeasureValue(Float measureValue) {
		this.measureValue = measureValue;
	}

	public Set<NumericIndicatorValue> getNumericIndicatorValues() {
		return this.numericIndicatorValues;
	}

	public void setNumericIndicatorValues(Set<NumericIndicatorValue> numericIndicatorValues) {
		this.numericIndicatorValues = numericIndicatorValues;
	}

}
