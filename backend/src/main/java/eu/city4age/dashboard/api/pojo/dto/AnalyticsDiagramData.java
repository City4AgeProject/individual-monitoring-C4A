package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVCategoryView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVTimeCategoryView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVTimeView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsCSVView;
import eu.city4age.dashboard.api.pojo.json.view.View.AnalyticsGraphView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AnalyticsDiagramData {
	
	@JsonInclude(Include.ALWAYS)
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVView.class})
	private String pilot;
	
	@JsonInclude(Include.ALWAYS)
	@JsonView (AnalyticsGraphView.class)
	private Long detectionVariable;
	
	@JsonInclude(Include.ALWAYS)
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVView.class})
	private String detectionVariableName;
	
	@JsonInclude(Include.ALWAYS)
	@JsonView (AnalyticsCSVView.class)
	private String detectionVariableType;
	
	@JsonIgnore
	private Map<String, String> category;
	
	@JsonView (AnalyticsGraphView.class)
	private String intervalStartJSON;
	
	@JsonView ({AnalyticsCSVTimeView.class, AnalyticsCSVTimeCategoryView.class})
	private String intervalStart;
	
	@JsonView ({AnalyticsCSVTimeView.class, AnalyticsCSVTimeCategoryView.class})
	private String typicalPeriod;
	
	@JsonInclude(Include.ALWAYS)
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVView.class})
	private BigDecimal avgValue;
	
	@JsonInclude(Include.ALWAYS)
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVView.class})
	private Long count;
	
	/**
	 * 
	 */
	public AnalyticsDiagramData() {
	}

	/**
	 * @return the pilot
	 */
	public String getPilot() {
		return pilot;
	}

	/**
	 * @param pilot the pilot to set
	 */
	public void setPilot(String pilot) {
		this.pilot = pilot;
	}

	/**
	 * @return the detectionVariable
	 */
	public Long getDetectionVariable() {
		return detectionVariable;
	}

	/**
	 * @param detectionVariable the detectionVariable to set
	 */
	public void setDetectionVariable(Long detectionVariable) {
		this.detectionVariable = detectionVariable;
	}

	/**
	 * @return the category
	 */
	@JsonAnyGetter
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVCategoryView.class, AnalyticsCSVTimeCategoryView.class})
	public Map<String, String> getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	@JsonAnySetter
	@JsonView ({AnalyticsGraphView.class, AnalyticsCSVCategoryView.class, AnalyticsCSVTimeCategoryView.class})
	public void setCategory(Map<String, String> category) {
		this.category = category;
	}

	/**
	 * @return the avgValue
	 */
	public BigDecimal getAvgValue() {
		return avgValue;
	}

	/**
	 * @param avgValue the avgValue to set
	 */
	public void setAvgValue(BigDecimal avgValue) {
		this.avgValue = avgValue;
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
	 * @return the intervalStartJSON
	 */
	public String getIntervalStartJSON() {
		return intervalStartJSON;
	}

	/**
	 * @param intervalStartJSON the intervalStartJSON to set
	 */
	public void setIntervalStartJSON(String intervalStartJSON) {
		this.intervalStartJSON = intervalStartJSON;
	}

	/**
	 * @return the intervalStart
	 */
	public String getIntervalStart() {
		return intervalStart;
	}

	/**
	 * @param intervalStartCSV the intervalStartCSV to set
	 */
	public void setIntervalStart(String intervalStart) {
		this.intervalStart = intervalStart;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

}
