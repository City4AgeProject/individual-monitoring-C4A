package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class ViewCorrelationDataKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "id")
	private Long id;
	
	@Column(name = "data_type")
	private String dataType;

	/**
	 * 
	 */
	public ViewCorrelationDataKey() {
	}

	/**
	 * @param id
	 * @param dataType
	 */
	public ViewCorrelationDataKey(Long id, String dataType) {
		this.id = id;
		this.dataType = dataType;
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
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewCorrelationDataKey))
			return false;
		ViewCorrelationDataKey castOther = (ViewCorrelationDataKey) other;

		return (this.getId().equals(castOther.getId())
				&& this.getDataType().equals(castOther.getDataType()));
	}
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getId().intValue();
		result = 37 * result + this.getDataType().charAt(0);
		return result;
	}

}
