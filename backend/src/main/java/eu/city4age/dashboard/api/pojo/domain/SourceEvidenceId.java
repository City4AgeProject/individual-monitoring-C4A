package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

@Embeddable
public class SourceEvidenceId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5908214341407149708L;

	@GeneratedValue
	@Column(name="geriatric_factor_id")
	private int geriatricFactorId;
	
	@GeneratedValue
	@Column(name="author_id")
	private int userInRoleId;

	public SourceEvidenceId() {
	}

	public SourceEvidenceId(int geriatricFactorId, int userInRoleId) {
		this.geriatricFactorId = geriatricFactorId;
		this.userInRoleId = userInRoleId;
	}

	public int getGeriatricFactorId() {
		return this.geriatricFactorId;
	}

	public void setGeriatricFactorId(int geriatricFactorId) {
		this.geriatricFactorId = geriatricFactorId;
	}

	public int getRoleId() {
		return userInRoleId;
	}

	public void setUserInRoleId(int userInRoleId) {
		this.userInRoleId = userInRoleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SourceEvidenceId))
			return false;
		SourceEvidenceId castOther = (SourceEvidenceId) other;

		return (this.getGeriatricFactorId() == castOther.getGeriatricFactorId())
				&& ((this.getRoleId() == castOther.getRoleId()));
	}
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getGeriatricFactorId();
		result = 37 * result + this.getRoleId();
		return result;
	}

}
