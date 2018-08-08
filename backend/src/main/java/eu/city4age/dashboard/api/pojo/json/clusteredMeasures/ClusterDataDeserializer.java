package eu.city4age.dashboard.api.pojo.json.clusteredMeasures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClusterDataDeserializer {
	
	private String name;
	
	private List<String> items;

	/**
	 * 
	 */
	public ClusterDataDeserializer() {
	}

	/**
	 * @param name
	 * @param items
	 */
	public ClusterDataDeserializer(String name, List<Object> items) {
		this.name = name;
		List<String> itemsList = new ArrayList<String> ();
		for (Object item : items) {
			if (item.toString().equals("null")) itemsList.add(null);
			else itemsList.add(((BigDecimal) item).toString());
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the items
	 */
	public List<String> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<String> items) {
		this.items = items;
	}
}
