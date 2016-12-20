/*
 * City4Age Project
 * Horizon 2020  * 
 */
package rs.belit.c4a.jetdto;

import java.util.Date;

/**
 *
 * @author mnou2
 */
public class AssesmentForLastFives {
    
    private Long time_interval_id;
    private String interval_start;
    private Long gef_id;
    private Float gef_value;
    private Long assessment_id;
    private String assessment_comment, risk_status, data_validity_status, display_name;
    private Long author_id;
    private Date created;

    

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

    /**
     * @return the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * @param display_name the display_name to set
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return the time_interval_id
     */
    public Long getTime_interval_id() {
        return time_interval_id;
    }

    /**
     * @param time_interval_id the time_interval_id to set
     */
    public void setTime_interval_id(Long time_interval_id) {
        this.time_interval_id = time_interval_id;
    }
    
}
