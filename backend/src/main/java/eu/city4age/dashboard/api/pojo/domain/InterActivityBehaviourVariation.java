package eu.city4age.dashboard.api.pojo.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="inter_activity_behaviour_variation")
public class InterActivityBehaviourVariation extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5543250919034763161L;

	
	@ManyToOne
    @JoinColumn(name="real_activity_id")
	private Activity activityByRealActivityId;
	
	@ManyToOne
    @JoinColumn(name="expected_activity_id")
	private Activity activityByExpectedActivityId;

	@ManyToOne
    @JoinColumn(name="numeric_indicator_id")
	private NumericIndicatorValue numericIndicatorValue;
	
	private Float deviation;

	public InterActivityBehaviourVariation() {
	}

	public InterActivityBehaviourVariation(Activity activityByRealActivityId,
			Activity activityByExpectedActivityId, NumericIndicatorValue numericIndicatorValue) {
		this.activityByRealActivityId = activityByRealActivityId;
		this.activityByExpectedActivityId = activityByExpectedActivityId;
		this.numericIndicatorValue = numericIndicatorValue;
	}

	public InterActivityBehaviourVariation(Activity activityByRealActivityId,
			Activity activityByExpectedActivityId, NumericIndicatorValue numericIndicatorValue, Float deviation) {
		this.activityByRealActivityId = activityByRealActivityId;
		this.activityByExpectedActivityId = activityByExpectedActivityId;
		this.numericIndicatorValue = numericIndicatorValue;
		this.deviation = deviation;
	}

	public Activity getActivityByRealActivityId() {
		return this.activityByRealActivityId;
	}

	public void setActivityByRealActivityId(Activity activityByRealActivityId) {
		this.activityByRealActivityId = activityByRealActivityId;
	}

	public Activity getActivityByExpectedActivityId() {
		return this.activityByExpectedActivityId;
	}

	public void setActivityByExpectedActivityId(Activity activityByExpectedActivityId) {
		this.activityByExpectedActivityId = activityByExpectedActivityId;
	}

	public NumericIndicatorValue getNumericIndicatorValue() {
		return this.numericIndicatorValue;
	}

	public void setNumericIndicatorValue(NumericIndicatorValue numericIndicatorValue) {
		this.numericIndicatorValue = numericIndicatorValue;
	}

	public Float getDeviation() {
		return this.deviation;
	}

	public void setDeviation(Float deviation) {
		this.deviation = deviation;
	}

}