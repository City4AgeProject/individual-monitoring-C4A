package eu.city4age.dashboard.api.pojo.dto.clusteredMeasures;

import java.util.List;

public class ClusteredMeasuresGroups {
	
	private String name;
	
	private List<String> groups;

	/**
	 * 
	 */
	public ClusteredMeasuresGroups() {
	}

	/**
	 * @param name
	 * @param groups
	 */
	public ClusteredMeasuresGroups(String name, List<String> groups) {
		this.name = name;
		this.groups = groups;
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
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

}
