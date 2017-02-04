package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="executed_action")
public class ExecutedAction extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1443827774597428823L;

	@ManyToOne
    @JoinColumn(name="actionid")
	private Action action;
	
	@ManyToOne
    @JoinColumn(name="activityid")
	private Activity activity;

	@ManyToOne
    @JoinColumn(name="locationid")
	private Location location;
	
	@Column(name="userinroleid")
	private UserInRole userInRole;

	private Date date;

	private Integer rating;
	
	@Column(name="sensor_id")
	private int sensorId;

	private String payload;
	
	@Column(name="extra_information")
	private String extraInformation;

	public ExecutedAction() {
	}

	public ExecutedAction(Action action, Location location, UserInRole userInRole, Date date, int sensorId,
			String payload) {
		this.action = action;
		this.location = location;
		this.userInRole = userInRole;
		this.date = date;
		this.sensorId = sensorId;
		this.payload = payload;
	}

	public ExecutedAction(Action action, Activity activity, Location location, UserInRole userInRole, Date date,
			Integer rating, int sensorId, String payload, String extraInformation) {
		this.action = action;
		this.activity = activity;
		this.location = location;
		this.userInRole = userInRole;
		this.date = date;
		this.rating = rating;
		this.sensorId = sensorId;
		this.payload = payload;
		this.extraInformation = extraInformation;
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

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getRating() {
		return this.rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public int getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getExtraInformation() {
		return this.extraInformation;
	}

	public void setExtraInformation(String extraInformation) {
		this.extraInformation = extraInformation;
	}

}
