package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;
/**
 * @author Andrija Petrovic
 *
 */
public class Activity {
	
	private String type;
	private String start;
	private String end;
	private List<Gps> gps;
	private List<Bluetooth> bluetooth;
	private List<Wifi> wifi;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public List<Gps> getGps() {
		return gps;
	}
	public void setGps(List<Gps> gps) {
		this.gps = gps;
	}
	public List<Bluetooth> getBluetooth() {
		return bluetooth;
	}
	public void setBluetooth(List<Bluetooth> bluetooth) {
		this.bluetooth = bluetooth;
	}
	public List<Wifi> getWifi() {
		return wifi;
	}
	public void setWifi(List<Wifi> wifi) {
		this.wifi = wifi;
	}
}
