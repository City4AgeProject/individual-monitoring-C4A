/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

/**
 *
 * @author mnou2
 */
public class Item {
    
    private Long id;
    private Float value;
    private String type;
    private Integer gefTypeId;
    private Long timeIntervalId;
    private String monthLabel;
    
    

    public Item(Long id, BigDecimal value, String type, Long gefTypeId, Long timeIntervalId) {
		this.id = id;
		this.value = value.floatValue();
		this.type = type;
		this.gefTypeId = gefTypeId.intValue();
		this.timeIntervalId = timeIntervalId;
	}
    
    public Item(BigDecimal value, Long gefTypeId, Long timeIntervalId) {
		this.value = value.floatValue();
		this.gefTypeId = gefTypeId.intValue();
		this.timeIntervalId = timeIntervalId;
	}
    
    public Item(BigDecimal value, Long gefTypeId, Long timeIntervalId, String monthLabel) {
		this.value = value.floatValue();
		this.gefTypeId = gefTypeId.intValue();
		this.timeIntervalId = timeIntervalId;
		this.monthLabel = monthLabel;
	}

	public Item() {
	}

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

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Long getTimeIntervalId() {
		return timeIntervalId;
	}

	public void setTimeIntervalId(Long timeIntervalId) {
		this.timeIntervalId = timeIntervalId;
	}

	/**
	 * @return the monthLabel
	 */
	public String getMonthLabel() {
		return monthLabel;
	}

	/**
	 * @param monthLabel the monthLabel to set
	 */
	public void setMonthLabel(String monthLabel) {
		this.monthLabel = monthLabel;
	}
    
}
