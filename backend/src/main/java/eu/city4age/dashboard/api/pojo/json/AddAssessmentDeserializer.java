package eu.city4age.dashboard.api.pojo.json;

import java.util.List;

import eu.city4age.dashboard.api.pojo.enu.DataValidity;

public class AddAssessmentDeserializer {

	String jwt;

	String comment;

	Character riskStatus;

	DataValidity dataValidity;

	List<Long> geriatricFactorValueIds;

	List<Long> audienceIds;
	
	String type;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public List<Long> getGeriatricFactorValueIds() {
		return geriatricFactorValueIds;
	}

	public void setGeriatricFactorValueIds(List<Long> geriatricFactorValueIds) {
		this.geriatricFactorValueIds = geriatricFactorValueIds;
	}

	public Character getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Character riskStatus) {
		this.riskStatus = riskStatus;
	}

	public DataValidity getDataValidity() {
		return dataValidity;
	}

	public void setDataValidity(DataValidity dataValidity) {
		this.dataValidity = dataValidity;
	}

	public List<Long> getAudienceIds() {
		return audienceIds;
	}

	public void setAudienceIds(List<Long> audienceIds) {
		this.audienceIds = audienceIds;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
