package eu.city4age.dashboard.api.pojo.persist;

import java.util.HashMap;
import java.util.Map;

public class Filter {

	private String name;

	private Map<String, Object> inParams = new HashMap<String, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getInParams() {
		return inParams;
	}

	public void setInParams(Map<String, Object> inParams) {
		this.inParams = inParams;
	}

}
