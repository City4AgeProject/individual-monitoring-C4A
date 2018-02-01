package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column (name = "obtrusive")
	private Boolean obtrusive;

	public DataSourceType() {
	}

	public DataSourceType(String dataSourceType, String dataSourceTypeDescription, Boolean obtrusive) {
		this.dataSourceType = dataSourceType;
		this.dataSourceTypeDescription = dataSourceTypeDescription;
		this.obtrusive = obtrusive;
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

	public Boolean getObtrusive() {
		return obtrusive;
	}

	public void setObtrusive(Boolean obtrusive) {
		this.obtrusive = obtrusive;
	}
}