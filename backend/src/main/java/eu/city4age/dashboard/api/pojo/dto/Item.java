/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto;

/**
 *
 * @author mnou2
 */
public class Item {
    
    private Long id;
    private Float value;
    private Integer gefTypeId;
    

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
