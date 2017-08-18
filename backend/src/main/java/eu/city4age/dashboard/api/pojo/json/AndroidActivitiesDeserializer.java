package eu.city4age.dashboard.api.pojo.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.city4age.dashboard.api.pojo.json.desobj.Activity;


public class AndroidActivitiesDeserializer {

	private Long id;
	private String date;
	private List<Activity> activities;
	
	@JsonProperty("ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

}
