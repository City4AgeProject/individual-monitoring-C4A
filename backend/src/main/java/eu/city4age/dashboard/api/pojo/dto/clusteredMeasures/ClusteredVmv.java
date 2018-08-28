package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.math.BigDecimal;

public class ClusteredVmv {
	
	private BigDecimal value;
	
	private String intervalStart;
	
	private String imagePath;
	
	private String filterDescription;

	/**
	 * 
	 */
	public ClusteredVmv() {
	}

	/**
	 * @param value
	 * @param intervalStart
	 * @param imagePath
	 * @param filterDescription
	 */
	public ClusteredVmv(BigDecimal value, String intervalStart, String imagePath, String filterDescription) {
		this.value = value;
		this.intervalStart = intervalStart;
		this.imagePath = imagePath;
		this.filterDescription = filterDescription;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the intervalStart
	 */
	public String getIntervalStart() {
		return intervalStart;
	}

	/**
	 * @param intervalStart the intervalStart to set
	 */
	public void setIntervalStart(String intervalStart) {
		this.intervalStart = intervalStart;
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

	/**
	 * @return the filterDescription
	 */
	public String getFilterDescription() {
		return filterDescription;
	}

	/**
	 * @param filterDescription the filterDescription to set
	 */
	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}
}
