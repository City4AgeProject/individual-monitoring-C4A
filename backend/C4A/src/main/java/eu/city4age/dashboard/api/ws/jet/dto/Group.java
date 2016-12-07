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
public class Group implements Serializable {
    
    private String name;
//    private List<Float> items;

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
//
//    /**
//     * @return the items
//     */
//    public List<Float> getItems() {
//        return items;
//    }
//
//    /**
//     * @param items the items to set
//     */
//    public void setItems(List<Float> items) {
//        this.items = items;
//    }
    
}
