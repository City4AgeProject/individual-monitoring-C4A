package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="cd_activity")
@SequenceGenerator(name = "default_gen", sequenceName = "cd_activity_id_seq", allocationSize = 1)
public class Activity extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2798372020376275382L;

	@Column(name="activity_name")
	private String name;
	
	@Column(name="activity_description")
	private String description;
	
	@Column(name="creation_date")
	private Date creation;

	private Boolean instrumental;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Boolean getInstrumental() {
		return instrumental;
	}

	public void setInstrumental(Boolean instrumental) {
		this.instrumental = instrumental;
	}
	
}
