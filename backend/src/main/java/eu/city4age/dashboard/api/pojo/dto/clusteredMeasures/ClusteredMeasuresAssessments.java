package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.math.BigDecimal;
import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.FilterType;

public class ClusteredMeasuresAssessments {
	
	private Long id;
	
	private FilterType filterType;
	
	private String comment;
	
	private String createdOrUpdated;
	
	private String author;
	
	private List<String> dataIDs;
	
	private BigDecimal mean;
	
	private String minMaxValue;
	
	private String intervalStartIntervalEnd;
	
	private List<ClusteredVmv> measures;

	/**
	 * 
	 */
	public ClusteredMeasuresAssessments() {
	}

	/**
	 * @param id
	 * @param filterType
	 * @param comment
	 * @param createdOrUpdated
	 * @param author
	 * @param dataIDs
	 * @param mean
	 * @param minMaxValue
	 * @param intervalStartIntervalEnd
	 * @param measures
	 */
	public ClusteredMeasuresAssessments(Long id, FilterType filterType, String comment, String createdOrUpdated,
			String author, List<String> dataIDs, BigDecimal mean, String minMaxValue, String intervalStartIntervalEnd,
			List<ClusteredVmv> measures) {
		this.id = id;
		this.filterType = filterType;
		this.comment = comment;
		this.createdOrUpdated = createdOrUpdated;
		this.author = author;
		this.dataIDs = dataIDs;
		this.mean = mean;
		this.minMaxValue = minMaxValue;
		this.intervalStartIntervalEnd = intervalStartIntervalEnd;
		this.measures = measures;
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
	 * @return the filterType
	 */
	public FilterType getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(FilterType filterType) {
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
	 * @return the createdOrUpdated
	 */
	public String getCreatedOrUpdated() {
		return createdOrUpdated;
	}

	/**
	 * @param createdOrUpdated the createdOrUpdated to set
	 */
	public void setCreatedOrUpdated(String createdOrUpdated) {
		this.createdOrUpdated = createdOrUpdated;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the dataIDs
	 */
	public List<String> getDataIDs() {
		return dataIDs;
	}

	/**
	 * @param dataIDs the dataIDs to set
	 */
	public void setDataIDs(List<String> dataIDs) {
		this.dataIDs = dataIDs;
	}

	/**
	 * @return the mean
	 */
	public BigDecimal getMean() {
		return mean;
	}

	/**
	 * @param mean the mean to set
	 */
	public void setMean(BigDecimal mean) {
		this.mean = mean;
	}

	/**
	 * @return the minMaxValue
	 */
	public String getMinMaxValue() {
		return minMaxValue;
	}

	/**
	 * @param minMaxValue the minMaxValue to set
	 */
	public void setMinMaxValue(String minMaxValue) {
		this.minMaxValue = minMaxValue;
	}

	/**
	 * @return the intervalStartIntervalEnd
	 */
	public String getIntervalStartIntervalEnd() {
		return intervalStartIntervalEnd;
	}

	/**
	 * @param intervalStartIntervalEnd the intervalStartIntervalEnd to set
	 */
	public void setIntervalStartIntervalEnd(String intervalStartIntervalEnd) {
		this.intervalStartIntervalEnd = intervalStartIntervalEnd;
	}

	/**
	 * @return the measures
	 */
	public List<ClusteredVmv> getMeasures() {
		return measures;
	}

	/**
	 * @param measures the measures to set
	 */
	public void setMeasures(List<ClusteredVmv> measures) {
		this.measures = measures;
	}
}
