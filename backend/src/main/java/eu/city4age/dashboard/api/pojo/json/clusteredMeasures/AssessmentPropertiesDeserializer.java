package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import java.util.List;

public class AssessmentPropertiesDeserializer {
	
	private String name;
	
	private List<String> value;

	/**
	 * 
	 */
	public AssessmentPropertiesDeserializer() {
	}

	/**
	 * @param name
	 * @param value
	 */
	public AssessmentPropertiesDeserializer(String name, List<String> value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public List<String> getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(List<String> value) {
		this.value = value;
	}

}
