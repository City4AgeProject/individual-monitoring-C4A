package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.dto.Last5Assessment;
import eu.city4age.dashboard.api.pojo.json.view.View;

@SqlResultSetMapping(name = "lastFiveMapping", classes = {
		@ConstructorResult(targetClass = Last5Assessment.class, columns = { @ColumnResult(name = "time_interval_id"),
				@ColumnResult(name = "interval_start"), @ColumnResult(name = "gef_id"),
				@ColumnResult(name = "gef_value"), @ColumnResult(name = "assessment_id"),
				@ColumnResult(name = "assessment_comment"), @ColumnResult(name = "risk_status"),
				@ColumnResult(name = "data_validity_status"), @ColumnResult(name = "created"),
				@ColumnResult(name = "display_name") }) })
// Need to check this querry why assessment properties were null
@NamedNativeQuery(name = "TimeInterval.getLastFiveForDiagram", resultSetMapping = "lastFiveMapping", query = "SELECT DISTINCT ti.id AS time_interval_id, ti.interval_start, gfv.id AS gef_id, gfv.gef_value, aa.id AS assessment_id, aa.assessment_comment, aa.risk_status AS risk_status, aa.data_validity_status, aa.created AS created, uis.display_name FROM time_interval AS ti LEFT OUTER JOIN (geriatric_factor_value AS gfv LEFT OUTER JOIN (assessed_gef_value_set AS agvs INNER JOIN assessment AS aa ON agvs.assessment_id = aa.id) ON agvs.gef_value_id = gfv.id) ON gfv.time_interval_id=ti.id LEFT OUTER JOIN user_in_role AS uir ON uir.id = gfv.user_in_role_id LEFT OUTER JOIN user_in_system AS uis ON uis.id = uir.user_in_system_id WHERE ti.interval_start >= :intervalStart AND (ti.interval_end <= :intervalEnd OR ti.typical_period = 'MON') AND (aa.id IN (SELECT id FROM (SELECT DISTINCT a1.id,a1.created FROM assessment a1 INNER JOIN assessed_gef_value_set AS agvs1 ON agvs1.assessment_id = a1.id WHERE agvs1.gef_value_id = agvs.gef_value_id ORDER BY a1.created DESC FETCH FIRST 5 ROWS ONLY) t) OR aa.id IS NULL) AND (gfv.user_in_role_id = :userInRoleId OR gfv.id IS NULL) AND (gfv.gef_type_id IN (SELECT dv1.id FROM cd_detection_variable AS dv1 JOIN md_pilot_detection_variable as pdv1 ON dv1.id = pdv1.detection_variable_id WHERE pdv1.derived_detection_variable_id = :parentDetectionVariableId AND pdv1.pilot_code = uir.pilot_code) OR gfv.id IS NULL) ORDER BY ti.id")

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

	@Transient
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "timeInterval", fetch = FetchType.LAZY)
	private Set<Activity> activities = new HashSet<Activity>(0);

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

	public TimeInterval(Timestamp intervalStart, Timestamp intervalEnd, String typicalPeriod, Set<Activity> activities,
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

	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<Activity> activities) {
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

}
