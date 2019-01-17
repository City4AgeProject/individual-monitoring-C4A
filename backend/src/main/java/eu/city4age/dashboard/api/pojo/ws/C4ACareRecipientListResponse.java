/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.ws;

import eu.city4age.dashboard.api.pojo.domain.Pilot;

/**
 *
 * @author EMantziou
 */
public class C4ACareRecipientListResponse {
	
	private Long userId;

	private Integer age;

	private String frailtyStatus;

	private String frailtyNotice;

	private Character attention;

	private String textline;

	private Character interventionstatus;

	private String interventionDate;

	private String detectionStatus;

	private String detectionDate;
	
	private Pilot.PilotCode pilotCode;
	
	private String gender;
	
	private Integer interventions;

	public C4ACareRecipientListResponse(Long userId, Integer age, String frailtyStatus, String frailtyNotice, Character attention,
			String textline, Character interventionstatus, String interventionDate, String detectionStatus,
			String detectionDate, Pilot.PilotCode pilotCode,String gender) {
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
		this.gender = gender;
	}

	public C4ACareRecipientListResponse(Long userId, Integer age, String frailtyStatus, String frailtyNotice, Character attention,
        String textline, Character interventionstatus, String interventionDate, String detectionStatus,
        String detectionDate, Pilot.PilotCode pilotCode,String gender, Integer interventions) {
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
        this.gender = gender;
        this.interventions = interventions;
    }
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
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

	public Character getAttention() {
		return attention;
	}

	public void setAttention(Character attention) {
		this.attention = attention;
	}

	public String getTextline() {
		return textline;
	}

	public void setTextline(String textline) {
		this.textline = textline;
	}

	public Character getInterventionstatus() {
		return interventionstatus;
	}

	public void setInterventionstatus(Character interventionstatus) {
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
	
	public Pilot.PilotCode getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(Pilot.PilotCode pilotCode) {
		this.pilotCode = pilotCode;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

  public Integer getInterventions() {
    return interventions;
  }

  public void setInterventions(Integer interventions) {
    this.interventions = interventions;
  }

	
}// end class
