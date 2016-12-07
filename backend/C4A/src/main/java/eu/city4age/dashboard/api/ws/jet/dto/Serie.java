/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mnou2
 */
public class Serie implements Serializable {
    
    private String name;
    private List<Item> items;

    private String color;
    private String source;
    private String lineType;
    private String markerDisplayed;
    private Integer markerSize;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the lineType
     */
    public String getLineType() {
        return lineType;
    }

    /**
     * @param lineType the lineType to set
     */
    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    /**
     * @return the markerDisplayed
     */
    public String getMarkerDisplayed() {
        return markerDisplayed;
    }

    /**
     * @param markerDisplayed the markerDisplayed to set
     */
    public void setMarkerDisplayed(String markerDisplayed) {
        this.markerDisplayed = markerDisplayed;
    }

    /**
     * @return the markerSize
     */
    public Integer getMarkerSize() {
        return markerSize;
    }

    /**
     * @param markerSize the markerSize to set
     */
    public void setMarkerSize(Integer markerSize) {
        this.markerSize = markerSize;
    }

}
