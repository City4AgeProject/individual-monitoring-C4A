package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "time_interval", uniqueConstraints = @UniqueConstraint(columnNames = { "interval_start",
		"typical_period" }, name = "time_interval_interval_start_typical_period_key"))
public class TimeInterval implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -865550404227746101L;

	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "ti_seq", sequenceName = "time_interval_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "ti_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "interval_start")
	private Timestamp intervalStart;


	@JsonView(View.VariationMeasureValueView.class)
	@Column(name = "interval_end")
	private Timestamp intervalEnd;

	@JsonView(View.TimeIntervalView.class) // Only on start not end, check
											// this!!!	
	@Transient
	private String start;

	@Transient
	private String end;
	
	
	@Column(name = "typical_period")
	private String typicalPeriod;
	
	@Column
	private Timestamp created;

	@Transient
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<ExecutedActivity> activities = new HashSet<ExecutedActivity>(0);

	@Transient
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<NumericIndicatorValue> numericIndicatorValues = new HashSet<NumericIndicatorValue>(0);

	@Transient
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<VariationMeasureValue> variationMeasureValues = new HashSet<VariationMeasureValue>(0);

	@JsonView(View.TimeIntervalView.class)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<GeriatricFactorValue> geriatricFactorValue;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<FrailtyStatusTimeline> frailtyStatusTimeline = new HashSet<FrailtyStatusTimeline>();

	public TimeInterval() {
	}

	public TimeInterval(Timestamp intervalStart, Timestamp intervalEnd, String typicalPeriod, Set<ExecutedActivity> activities,
			Set<NumericIndicatorValue> numericIndicatorValues, Set<VariationMeasureValue> variationMeasureValues,
			Set<GeriatricFactorValue> geriatricFactorValue, Set<FrailtyStatusTimeline> frailtyStatusTimeline) {
		this.intervalStart = intervalStart;
		this.intervalEnd = intervalEnd;
		this.typicalPeriod = typicalPeriod;
		this.activities = activities;
		this.numericIndicatorValues = numericIndicatorValues;
		this.variationMeasureValues = variationMeasureValues;
		this.geriatricFactorValue = geriatricFactorValue;
		this.start = intervalStart.toString();
		this.end = intervalEnd.toString();
		this.frailtyStatusTimeline = frailtyStatusTimeline;
	}

	public Timestamp getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Timestamp intervalStart) {
		this.intervalStart = intervalStart;
	}

	public Timestamp getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(Timestamp intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

	public String getTypicalPeriod() {
		return this.typicalPeriod;
	}

	public void setTypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}

	public Set<ExecutedActivity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<ExecutedActivity> activities) {
		this.activities = activities;
	}

	public Set<NumericIndicatorValue> getNumericIndicatorValues() {
		return this.numericIndicatorValues;
	}

	public void setNumericIndicatorValues(Set<NumericIndicatorValue> numericIndicatorValues) {
		this.numericIndicatorValues = numericIndicatorValues;
	}

	public Set<VariationMeasureValue> getVariationMeasureValues() {
		return this.variationMeasureValues;
	}

	public void setVariationMeasureValues(Set<VariationMeasureValue> variationMeasureValues) {
		this.variationMeasureValues = variationMeasureValues;
	}

	public Set<GeriatricFactorValue> getGeriatricFactorValue() {
		return geriatricFactorValue;
	}

	public void setGeriatricFactorValue(Set<GeriatricFactorValue> geriatricFactorValue) {
		this.geriatricFactorValue = geriatricFactorValue;
	}

	public Set<FrailtyStatusTimeline> getFrailtyStatusTimeline() {
		return frailtyStatusTimeline;
	}

	public void setFrailtyStatusTimeline(Set<FrailtyStatusTimeline> frailtyStatusTimeline) {
		this.frailtyStatusTimeline = frailtyStatusTimeline;
	}

	public String getStart() {
		return intervalStart.toString();
	}

	public String getEnd() {
		if(intervalEnd != null)
			return intervalEnd.toString();
		else
			return "";
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}
