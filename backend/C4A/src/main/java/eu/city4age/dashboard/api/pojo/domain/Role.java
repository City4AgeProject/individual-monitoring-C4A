package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cd_role")
public class Role extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2562593539826116725L;

	@Column(name="role_name")
	private String roleName;

	@Column(name="role_abbreviation")
	private String roleAbbreviation;

	@Column(name="role_description")
	private String roleDescription;
	
	@JsonIgnore
	@Column(name="valid_from")
	private Date validFrom;
	
	@JsonIgnore
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="stakeholder_abbreviation")
	private String stakeholderAbbreviation;
	
	public Role() {
	}

	public Role(String roleName, String roleAbbreviation, String roleDescription, Date validFrom,
			Date validTo, String stakeholderAbbreviation) {
		this.roleName = roleName;
		this.roleAbbreviation = roleAbbreviation;
		this.roleDescription = roleDescription;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.stakeholderAbbreviation = stakeholderAbbreviation;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleAbbreviation() {
		return this.roleAbbreviation;
	}

	public void setRoleAbbreviation(String roleAbbreviation) {
		this.roleAbbreviation = roleAbbreviation;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public String getStakeholderAbbreviation() {
		return stakeholderAbbreviation;
	}

	public void setStakeholderAbbreviation(String stakeholderAbbreviation) {
		this.stakeholderAbbreviation = stakeholderAbbreviation;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

}
