package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.math.BigDecimal;
import java.util.List;

public class GroupAnalyticsSeries {
	
	private String name;
	
	private List<BigDecimal> items;
	
	private String pilot;
	
	private String assignedToY2;
	
	private String displayInLegend;
	
	public GroupAnalyticsSeries() {
		
	}

	public GroupAnalyticsSeries(String name, List<BigDecimal> items, String pilot) {
		this.name = name;
		this.items = items;
		this.pilot = pilot;
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
	 * @return the items
	 */
	public List<BigDecimal> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<BigDecimal> items) {
		this.items = items;
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
	 * @return the assignedToY2
	 */
	public String getAssignedToY2() {
		return assignedToY2;
	}

	/**
	 * @param assignedToY2 the assignedToY2 to set
	 */
	public void setAssignedToY2(String assignedToY2) {
		this.assignedToY2 = assignedToY2;
	}

	/**
	 * @return the displayInLegend
	 */
	public String getDisplayInLegend() {
		return displayInLegend;
	}

	/**
	 * @param displayInLegend the displayInLegend to set
	 */
	public void setDisplayInLegend(String displayInLegend) {
		this.displayInLegend = displayInLegend;
	}

}
