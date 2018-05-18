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
public class ViewGefCalculatedInterpolatedPredictedValues implements Comparable<ViewGefCalculatedInterpolatedPredictedValues> {
	
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
	
	@Column(name = "interval_start_label")
	private String intervalStartLabel;
	
	@Column(name="typical_period")
	private String typicalPeriod;
	
	@Column(name="interval_end")
	private Date intervalEnd;
	
	@Column(name = "interval_end_label")
	private String intervalEndLabel;
	
	@Column(name = "time_zone")
	private String timeZone;
	
	@Column(name = "comp_zone")
	private String compZone;
	
	@Column(name = "detection_variable_name")
	private String detectionVariableName;
	
	@Column(name = "detection_variable_type")
	private String detectionVariableType;
	
	@Column(name = "derived_detection_variable_id")
	private Long derivedDetectionVariableId;
	
	@Column(name = "derived_detection_variable_name")
	private String derivedDetectionVariableName;
	
	@Column(name = "derived_detection_variable_type")
	private String derivedDetectionVariableType;
	
	@Column(name = "frailty_status")
	private String frailtyStatus;
	
	@Column(name = "pilot_code")
	private String pilotCode;
	
	@Column(name = "username")
	private String username;

	public ViewGefCalculatedInterpolatedPredictedValues() {
		
	}

	/**
	 * @return the id
	 */
	public ViewGefCalculatedInterpolatedPredictedValuesKey getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(ViewGefCalculatedInterpolatedPredictedValuesKey id) {
		this.id = id;
	}


	/**
	 * @return the gefValue
	 */
	public BigDecimal getGefValue() {
		return gefValue;
	}


	/**
	 * @param gefValue the gefValue to set
	 */
	public void setGefValue(BigDecimal gefValue) {
		this.gefValue = gefValue;
	}


	/**
	 * @return the derivationWeight
	 */
	public BigDecimal getDerivationWeight() {
		return derivationWeight;
	}


	/**
	 * @param derivationWeight the derivationWeight to set
	 */
	public void setDerivationWeight(BigDecimal derivationWeight) {
		this.derivationWeight = derivationWeight;
	}


	/**
	 * @return the dataSourceType
	 */
	public BigDecimal getDataSourceType() {
		return dataSourceType;
	}


	/**
	 * @param dataSourceType the dataSourceType to set
	 */
	public void setDataSourceType(BigDecimal dataSourceType) {
		this.dataSourceType = dataSourceType;
	}


	/**
	 * @return the intervalStart
	 */
	public Date getIntervalStart() {
		return intervalStart;
	}


	/**
	 * @param intervalStart the intervalStart to set
	 */
	public void setIntervalStart(Date intervalStart) {
		this.intervalStart = intervalStart;
	}


	/**
	 * @return the intervalStartLabel
	 */
	public String getIntervalStartLabel() {
		return intervalStartLabel;
	}


	/**
	 * @param intervalStartLabel the intervalStartLabel to set
	 */
	public void setIntervalStartLabel(String intervalStartLabel) {
		this.intervalStartLabel = intervalStartLabel;
	}


	/**
	 * @return the typicalPeriod
	 */
	public String getTypicalPeriod() {
		return typicalPeriod;
	}


	/**
	 * @param typicalPeriod the typicalPeriod to set
	 */
	public void setTypicalPeriod(String typicalPeriod) {
		this.typicalPeriod = typicalPeriod;
	}


	/**
	 * @return the intervalEnd
	 */
	public Date getIntervalEnd() {
		return intervalEnd;
	}


	/**
	 * @param intervalEnd the intervalEnd to set
	 */
	public void setIntervalEnd(Date intervalEnd) {
		this.intervalEnd = intervalEnd;
	}


	/**
	 * @return the intervalEndLabel
	 */
	public String getIntervalEndLabel() {
		return intervalEndLabel;
	}


	/**
	 * @param intervalEndLabel the intervalEndLabel to set
	 */
	public void setIntervalEndLabel(String intervalEndLabel) {
		this.intervalEndLabel = intervalEndLabel;
	}


	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}


	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}


	/**
	 * @return the compZone
	 */
	public String getCompZone() {
		return compZone;
	}


	/**
	 * @param compZone the compZone to set
	 */
	public void setCompZone(String compZone) {
		this.compZone = compZone;
	}


	/**
	 * @return the derivedDetectionVariableId
	 */
	public Long getDerivedDetectionVariableId() {
		return derivedDetectionVariableId;
	}


	/**
	 * @param derivedDetectionVariableId the derivedDetectionVariableId to set
	 */
	public void setDerivedDetectionVariableId(Long derivedDetectionVariableId) {
		this.derivedDetectionVariableId = derivedDetectionVariableId;
	}


	/**
	 * @return the frailtyStatus
	 */
	public String getFrailtyStatus() {
		return frailtyStatus;
	}


	/**
	 * @param frailtyStatus the frailtyStatus to set
	 */
	public void setFrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}


	/**
	 * @return the pilotCode
	 */
	public String getPilotCode() {
		return pilotCode;
	}


	/**
	 * @param pilotCode the pilotCode to set
	 */
	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}


	/**
	 * @return the detectionVariableType
	 */
	public String getDetectionVariableType() {
		return detectionVariableType;
	}


	/**
	 * @param detectionVariableType the detectionVariableType to set
	 */
	public void setDetectionVariableType(String detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

	/**
	 * @return the detectionVariableName
	 */
	public String getDetectionVariableName() {
		return detectionVariableName;
	}

	/**
	 * @param detectionVariableName the detectionVariableName to set
	 */
	public void setDetectionVariableName(String detectionVariableName) {
		this.detectionVariableName = detectionVariableName;
	}

	/**
	 * @return the derivedDetectionVariableType
	 */
	public String getDerivedDetectionVariableType() {
		return derivedDetectionVariableType;
	}

	/**
	 * @param derivedDetectionVariableType the derivedDetectionVariableType to set
	 */
	public void setDerivedDetectionVariableType(String derivedDetectionVariableType) {
		this.derivedDetectionVariableType = derivedDetectionVariableType;
	}

	/**
	 * @return the derivedDetectionVariableName
	 */
	public String getDerivedDetectionVariableName() {
		return derivedDetectionVariableName;
	}

	/**
	 * @param derivedDetectionVariableName the derivedDetectionVariableName to set
	 */
	public void setDerivedDetectionVariableName(String derivedDetectionVariableName) {
		this.derivedDetectionVariableName = derivedDetectionVariableName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int compareTo(ViewGefCalculatedInterpolatedPredictedValues o) {
		return this.getId().getTimeIntervalId().compareTo(o.getId().getTimeIntervalId());
	}

}
