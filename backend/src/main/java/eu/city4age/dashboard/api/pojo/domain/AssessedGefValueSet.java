package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "assessed_gef_value_set")
@IdClass(AssessedGefValueSetId.class)
public class AssessedGefValueSet implements Serializable {

	static protected Logger logger = LogManager.getLogger(AssessedGefValueSet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8878815954561540129L;

	@JsonIgnore
	@Id
	@Column(name = "gef_value_id", nullable = false)
	private Integer gefValueId;

	@JsonIgnore
	@Id
	@Column(name = "assessment_id", nullable = false)
	private Integer assessmentId;

	@Column(name = "json", columnDefinition = "jsonb")
	@Type(type = "JsonStringType")
    private String jsonString;

	public AssessedGefValueSet() {
	}

	public AssessedGefValueSet(AssessedGefValueSetBuilder builder) {
		this.gefValueId = builder.gefValueId;
		this.assessmentId = builder.assessmentId;
		this.jsonString = builder.jsonString;
	}

	public AssessedGefValueSet(Integer gefValueId, Integer assessmentId, String jsonString) {
		this.gefValueId = gefValueId;
		this.assessmentId = assessmentId;
		this.jsonString = jsonString;
	}

	public AssessedGefValueSet(BigInteger gefValueId, Integer assessmentId, String jsonString) {
		if (gefValueId != null && assessmentId != null) {
			this.gefValueId = Integer.valueOf(gefValueId.intValue());
			this.assessmentId = assessmentId;
			this.jsonString = jsonString;
		}
	}

	public Integer getGefValueId() {
		return gefValueId;
	}

	public void setGefValueId(Integer gefValueId) {
		this.gefValueId = gefValueId;
	}

	public Integer getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public static class AssessedGefValueSetBuilder {
		private Integer gefValueId = 0;
		private Integer assessmentId = 0;
		private String jsonString = "";

		public AssessedGefValueSetBuilder gefValueId(Integer gefValueId) {
			this.gefValueId = gefValueId;
			return this;
		}

		public AssessedGefValueSetBuilder assessmentId(Integer assessmentId) {
			this.assessmentId = assessmentId;
			return this;
		}
		
		public AssessedGefValueSetBuilder jsonString(String jsonString) {
			this.jsonString = jsonString;
			return this;
		}

		public AssessedGefValueSet build() {
			return new AssessedGefValueSet(this);
		}

	}

}
