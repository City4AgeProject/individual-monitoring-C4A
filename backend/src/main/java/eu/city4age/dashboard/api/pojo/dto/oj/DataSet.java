/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto.oj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.Response;
import eu.city4age.dashboard.api.pojo.dto.Status;
import eu.city4age.dashboard.api.pojo.dto.oj.variant.next.Serie;

/**
 *
 * @author mnou2
 */
public class DataSet extends Status implements Response {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -18477891286808651L;


	private Set<DataIdValue> groups = new TreeSet<DataIdValue>();
 
    private List<Serie> series = new ArrayList<Serie>();
    
    private OJDiagramFrailtyStatus frailtyStatus;
    
	/**
     * @return the groups
     */
    public Set<DataIdValue> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Set<DataIdValue> groups) {
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

	public OJDiagramFrailtyStatus getFrailtyStatus() {
		return frailtyStatus;
	}

	public void setFrailtyStatus(OJDiagramFrailtyStatus frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}

}
