package eu.city4age.dashboard.api.pojo.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="inter_activity_behaviour_variation")
@SequenceGenerator(name = "default_gen", sequenceName = "inter_activity_behaviour_variation_seq", allocationSize = 1)
public class InterActivityBehaviourVariation extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5543250919034763161L;
	
	@ManyToOne
    @JoinColumn(name="real_activity_id")
	private ExecutedActivity activityByRealActivityId;
	
	@ManyToOne
    @JoinColumn(name="expected_activity_id")
	private ExecutedActivity activityByExpectedActivityId;

	@ManyToOne
    @JoinColumn(name="numeric_indicator_id")
	private NumericIndicatorValue numericIndicatorValue;
	
	private Float deviation;

	public InterActivityBehaviourVariation() {
	}

	public InterActivityBehaviourVariation(ExecutedActivity activityByRealActivityId,
			ExecutedActivity activityByExpectedActivityId, NumericIndicatorValue numericIndicatorValue) {
		this.activityByRealActivityId = activityByRealActivityId;
		this.activityByExpectedActivityId = activityByExpectedActivityId;
		this.numericIndicatorValue = numericIndicatorValue;
	}

	public InterActivityBehaviourVariation(ExecutedActivity activityByRealActivityId,
			ExecutedActivity activityByExpectedActivityId, NumericIndicatorValue numericIndicatorValue, Float deviation) {
		this.activityByRealActivityId = activityByRealActivityId;
		this.activityByExpectedActivityId = activityByExpectedActivityId;
		this.numericIndicatorValue = numericIndicatorValue;
		this.deviation = deviation;
	}

	public ExecutedActivity getActivityByRealActivityId() {
		return this.activityByRealActivityId;
	}

	public void setActivityByRealActivityId(ExecutedActivity activityByRealActivityId) {
		this.activityByRealActivityId = activityByRealActivityId;
	}

	public ExecutedActivity getActivityByExpectedActivityId() {
		return this.activityByExpectedActivityId;
	}

	public void setActivityByExpectedActivityId(ExecutedActivity activityByExpectedActivityId) {
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