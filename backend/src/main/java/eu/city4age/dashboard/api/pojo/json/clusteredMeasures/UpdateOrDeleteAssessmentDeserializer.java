package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateOrDeleteAssessmentDeserializer {
	
	private Character updateOrDelete;
	
	private Long assessmentId;
	
	private String comment;
	
	private Character filterType;

	/**
	 * 
	 */
	public UpdateOrDeleteAssessmentDeserializer() {
	}

	/**
	 * @param updateOrDelete
	 * @param assessmentId
	 * @param comment
	 * @param filterType
	 */
	public UpdateOrDeleteAssessmentDeserializer(@JsonProperty (value = "updateOrDelete", required = true) Character updateOrDelete, 
												@JsonProperty (value = "assessmentId", required = true) Long assessmentId, 
												@JsonProperty (value = "comment", required = false) String comment,
												@JsonProperty (value = "filterType", required = false) Character filterType) {
		this.updateOrDelete = updateOrDelete;
		this.assessmentId = assessmentId;
		this.comment = comment;
		this.filterType = filterType;
	}

	/**
	 * @return the updateOrDelete
	 */
	public Character getUpdateOrDelete() {
		return updateOrDelete;
	}

	/**
	 * @param updateOrDelete the updateOrDelete to set
	 */
	public void setUpdateOrDelete(Character updateOrDelete) {
		this.updateOrDelete = updateOrDelete;
	}

	/**
	 * @return the assessmentId
	 */
	public Long getAssessmentId() {
		return assessmentId;
	}

	/**
	 * @param assessmentId the assessmentId to set
	 */
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
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
	 * @return the filterType
	 */
	public Character getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(Character filterType) {
		this.filterType = filterType;
	}

}
