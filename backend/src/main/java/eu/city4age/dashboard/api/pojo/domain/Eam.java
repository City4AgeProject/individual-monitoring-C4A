package eu.city4age.dashboard.api.pojo.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="eam")
@SequenceGenerator(name = "default_gen", sequenceName = "eam_seq", allocationSize = 1)
public class Eam extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2984515729314887339L;

	@ManyToOne
    @JoinColumn(name="cd_action_id")
	private Action action;
	
	@ManyToOne
    @JoinColumn(name="executed_activity_id")
	private ExecutedActivity activity;
	
	private Integer duration;

	public Eam() {
	}

	public Eam(Action action, ExecutedActivity activity) {
		this.action = action;
		this.activity = activity;
	}

	public Eam(Action action, ExecutedActivity activity, Integer duration) {
		this.action = action;
		this.activity = activity;
		this.duration = duration;
	}

	public Action getAction() {
		return this.action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public ExecutedActivity getActivity() {
		return this.activity;
	}

	public void setActivity(ExecutedActivity activity) {
		this.activity = activity;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public int hashCode() {
		return id.intValue();
	}

	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof Eam) {
			Eam eam = (Eam) obj;
			if (eam.getId().equals(this.id)) return true;
			else return false;
		}
		return false;
	}

}
