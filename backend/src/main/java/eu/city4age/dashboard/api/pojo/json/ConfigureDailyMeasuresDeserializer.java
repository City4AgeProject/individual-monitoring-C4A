package eu.city4age.dashboard.api.pojo.json;

import java.util.List;

import eu.city4age.dashboard.api.pojo.json.desobj.Groups;

public class ConfigureDailyMeasuresDeserializer {

	String name;
	Integer level;
	String dateUpdated;
	String pilotCode;
	Long weight;
	String formula;
	
	List<Groups> groups;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPilotCode() {
		return pilotCode;
	}
	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}


}
