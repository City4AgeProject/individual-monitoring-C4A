/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.dto;

import eu.city4age.dashboard.api.domain.DataPointSet;
import java.util.ArrayList;

/**
 *
 * @author misha
 */
public class DiagramDataPointSet {

    DataPointSet dataSet;
    ArrayList<DiagramDataPoint> diagramDataPoints = new ArrayList<DiagramDataPoint>();

    
    public DiagramDataPointSet(){
        
    }
    
    public DiagramDataPointSet(DataPointSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataPointSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataPointSet dataSet) {
        this.dataSet = dataSet;
    }

    public ArrayList<DiagramDataPoint> getDiagramDataPoints() {
        return diagramDataPoints;
    }

    public void setDiagramDataPoints(ArrayList<DiagramDataPoint> diagramDataPoints) {
        this.diagramDataPoints = diagramDataPoints;
    }

}
