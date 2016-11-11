/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.dto;

/**
 *
 * @author misha
 */
public class Annotation {

    Long id;
    String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
