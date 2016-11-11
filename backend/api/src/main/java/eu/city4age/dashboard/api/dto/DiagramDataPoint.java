/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.dto;

/**
 *
 * @author misha
 */
public class DiagramDataPoint {

    Long id;
    Float value;
    Annotation annotation;

    public DiagramDataPoint() {
    }

    public DiagramDataPoint(Long id, Float value, Annotation annotation) {
        this.id = id;
        this.value = value;
        this.annotation = annotation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

}
