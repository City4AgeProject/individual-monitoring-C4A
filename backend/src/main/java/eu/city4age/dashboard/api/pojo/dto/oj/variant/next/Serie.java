package eu.city4age.dashboard.api.pojo.dto.oj.variant.next;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.pojo.dto.Item;

public class Serie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5611558102192161051L;

	String name;

	List<Item> items = new ArrayList<Item>();

	public Serie(String name, List<Item> items) {
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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
