/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.domain;

/**
 *
 * @author misha
 */
public class DataPointSet {

    Long id;
    String label;

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

}
