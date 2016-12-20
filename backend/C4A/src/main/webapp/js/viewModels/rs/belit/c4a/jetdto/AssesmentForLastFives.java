/*
 * City4Age Project
 * Horizon 2020  * 
 */
package rs.belit.c4a.jetdto;

/**
 *
 * @author mnou2
 */
public class AssesmentForLastFives {
    
    private Long id;
    private String interval_start;
    private Long gef_id;
    private Float gef_value;
    private Long assessment_id;
    private String assessment_comment, risk_status, data_validity_status;
    private Long author_id;

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
     * @return the interval_start
     */
    public String getInterval_start() {
        return interval_start;
    }

    /**
     * @param interval_start the interval_start to set
     */
    public void setInterval_start(String interval_start) {
        this.interval_start = interval_start;
    }

    /**
     * @return the gef_id
     */
    public Long getGef_id() {
        return gef_id;
    }

    /**
     * @param gef_id the gef_id to set
     */
    public void setGef_id(Long gef_id) {
        this.gef_id = gef_id;
    }

    /**
     * @return the gef_value
     */
    public Float getGef_value() {
        return gef_value;
    }

    /**
     * @param gef_value the gef_value to set
     */
    public void setGef_value(Float gef_value) {
        this.gef_value = gef_value;
    }

    /**
     * @return the assessment_id
     */
    public Long getAssessment_id() {
        return assessment_id;
    }

    /**
     * @param assessment_id the assessment_id to set
     */
    public void setAssessment_id(Long assessment_id) {
        this.assessment_id = assessment_id;
    }

    /**
     * @return the assessment_comment
     */
    public String getAssessment_comment() {
        return assessment_comment;
    }

    /**
     * @param assessment_comment the assessment_comment to set
     */
    public void setAssessment_comment(String assessment_comment) {
        this.assessment_comment = assessment_comment;
    }

    /**
     * @return the risk_status
     */
    public String getRisk_status() {
        return risk_status;
    }

    /**
     * @param risk_status the risk_status to set
     */
    public void setRisk_status(String risk_status) {
        this.risk_status = risk_status;
    }

    /**
     * @return the data_validity_status
     */
    public String getData_validity_status() {
        return data_validity_status;
    }

    /**
     * @param data_validity_status the data_validity_status to set
     */
    public void setData_validity_status(String data_validity_status) {
        this.data_validity_status = data_validity_status;
    }

    /**
     * @return the author_id
     */
    public Long getAuthor_id() {
        return author_id;
    }

    /**
     * @param author_id the author_id to set
     */
    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }
    
}
