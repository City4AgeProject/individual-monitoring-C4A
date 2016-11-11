/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.dto;

import java.util.ArrayList;

/**
 *
 * @author misha
 */
public class DiagramDataPointSet {

    Long id;
    String label;
    ArrayList<DiagramDataPoint> diagramDataPoints = new ArrayList<DiagramDataPoint>();

    public DiagramDataPointSet() {

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
