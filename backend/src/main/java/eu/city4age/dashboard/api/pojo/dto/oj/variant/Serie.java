package eu.city4age.dashboard.api.pojo.dto.oj.variant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Serie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5611558102192161051L;

	String name;

	List<BigDecimal> items = new ArrayList<BigDecimal>();

	public Serie(String name, List<BigDecimal> items) {
		this.name = name;
		this.items = items;
	}
	
	public Serie(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BigDecimal> getItems() {
		return items;
	}

	public void setItems(List<BigDecimal> items) {
		this.items = items;
	}

}
