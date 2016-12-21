/*
 * City4Age Project
 * Horizon 2020  * 
 */
package rs.belit.c4a.jetdto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mnou2
 */
public class Item {
    
    private Long id;
    private Float value;
    private Integer gefTypeId;
    private List<Assessment> assessmentObjects = new ArrayList<Assessment>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * @return the assessmentObjects
     */
    public List<Assessment> getAssessmentObjects() {
        return assessmentObjects;
    }

    /**
     * @param assessmentObjects the assessmentObjects to set
     */
    public void setAssessmentObjects(List<Assessment> assessmentObjects) {
        this.assessmentObjects = assessmentObjects;
    }

    /**
     * @return the gefTypeId
     */
    public Integer getGefTypeId() {
        return gefTypeId;
    }

    /**
     * @param gefTypeId the gefTypeId to set
     */
    public void setGefTypeId(Integer gefTypeId) {
        this.gefTypeId = gefTypeId;
    }
    
    
    
}
