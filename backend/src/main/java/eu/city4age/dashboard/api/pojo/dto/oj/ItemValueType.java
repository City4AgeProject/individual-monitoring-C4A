package eu.city4age.dashboard.api.pojo.dto.oj;

import java.io.Serializable;

public class ItemValueType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2800095291491540316L;

	private Float value;
	
	private String type;


	public ItemValueType(Float value, String type) {
		this.value = value;
		this.type = type;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
