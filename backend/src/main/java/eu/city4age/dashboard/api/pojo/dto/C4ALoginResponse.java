/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.dto;

/**
 *
 * @author EMantziou
 */
public class C4ALoginResponse {
	private String message;

	private int responseCode;

	private String displayName;
	
	private short roleId;
	
	private String pilotName;
	
	private String pilotCode;

	public String getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}

	public String getpilotName() {
		return pilotName;
	}

	public void setpilotName(String pilotName) {
		this.pilotName = pilotName;
	}

	public short getRoleId() {
		return roleId;
	}

	public void setRoleId(short roleId) {
		this.roleId = roleId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
