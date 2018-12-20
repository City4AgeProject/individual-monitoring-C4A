package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import java.util.List;

public class AddAssessmentClusterDeserializer {
	
	private String comment;
	
	private List<Long> dataIDs;
	
	private Character filterType;

	/**
	 * 
	 */
	public AddAssessmentClusterDeserializer() {
	}

	/**
	 * @param comment
	 * @param dataIDs
	 * @param filterType
	 */
	public AddAssessmentClusterDeserializer(String comment, List<Long> dataIDs, Character filterType) {
		this.comment = comment;
		this.dataIDs = dataIDs;
		this.filterType = filterType;
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
	 * @return the dataIDs
	 */
	public List<Long> getDataIDs() {
		return dataIDs;
	}

	/**
	 * @param dataIDs the dataIDs to set
	 */
	public void setDataIDs(List<Long> dataIDs) {
		this.dataIDs = dataIDs;
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
