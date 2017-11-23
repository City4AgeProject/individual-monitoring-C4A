package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import eu.city4age.dashboard.api.pojo.dto.Gfvs;

@SqlResultSetMappings(value = { @SqlResultSetMapping(name = "gfvs", classes = {
				@ConstructorResult(targetClass = Gfvs.class,
			    columns = { @ColumnResult(name = "uir_id", type = Long.class),
			    		@ColumnResult(name = "ddv_id", type = Long.class),
			    		@ColumnResult(name = "ges_value", type = BigDecimal.class),
			    		@ColumnResult(name = "weight", type = BigDecimal.class)}) })
})
@NamedNativeQueries(value = {
	@NamedNativeQuery(name = "ViewGefValuesPersistedSourceGesTypes.doAllGfvs", resultSetMapping = "gfvs", query = "WITH subq1 AS ( SELECT user_in_role_id AS uir_id, derived_detection_variable_id AS ddv_id, SUM (derivation_weight) AS sum_weight, SUM (gef_value*derivation_weight) AS sum_ges_value_weight FROM vw_gef_values_persisted_source_ges_types WHERE interval_start >= :startOfMonth AND interval_start <= :endOfMonth AND typical_period = 'MON' AND derived_detection_variable_type = :detectionVariableType GROUP BY ( user_in_role_id, derived_detection_variable_id ) ORDER BY (user_in_role_id) ), subq2 AS ( SELECT user_in_role_id AS uir_id, derivation_weight, detection_variable_id AS dv_id FROM vw_gef_values_persisted_source_ges_types WHERE interval_start >= :startOfMonth AND interval_start <= :endOfMonth AND typical_period = 'MON' AND detection_variable_type = :detectionVariableType )SELECT subq1.uir_id, subq1.ddv_id, subq1.sum_ges_value_weight/subq1.sum_weight as ges_value, subq2.derivation_weight as weight FROM subq1 LEFT JOIN subq2 ON subq1.uir_id = subq2.uir_id AND subq1.ddv_id = subq2.dv_id ORDER BY subq1.uir_id,subq1.ddv_id")
})
@Entity
@Table(name = "vw_gef_values_persisted_source_ges_types")
@Immutable
public class ViewGefValuesPersistedSourceGesTypes {
	
	@Column(name = "mpdv_id")
	private Long mpdvId;
	
	@Column(name = "detection_variable_name")
	private String gesName;
	
	@Column(name = "detection_variable_type")
	private String gesType;
	
	@Column(name = "derived_detection_variable_name")
	private String derivedGefName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "derived_detection_variable_type")
	private DetectionVariableType derivedGefType;
	
	@EmbeddedId
	private ViewPilotDetectionVariableKey id;
	
	@Column(name="gef_value")
	private BigDecimal gefValue;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	@Column(name="interval_start")
	private Date intervalStart;
	
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="interval_end")
	private Date intervalEnd;

	public Long getMpdvId() {
		return mpdvId;
	}

	public void setMpdvId(Long mpdvId) {
		this.mpdvId = mpdvId;
	}

	public String getGesName() {
		return gesName;
	}

	public void setGesName(String gesName) {
		this.gesName = gesName;
	}

	public String getGesType() {
		return gesType;
	}

	public void setGesType(String gesType) {
		this.gesType = gesType;
	}

	public String getDerivedGefName() {
		return derivedGefName;
	}

	public void setDerivedGefName(String derivedGefName) {
		this.derivedGefName = derivedGefName;
	}

	public DetectionVariableType getDerivedGefType() {
		return derivedGefType;
	}

	public void setDerivedGefType(DetectionVariableType derivedGefType) {
		this.derivedGefType = derivedGefType;
	}

	public ViewPilotDetectionVariableKey getId() {
		return id;
	}

	public void setId(ViewPilotDetectionVariableKey id) {
		this.id = id;
	}

	public BigDecimal getGefValue() {
		return gefValue;
	}

	public void setGefValue(BigDecimal gefValue) {
		this.gefValue = gefValue;
	}

	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}

	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}

	public Date getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Date intervalStart) {
		this.intervalStart = intervalStart;
	}

	public String getTypicalPeriod() {
		return typicalPeriod;
	}

	public void setTypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}

	public Date getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(Date intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

}

