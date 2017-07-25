package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="activity")
public class Activity extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4227824408744213065L;

	@ManyToOne
    @JoinColumn(name="data_source_type")
	private DataSourceType cdDataSourceType;
	
	@ManyToOne
    @JoinColumn(name="time_interval_id", referencedColumnName = "id")
	private TimeInterval timeInterval;
	
	@Column(name="user_in_role_id")
	private UserInRole userInRole;
	
	@Column(name="activity_name")
	private String activityName;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="activity",fetch=FetchType.LAZY)
	private Set<ExecutedAction> executedActions = new HashSet<ExecutedAction>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="activityByRealActivityId",fetch=FetchType.LAZY)
	private Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForRealActivityId = new HashSet<InterActivityBehaviourVariation>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="activity",fetch=FetchType.LAZY)
	private Set<VariationMeasureValue> variationMeasureValues = new HashSet<VariationMeasureValue>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="activity",fetch=FetchType.LAZY)
	private Set<Eam> eams = new HashSet<Eam>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="activityByExpectedActivityId",fetch=FetchType.LAZY)
	private Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForExpectedActivityId = new HashSet<InterActivityBehaviourVariation>(0);

	public Activity() {
	}

	public Activity(DataSourceType cdDataSourceType, TimeInterval timeInterval, UserInRole userInRole) {
		this.cdDataSourceType = cdDataSourceType;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
	}

	public Activity(DataSourceType cdDataSourceType, TimeInterval timeInterval, UserInRole userInRole,
			String activityName, Set<ExecutedAction> executedActions, Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForRealActivityId,
			Set<VariationMeasureValue> variationMeasureValues, Set<Eam> eams, Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForExpectedActivityId) {
		this.cdDataSourceType = cdDataSourceType;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.activityName = activityName;
		this.executedActions = executedActions;
		this.interActivityBehaviourVariationsForRealActivityId = interActivityBehaviourVariationsForRealActivityId;
		this.variationMeasureValues = variationMeasureValues;
		this.eams = eams;
		this.interActivityBehaviourVariationsForExpectedActivityId = interActivityBehaviourVariationsForExpectedActivityId;
	}

	public DataSourceType getCdDataSourceType() {
		return this.cdDataSourceType;
	}

	public void setCdDataSourceType(DataSourceType cdDataSourceType) {
		this.cdDataSourceType = cdDataSourceType;
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

	public String getActivityName() {
		return this.activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Set<ExecutedAction> getExecutedActions() {
		return this.executedActions;
	}

	public void setExecutedActions(Set<ExecutedAction> executedActions) {
		this.executedActions = executedActions;
	}

	public Set<InterActivityBehaviourVariation> getInterActivityBehaviourVariationsForRealActivityId() {
		return this.interActivityBehaviourVariationsForRealActivityId;
	}

	public void setInterActivityBehaviourVariationsForRealActivityId(
			Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForRealActivityId) {
		this.interActivityBehaviourVariationsForRealActivityId = interActivityBehaviourVariationsForRealActivityId;
	}

	public Set<VariationMeasureValue> getVariationMeasureValues() {
		return this.variationMeasureValues;
	}

	public void setVariationMeasureValues(Set<VariationMeasureValue> variationMeasureValues) {
		this.variationMeasureValues = variationMeasureValues;
	}

	public Set<Eam> getEams() {
		return this.eams;
	}

	public void setEams(Set<Eam> eams) {
		this.eams = eams;
	}

	public Set<InterActivityBehaviourVariation> getInterActivityBehaviourVariationsForExpectedActivityId() {
		return this.interActivityBehaviourVariationsForExpectedActivityId;
	}

	public void setInterActivityBehaviourVariationsForExpectedActivityId(
			Set<InterActivityBehaviourVariation> interActivityBehaviourVariationsForExpectedActivityId) {
		this.interActivityBehaviourVariationsForExpectedActivityId = interActivityBehaviourVariationsForExpectedActivityId;
	}

}
