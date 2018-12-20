package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name = "vw_group_analytics_data")
@Immutable
@FilterDefs(value = { 
		@FilterDef(name = "detectionVariable", parameters = @ParamDef(name = "detectionVariable", type = "long")),
		@FilterDef(name = "pilot", parameters = @ParamDef(name = "pilot", type = "string")),
		@FilterDef(name = "intervalStart", parameters = @ParamDef(name = "intervalStart", type = "timestamp")),
		@FilterDef(name = "intervalEnd", parameters = @ParamDef(name = "intervalEnd", type = "timestamp")),
		@FilterDef(name = "sex", parameters = @ParamDef(name = "sex", type = "string")),
		@FilterDef(name = "marital_status", parameters = @ParamDef(name = "marital_status", type = "string")),
		@FilterDef(name = "age_group", parameters = @ParamDef(name = "age_group", type = "string")),
		@FilterDef(name = "cohabiting", parameters = @ParamDef(name = "cohabiting", type = "string")),
		@FilterDef(name = "education", parameters = @ParamDef(name = "education", type = "string")),
		@FilterDef(name = "informal_caregiver_ability", parameters = @ParamDef(name = "informal_caregiver_ability", type = "string")),
		@FilterDef(name = "quality_housing", parameters = @ParamDef(name = "quality_housing", type = "string")),
		@FilterDef(name = "quality_neighborhood", parameters = @ParamDef(name = "quality_neighborhood", type = "string")),
		@FilterDef(name = "working", parameters = @ParamDef(name = "working", type = "string"))
})
@Filters(value = {
		@Filter(name = "detectionVariable", condition = "detection_variable_id IN (:detectionVariable) "),
		@Filter(name = "pilot", condition = "pilot_code IN (:pilot) "),
		@Filter(name = "intervalStart", condition = "interval_start >= :intervalStart"),
		@Filter(name = "intervalEnd", condition = "interval_start < :intervalEnd"),
		@Filter(name = "sex", condition = "sex = :sex"),
		@Filter(name = "marital_status", condition = "marital_status = :marital_status"),
		@Filter(name = "age_group", condition = "age_group = :age_group"),
		@Filter(name = "cohabiting", condition = "cohabiting = :cohabiting"),
		@Filter(name = "education", condition = "education = :education"),
		@Filter(name = "informal_caregiver_ability", condition = "informal_caregiver_ability = :informal_caregiver_ability"),
		@Filter(name = "quality_housing", condition = "quality_housing = :quality_housing"),
		@Filter(name = "quality_neighborhood", condition = "quality_neighborhood = :quality_neighborhood"),
		@Filter(name = "working", condition = "working = :working")
})
public class ViewGroupAnalyticsData {

	@EmbeddedId
	private ViewGroupAnalyticsDataKey id;
	
	@Column(name = "user_in_system_id")
	private Long userInSystemId;
	
	@Column(name = "pilot_code")
	private String pilotCode;
	
	@Column(name = "detection_variable_name")
	private String detectionVariableName;
	
	@Column(name = "detection_variable_type")
	private String detectionVariableType;
	
	@Column(name = "gfv_dmv_nui_id")
	private Long gfvNuiId;
	
	@Column(name = "interval_start")
	private String intervalStart;
	
	@Column()
	private BigDecimal value;
	
	@Column
	private String sex;
	
	@Column(name = "birth_year")
	private Integer birthYear;
	
	@Column
	private String cohabiting;
	
	@Column
	private String education;
	
	@Column(name = "informal_caregiver_ability")
	private String informalCaregiverAbility;
	
	@Column(name = "marital_status")
	private String maritalStatus;
	
	@Column
	private String occupation;
	
	@Column(name = "quality_housing")
	private String qualityOfHousing;
	
	@Column(name = "quality_neighborhood")
	private String qualityOfNeighborhood;
	
	@Column (name = "age_group")
	private String ageGroup;
	
	@Column (name = "working")
	private String working;

	/**
	 * @return the id
	 */
	public ViewGroupAnalyticsDataKey getId() {
		return id;
	}

	/**
	 * @return the userInSystemId
	 */
	public Long getUserInSystemId() {
		return userInSystemId;
	}

	/**
	 * @return the pilotCode
	 */
	public String getPilotCode() {
		return pilotCode;
	}

	/**
	 * @return the detectionVariableName
	 */
	public String getDetectionVariableName() {
		return detectionVariableName;
	}

	/**
	 * @return the detectionVariableType
	 */
	public String getDetectionVariableType() {
		return detectionVariableType;
	}

	/**
	 * @return the gfvNuiId
	 */
	public Long getGfvNuiId() {
		return gfvNuiId;
	}

	/**
	 * @return the intervalStart
	 */
	public String getIntervalStart() {
		return intervalStart;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return the birthYear
	 */
	public Integer getBirthYear() {
		return birthYear;
	}

	/**
	 * @return the cohabiting
	 */
	public String getCohabiting() {
		return cohabiting;
	}

	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * @return the informalCaregiverAbility
	 */
	public String getInformalCaregiverAbility() {
		return informalCaregiverAbility;
	}

	/**
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @return the qualityOfHousing
	 */
	public String getQualityOfHousing() {
		return qualityOfHousing;
	}

	/**
	 * @return the qualityOfNeighborhood
	 */
	public String getQualityOfNeighborhood() {
		return qualityOfNeighborhood;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @return the ageGroup
	 */
	public String getAgeGroup() {
		return ageGroup;
	}

	/**
	 * @return the working
	 */
	public String getWorking() {
		return working;
	}
	
}
