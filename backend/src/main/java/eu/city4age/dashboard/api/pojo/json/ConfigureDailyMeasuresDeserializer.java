package eu.city4age.dashboard.api.pojo.json;


import java.util.List;

import eu.city4age.dashboard.api.pojo.json.desobj.Groups;



public class ConfigureDailyMeasuresDeserializer {

	String name;
	Integer level;
	String dateUpdated;
	List<Groups> configTree;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<Groups> getConfigTree() {
		return configTree;
	}
	public void setConfigTree(List<Groups> configTree) {
		this.configTree = configTree;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

}
