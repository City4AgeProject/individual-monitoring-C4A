package eu.city4age.dashboard.api.domain;

import java.io.Serializable;

public class DataIdValue implements Serializable {
	
	Long id;
	
	String value;

	public DataIdValue(Long id, String value) {
		this.id = id;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
