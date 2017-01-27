/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto;

import java.util.ArrayList;

/**
 *
 * 
 */
public class DiagramDataPointSet {

	Long id;
	String label;
	ArrayList<DiagramDataPoint> diagramDataPoints = new ArrayList<DiagramDataPoint>();

	public DiagramDataPointSet() {

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

	public DiagramDataPointSet(Long id, String label) {
		this.id = id;
		this.label = label;
	}

	public ArrayList<DiagramDataPoint> getDiagramDataPoints() {
		return diagramDataPoints;
	}

	public void setDiagramDataPoints(ArrayList<DiagramDataPoint> diagramDataPoints) {
		this.diagramDataPoints = diagramDataPoints;
	}

}
