package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewGefCalculatedInterpolatedPredictedValuesKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8142020035973363403L;

	@Column(name = "id")
	private Long id;
	
	@Column(name = "data_type")
	private String dataType;


	public ViewGefCalculatedInterpolatedPredictedValuesKey() {
		
	}
	
	public ViewGefCalculatedInterpolatedPredictedValuesKey(Long id, String dataType) {
		this.id = id;
		this.dataType = dataType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


}
