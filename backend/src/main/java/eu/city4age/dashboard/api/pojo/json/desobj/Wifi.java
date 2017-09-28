package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

public class Wifi {
	
	private String date;
	private List<String> devices;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<String> getDevices() {
		return devices;
	}
	public void setDevices(List<String> devices) {
		this.devices = devices;
	}
	

}
