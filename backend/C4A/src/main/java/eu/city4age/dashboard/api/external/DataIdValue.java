package eu.city4age.dashboard.api.external;

import java.io.Serializable;

public class DataIdValue implements Serializable {
	
	Long id;
	
	Double value;

	public DataIdValue(Long id, Double value) {
		this.id = id;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
