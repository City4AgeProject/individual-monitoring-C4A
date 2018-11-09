package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CorrelationData {

	private String detectionVariableName;
	
	@JsonIgnore
	private Map<String, Double> values;

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
	 * @return the values
	 */
	@JsonAnyGetter
	public Map<String, Double> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	@JsonAnySetter
	public void setValues(Map<String, Double> values) {
		this.values = values;
	}
	
	
}
