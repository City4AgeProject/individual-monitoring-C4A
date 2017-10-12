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
	private List<Gps> gpss;
	private List<Bluetooth> bluetooths;
	private List<Wifi> wifis;
	
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
	public List<Gps> getGpss() {
		return gpss;
	}
	public void setGpss(List<Gps> gpss) {
		this.gpss = gpss;
	}
	public List<Bluetooth> getBluetooths() {
		return bluetooths;
	}
	public void setBluetooths(List<Bluetooth> bluetooths) {
		this.bluetooths = bluetooths;
	}
	public List<Wifi> getWifis() {
		return wifis;
	}
	public void setWifis(List<Wifi> wifis) {
		this.wifis = wifis;
	}
}
