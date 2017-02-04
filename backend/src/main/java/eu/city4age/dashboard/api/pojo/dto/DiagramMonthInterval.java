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
public class DiagramMonthInterval {

	Integer start;
	Integer end;
	ArrayList<DiagramDataPointSet> diagramDataPointSets = new ArrayList<DiagramDataPointSet>();

	public DiagramMonthInterval() {
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public ArrayList<DiagramDataPointSet> getDiagramDataPointSets() {
		return diagramDataPointSets;
	}

	public void setDiagramDataPointSets(ArrayList<DiagramDataPointSet> diagramDataPointSets) {
		this.diagramDataPointSets = diagramDataPointSets;
	}
}