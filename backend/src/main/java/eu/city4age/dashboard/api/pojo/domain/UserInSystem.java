package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name="user_in_system")
@SequenceGenerator(name = "default_gen", sequenceName = "user_in_system_seq", allocationSize = 1)
public class UserInSystem extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6430938677533568378L;

	private String username;
	
	private String password;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@JsonView(View.AssessmentView.class)
	@Column(name="display_name")
	private String displayName;
	
	@Column (name = "is_active")
	private Boolean isActive;

	public UserInSystem() {
	}	

	public UserInSystem(String username, String password, Date createdDate, String displayName, Boolean isActive) {
		this.username = username;
		this.password = password;
		this.createdDate = createdDate;
		this.displayName = displayName;
		this.isActive = isActive;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
