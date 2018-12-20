package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cd_filter_type")
public class FilterType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9015780547684012397L;
	
	@Id
	@Column(name="filter_type")
	private Character filterType;
	
	@Column(name="filter_description")
	private String filterDescription;
	
	@Column(name="image_path")
	private String imagePath;

	/**
	 * 
	 */
	public FilterType() {
	}

	/**
	 * @param filterType
	 * @param filterDescription
	 * @param imagePath
	 */
	public FilterType(Character filterType, String filterDescription, String imagePath) {
		this.filterType = filterType;
		this.filterDescription = filterDescription;
		this.imagePath = imagePath;
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

	/**
	 * @return the filterDescription
	 */
	public String getFilterDescription() {
		return filterDescription;
	}

	/**
	 * @param filterDescription the filterDescription to set
	 */
	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
