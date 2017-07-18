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
public class C4ACareRecipientListResponse {
	private long userId;

	private int age;

	private String frailtyStatus;

	private String frailtyNotice;

	private char attention;

	private String textline;

	private char interventionstatus;

	private String interventionDate;

	private String detectionStatus;

	private String detectionDate;
	
	private String pilotCode;

	public C4ACareRecipientListResponse(long userId, int age, String frailtyStatus, String frailtyNotice, char attention,
			String textline, char interventionstatus, String interventionDate, String detectionStatus,
			String detectionDate, String pilotCode) {
		this.userId = userId;
		this.age = age;
		this.frailtyStatus = frailtyStatus;
		this.frailtyNotice = frailtyNotice;
		this.attention = attention;
		this.textline = textline;
		this.interventionstatus = interventionstatus;
		this.interventionDate = interventionDate;
		this.detectionStatus = detectionStatus;
		this.detectionDate = detectionDate;
		this.pilotCode = pilotCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFrailtyStatus() {
		return frailtyStatus;
	}

	public void setFrailtyStatus(String frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}

	public String getFrailtyNotice() {
		return frailtyNotice;
	}

	public void setFrailtyNotice(String frailtyNotice) {
		this.frailtyNotice = frailtyNotice;
	}

	public char getAttention() {
		return attention;
	}

	public void setAttention(char attention) {
		this.attention = attention;
	}

	public String getTextline() {
		return textline;
	}

	public void setTextline(String textline) {
		this.textline = textline;
	}

	public char getInterventionstatus() {
		return interventionstatus;
	}

	public void setInterventionstatus(char interventionstatus) {
		this.interventionstatus = interventionstatus;
	}

	public String getInterventionDate() {
		return interventionDate;
	}

	public void setInterventionDate(String interventionDate) {
		this.interventionDate = interventionDate;
	}

	public String getDetectionStatus() {
		return detectionStatus;
	}

	public void setDetectionStatus(String detectionStatus) {
		this.detectionStatus = detectionStatus;
	}

	public String getDetectionDate() {
		return detectionDate;
	}

	public void setDetectionDate(String detectionDate) {
		this.detectionDate = detectionDate;
	}
	
	public String getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(String pilotCode) {
		this.pilotCode = pilotCode;
	}

}// end class
