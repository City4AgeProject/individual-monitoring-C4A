package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LastFiveAssessment {

	static protected Logger logger = LogManager.getLogger(LastFiveAssessment.class);

	private Long timeIntervalId, gefId, id;

	private String intervalStart, comment, riskStatus, riskStatusDesc, riskStatusImage, dataValidity, dataValidityDesc,
			dataValidityImage, from, dateAndTime;

	private Float gefValue;

	public LastFiveAssessment(BigInteger timeIntervalId, Object intervalStart, BigInteger gefId, BigDecimal gefValue,
			Integer assessmentId, String comment, Character riskStatus, Character dataValidity, Date created,
			String displayName) {

		this.timeIntervalId = Long.valueOf(timeIntervalId.toString());

		this.intervalStart = intervalStart.toString();

		this.gefId = Long.valueOf(gefId.toString());

		this.gefValue = Float.valueOf(gefValue.toString());

		if (assessmentId != null)
			this.id = assessmentId.longValue();

		if (comment != null)
			this.comment = comment;

		/*
		if (riskStatus != null) {
			this.riskStatus = riskStatus.toString();
			switch (riskStatus) {
			case 'A':
				this.riskStatusDesc = "Alert";
				this.riskStatusImage = "images/risk_alert_left.png";
				break;
			case 'W':
				this.riskStatusDesc = "Warning";
				this.riskStatusImage = "images/risk_warning.png";
				break;
			default:
				this.riskStatusDesc = "Comment";
				this.riskStatusImage = "images/comment.png";
				break;
			}
		}

		if (dataValidity != null) {
			this.dataValidity = dataValidity.toString();
			switch (dataValidity) {
			case 'F':
				this.dataValidityDesc = "Faulty data";
				this.dataValidityImage = "images/faulty_data.png";
				break;
			case 'Q':
				this.dataValidityDesc = "Questionable data";
				this.dataValidityImage = "images/questionable_data.png";
				break;
			case 'V':
				this.dataValidityDesc = "Valid data";
				this.dataValidityImage = "images/valid_data.png";
				break;
			}
		}*/

		if (created != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
			this.dateAndTime = sdf.format(created);
		}

		if (displayName != null) {
			this.from = displayName;
		} else {
			this.from = "No display name";
		}
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

	public String getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
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

	public String getDataValidity() {
		return dataValidity;
	}

	public void setDataValidity(String dataValidity) {
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
