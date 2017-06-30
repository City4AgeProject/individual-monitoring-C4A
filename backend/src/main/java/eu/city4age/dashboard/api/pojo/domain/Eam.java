package eu.city4age.dashboard.api.pojo.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="eam")
public class Eam extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2984515729314887339L;

	
	@ManyToOne
    @JoinColumn(name="action_id")
	private Action action;
	
	@ManyToOne
    @JoinColumn(name="activity_id")
	private Activity activity;
	
	private Integer duration;

	public Eam() {
	}

	public Eam(Action action, Activity activity) {
		this.action = action;
		this.activity = activity;
	}

	public Eam(Action action, Activity activity, Integer duration) {
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

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
