/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mnou2
 */
public class DataSet extends  C4AGroupsResponse {
    
    private List<Group> groups = new ArrayList<Group>();
    private List<Serie> series = new ArrayList<Serie>();

    /**
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups) {
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
