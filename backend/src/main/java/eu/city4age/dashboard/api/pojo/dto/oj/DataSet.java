/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto.oj;

import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.pojo.dto.Status;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.Serie;

/**
 *
 * @author mnou2
 */
public class DataSet extends Status {
    
    private List<String> groups = new ArrayList<String>();
 
    private List<Serie> series = new ArrayList<Serie>();

    /**
     * @return the groups
     */
    public List<String> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    /**
     * @return the series
     */
    public List<Serie> getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    
    
}
