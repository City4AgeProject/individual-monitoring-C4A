package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

public class SingleAnnotationProperty {
	
	private String value;
	
	private String label;
	
	private String imagePath;

	/**
	 * 
	 */
	public SingleAnnotationProperty() {
	}

	/**
	 * @param value
	 * @param label
	 * @param imagePath
	 */
	public SingleAnnotationProperty(String value, String label, String imagePath) {
		this.value = value;
		this.label = label;
		this.imagePath = imagePath;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
