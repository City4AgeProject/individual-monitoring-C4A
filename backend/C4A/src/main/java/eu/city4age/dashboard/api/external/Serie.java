package eu.city4age.dashboard.api.external;

import java.io.Serializable;
import java.util.List;

public class Serie implements Serializable {
	
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
