package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.city4age.dashboard.api.pojo.json.desobj.Bluetooth;
import eu.city4age.dashboard.api.pojo.json.desobj.Wifi;

@Entity
@Table(name="mtesting_readings")
public class MTestingReadings implements Serializable {
	
	static protected Logger logger = LogManager.getLogger(AbstractBaseEntity.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 118520501300973077L;
	
	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "mt_seq", sequenceName = "m-testing_readings_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "mt_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="start_time")
	private Date start;
	
	@Column(name="end_time")
	private Date end;
	
	@Column(name="duration")
	private Integer duration;

	@Column(name="rating")
	private Float rating;
	
	@Column(name="sensor_id")
	private Integer sensor_id;
	
	@Column(name="position")
	private String position;
	
	@Column(name="extra_information")
	private String extraInformation;
	
	@Column(name="action_name")
	private String actionName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cd_activity_id")
	private Activity activity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_role_id")
	private UserInRole userInRole;
	
	@Column(name="gps_longitude")
	private Long gpsLongitude;
	
	@Column(name="gps_latitude")
	private Long gpsLatitude;
	
	@Column(name="bluetooth_devices")
	private String bluetoothDevices;
	
	@Column(name="wifi_devices")
	private String wifiDevices;
	


	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public UserInRole getUserInRole() {
		return userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public Integer getSensor_id() {
		return sensor_id;
	}

	public void setSensor_id(Integer sensor_id) {
		this.sensor_id = sensor_id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getExtraInformation() {
		return extraInformation;
	}

	public void setExtraInformation(String extraInformation) {
		this.extraInformation = extraInformation;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Long getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(Long gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Long getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(Long gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public String getBluetoothDevices() {
		return bluetoothDevices;
	}

	public void setBluetoothDevices(String bluetoothDevices) {
		this.bluetoothDevices = bluetoothDevices;
	}

	public String getWifiDevices() {
		return wifiDevices;
	}

	public void setWifiDevices(String wifiDevices) {
		this.wifiDevices = wifiDevices;
	}

	public void addBluetooth(List<Bluetooth> bluetooth) {
		if(bluetooth != null && bluetooth.size() > 0) {
			StringBuffer bts = new StringBuffer();
			for(Bluetooth bt : bluetooth){
				logger.info("bt device: " + bt.getDevice());
				bts.append(bt.getDevice()).append(";");
			}
			this.setBluetoothDevices(bts.toString());
		}	
	}

	public void addWifi(List<Wifi> wifi) {
		if(wifi != null && wifi.size() > 0) {
			StringBuffer bts = new StringBuffer();
			for(Wifi wf : wifi) {
				logger.info("wf device: " + wf.getDevice());
				bts.append(wf.getDevice()).append(";");
			}
			this.setWifiDevices(bts.toString());
		}
	}

}
