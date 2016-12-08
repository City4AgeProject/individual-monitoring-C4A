/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet.dto;

import eu.city4age.dashboard.api.model.Assessment;
import java.util.Objects;

/**
 *
 * @author mnou2
 */
public class Annotation {
    
    private Long id;
    private String title;
    private String type;
    private String from;
    private String comment;
    private String imgSrc;

    public Annotation() {}
    
    public Annotation(Assessment toCreateFrom) {
        id = toCreateFrom.getId();
        title = toCreateFrom.getDataValidityStatus().toString();
        type = toCreateFrom.getRiskStatus().toString();
        from = toCreateFrom.getUserInRole().getRoleId().toString();
        if("A".equals(toCreateFrom.getRiskStatus().toString()))
            imgSrc = "images/risk_alert.png";
        else if("W".equals(toCreateFrom.getRiskStatus().toString()))
            imgSrc = "images/risk_warning.png";
        else
            imgSrc = "images/comment.png";
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the imgSrc
     */
    public String getImgSrc() {
        return imgSrc;
    }

    /**
     * @param imgSrc the imgSrc to set
     */
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Annotation other = (Annotation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
