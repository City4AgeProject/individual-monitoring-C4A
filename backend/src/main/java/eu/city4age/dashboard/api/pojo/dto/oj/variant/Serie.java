package eu.city4age.dashboard.api.pojo.dto.oj.variant;

import java.io.Serializable;
import java.util.List;

public class Serie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5611558102192161051L;

	String name;

	List<DataIdValue> items;

	public Serie(String name, List<DataIdValue> items) {
		this.name = name;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataIdValue> getItems() {
		return items;
	}

	public void setItems(List<DataIdValue> items) {
		this.items = items;
	}

}
