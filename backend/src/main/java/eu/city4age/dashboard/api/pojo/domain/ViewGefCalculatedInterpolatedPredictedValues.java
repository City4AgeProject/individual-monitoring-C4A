package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vw_gef_calculated_interpolated_predicted_values")
@Immutable
public class ViewGefCalculatedInterpolatedPredictedValues {
	
	@EmbeddedId
	private ViewGefCalculatedInterpolatedPredictedValuesKey id;
	
	@Column(name = "gef_value")
	private BigDecimal gefValue;
	
	@Column(name = "derivation_weight")
	private BigDecimal derivationWeight;
	
	@Column(name = "data_source_type")
	private BigDecimal dataSourceType;
	
	@Column(name="interval_start")
	private Date intervalStart;
	
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="interval_end")
	private Date intervalEnd;

	public ViewGefCalculatedInterpolatedPredictedValues() {
		
	}

	public ViewGefCalculatedInterpolatedPredictedValuesKey getId() {
		return id;
	}

	public void setId(ViewGefCalculatedInterpolatedPredictedValuesKey id) {
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

	public BigDecimal getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(BigDecimal dataSourceType) {
		this.dataSourceType = dataSourceType;
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
