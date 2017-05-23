package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Last5Assessment {

	static protected Logger logger = LogManager.getLogger(Last5Assessment.class);

	private Long timeIntervalId, gefId, id;

	private String intervalStart, comment, riskStatusDesc, riskStatusImage, dataValidityDesc,
			dataValidityImage, from, dateAndTime;
	
	private Character riskStatus, dataValidity;

	private Float gefValue;

	public Last5Assessment(BigInteger timeIntervalId, Object intervalStart, BigInteger gefId, BigDecimal gefValue,
			Object assessmentId, String comment, Character riskStatus, Character dataValidity, Date created,
			String displayName) {

		if (timeIntervalId != null)
			this.timeIntervalId = Long.valueOf(timeIntervalId.toString());

		if (intervalStart != null)
			this.intervalStart = intervalStart.toString();

		if (gefId != null)
			this.gefId = Long.valueOf(gefId.toString());

		if (gefValue != null)
			this.gefValue = Float.valueOf(gefValue.toString());

		if (assessmentId != null)
			this.id = Long.parseLong(assessmentId.toString());

		if (comment != null)
			this.comment = comment;


		if (riskStatus != null) {
			this.riskStatus = riskStatus;
		}

		if (dataValidity != null) {
			this.dataValidity = dataValidity;
		}

		if (created != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.dateAndTime = sdf.format(created);
		}

		if (displayName != null) {
			this.from = displayName;
		} else {
			this.from = "No display name";
		}
	}
	
	public String toString() {
		return gefId + " " + id + " " + comment;
	}

	public Long getTimeIntervalId() {
		return timeIntervalId;
	}

	public void setTimeIntervalId(Long timeIntervalId) {
		this.timeIntervalId = timeIntervalId;
	}

	public String getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(String intervalStart) {
		this.intervalStart = intervalStart;
	}

	public Long getGefId() {
		return gefId;
	}

	public void setGefId(Long gefId) {
		this.gefId = gefId;
	}

	public Float getGefValue() {
		return gefValue;
	}

	public void setGefValue(Float gefValue) {
		this.gefValue = gefValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Character getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Character riskStatus) {
		this.riskStatus = riskStatus;
	}

	public String getRiskStatusDesc() {
		return riskStatusDesc;
	}

	public void setRiskStatusDesc(String riskStatusDesc) {
		this.riskStatusDesc = riskStatusDesc;
	}

	public String getRiskStatusImage() {
		return riskStatusImage;
	}

	public void setRiskStatusImage(String riskStatusImage) {
		this.riskStatusImage = riskStatusImage;
	}

	public Character getDataValidity() {
		return dataValidity;
	}

	public void setDataValidity(Character dataValidity) {
		this.dataValidity = dataValidity;
	}

	public String getDataValidityDesc() {
		return dataValidityDesc;
	}

	public void setDataValidityDesc(String dataValidityDesc) {
		this.dataValidityDesc = dataValidityDesc;
	}

	public String getDataValidityImage() {
		return dataValidityImage;
	}

	public void setDataValidityImage(String dataValidityImage) {
		this.dataValidityImage = dataValidityImage;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

}
