package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "cr_profile")
public class CrProfile extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1779173699467575209L;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_in_role_id", nullable = false)
	private UserInRole userInRole;

	@Column(name = "ref_height")
	private Float refHeight;

	@Column(name = "ref_weight")
	private Float refWeight;

	@Column(name = "ref_mean_blood_pressure")
	private BigDecimal refMeanBloodPressure;

	private Date date;

	@Column(name = "birth_date")
	private Date birthDate;

	private boolean gender;

	public CrProfile() {
	}

	public CrProfile(UserInRole userInRole, Date birthDate, boolean gender) {
		this.userInRole = userInRole;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public CrProfile(UserInRole userInRole, Float refHeight, Float refWeight, BigDecimal refMeanBloodPressure,
			Date date, Date birthDate, boolean gender) {
		this.userInRole = userInRole;
		this.refHeight = refHeight;
		this.refWeight = refWeight;
		this.refMeanBloodPressure = refMeanBloodPressure;
		this.date = date;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public Float getRefHeight() {
		return this.refHeight;
	}

	public void setRefHeight(Float refHeight) {
		this.refHeight = refHeight;
	}

	public Float getRefWeight() {
		return this.refWeight;
	}

	public void setRefWeight(Float refWeight) {
		this.refWeight = refWeight;
	}

	public BigDecimal getRefMeanBloodPressure() {
		return this.refMeanBloodPressure;
	}

	public void setRefMeanBloodPressure(BigDecimal refMeanBloodPressure) {
		this.refMeanBloodPressure = refMeanBloodPressure;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isGender() {
		return this.gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

}
