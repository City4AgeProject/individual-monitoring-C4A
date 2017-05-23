package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="cd_data_source_type")
public class DataSourceType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8134429096564215602L;

	@Id
	@Column(name="data_source_type")
	private String dataSourceType;
	
	@Column(name="data_source_type_description")
	private String dataSourceTypeDescription;
	
	/*@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="cdDataSourceType",fetch=FetchType.LAZY)
	private Set<VariationMeasureValue> variationMeasureValues = new HashSet<VariationMeasureValue>(0);*/
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="cdDataSourceType",fetch=FetchType.LAZY)
	private Set<Activity> activities = new HashSet<Activity>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="cdDataSourceType",fetch=FetchType.LAZY)
	private Set<NumericIndicatorValue> numericIndicatorValues = new HashSet<NumericIndicatorValue>(0);

	public DataSourceType() {
	}

	public DataSourceType(String dataSourceType, String dataSourceTypeDescription) {
		this.dataSourceType = dataSourceType;
		this.dataSourceTypeDescription = dataSourceTypeDescription;
	}

	public DataSourceType(String dataSourceType, String dataSourceTypeDescription, 
			//Set<VariationMeasureValue> variationMeasureValues,
			Set<Activity> activities, Set<NumericIndicatorValue> numericIndicatorValues) {
		this.dataSourceType = dataSourceType;
		this.dataSourceTypeDescription = dataSourceTypeDescription;
		//this.variationMeasureValues = variationMeasureValues;
		this.activities = activities;
		this.numericIndicatorValues = numericIndicatorValues;
	}

	public String getDataSourceType() {
		return this.dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourceTypeDescription() {
		return this.dataSourceTypeDescription;
	}

	public void setDataSourceTypeDescription(String dataSourceTypeDescription) {
		this.dataSourceTypeDescription = dataSourceTypeDescription;
	}

	/*public Set<VariationMeasureValue> getVariationMeasureValues() {
		return this.variationMeasureValues;
	}

	public void setVariationMeasureValues(Set<VariationMeasureValue> variationMeasureValues) {
		this.variationMeasureValues = variationMeasureValues;
	}*/

	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<NumericIndicatorValue> getNumericIndicatorValues() {
		return this.numericIndicatorValues;
	}

	public void setNumericIndicatorValues(Set<NumericIndicatorValue> numericIndicatorValues) {
		this.numericIndicatorValues = numericIndicatorValues;
	}

}
