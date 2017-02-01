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
public class Serie {
    
    private String name;
    private List<Item> items = new ArrayList<Item>();
    private String source;
    private String imgSize = "20px";
    private Integer markerSize = 16;
    private String markerDisplayed = "off";
    private String lineType = "auto";

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
     * @return the imgSize
     */
    public String getImgSize() {
        return imgSize;
    }

    /**
     * @param imgSize the imgSize to set
     */
    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
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
    
}
