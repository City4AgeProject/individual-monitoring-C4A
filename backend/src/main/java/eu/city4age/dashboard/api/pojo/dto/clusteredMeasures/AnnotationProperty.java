package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class AnnotationProperty {
	
	private String name;
	
	private String label;
	
	private String placeholder;
	
	private Boolean required;
	
	private Boolean multi;
	
	private List<SingleAnnotationProperty> propertiesValues;

	/**
	 * 
	 */
	public AnnotationProperty() {
	}

	/**
	 * @param name
	 * @param label
	 * @param placeholder
	 * @param required
	 * @param multi
	 * @param propertiesValues
	 */
	public AnnotationProperty(String name, String label, String placeholder, Boolean required, Boolean multi,
			List<SingleAnnotationProperty> propertiesValues) {
		this.name = name;
		this.label = label;
		this.placeholder = placeholder;
		this.required = required;
		this.multi = multi;
		this.propertiesValues = propertiesValues;
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the placeholder
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * @param placeholder the placeholder to set
	 */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the multi
	 */
	public Boolean getMulti() {
		return multi;
	}

	/**
	 * @param multi the multi to set
	 */
	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

	/**
	 * @return the propertiesValues
	 */
	public List<SingleAnnotationProperty> getPropertiesValues() {
		return propertiesValues;
	}

	/**
	 * @param propertiesValues the propertiesValues to set
	 */
	public void setPropertiesValues(List<SingleAnnotationProperty> propertiesValues) {
		this.propertiesValues = propertiesValues;
	}

}
