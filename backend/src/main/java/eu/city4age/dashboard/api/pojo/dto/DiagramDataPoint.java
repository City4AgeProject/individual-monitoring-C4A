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
public class DiagramDataPoint {

	Long id;
	Float value;
	ArrayList<DiagramAnnotation> diagramAnnotations;

	public DiagramDataPoint() {
	}

	public DiagramDataPoint(Long id, Float value, ArrayList<DiagramAnnotation> diagramAnnotations) {
		this.id = id;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public ArrayList<DiagramAnnotation> getDiagramAnnotations() {
		return diagramAnnotations;
	}

	public void setDiagramAnnotations(ArrayList<DiagramAnnotation> diagramAnnotations) {
		this.diagramAnnotations = diagramAnnotations;
	}

}
