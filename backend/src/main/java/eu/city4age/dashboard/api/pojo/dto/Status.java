package eu.city4age.dashboard.api.pojo.dto;

import java.io.Serializable;

public class Status implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5098805329397047578L;
	

	private String responseCode;
	
	private String console;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}
	

}
