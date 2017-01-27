/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto.oj;

import java.util.ArrayList;

/**
 *
 * 
 */
public class DataPointSet {

	Long id;
	String label;

	ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

	public DataPointSet() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(ArrayList<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

}
