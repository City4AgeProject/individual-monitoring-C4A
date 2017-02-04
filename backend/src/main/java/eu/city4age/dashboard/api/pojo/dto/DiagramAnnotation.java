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
public class DiagramAnnotation {

	Long id;
	String comments;
	ArrayList<DiagramDataPoint> diagramDataPoints = new ArrayList<DiagramDataPoint>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ArrayList<DiagramDataPoint> getDiagramDataPoints() {
		return diagramDataPoints;
	}

	public void setDiagramDataPoints(ArrayList<DiagramDataPoint> diagramDataPoints) {
		this.diagramDataPoints = diagramDataPoints;
	}

}
