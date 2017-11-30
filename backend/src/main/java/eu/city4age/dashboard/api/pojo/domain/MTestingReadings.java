package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.city4age.dashboard.api.pojo.json.desobj.Bluetooth;
import eu.city4age.dashboard.api.pojo.json.desobj.Wifi;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="mtesting_readings")
@SequenceGenerator(name = "default_gen", sequenceName = "mtesting_readings_id_seq", allocationSize = 1)
@ApiModel
public class MTestingReadings extends AbstractBaseEntity<Long> {
	
	static protected Logger logger = LogManager.getLogger(AbstractBaseEntity.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 118520501300973077L;

	@Column(name="start_time")
	@ApiModelProperty (value = "start time of recording", hidden = false)
	private Date start;
	
	@ApiModelProperty (value = "end time of recording", hidden = false)
	@Column(name="end_time")
	private Date end;

	@ApiModelProperty (value = "id of type of sensor in database", hidden = false)
	@Column(name="sensor_id")
	private Integer sensor_id;
	
	@Column(name="extra_information")
	@ApiModelProperty (value = "additional comment about recording", hidden = false)
	private String extraInformation;
	
	@Column(name="action_name")
	@ApiModelProperty (hidden = false)
	private String actionName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cd_activity_id")
	@ApiModelProperty (hidden = true)
	private Activity activity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_in_role_id")
	@ApiModelProperty (hidden = true)
	private UserInRole userInRole;
	
	@Column(name="gps_longitude")
	@ApiModelProperty (value = "geographic longitude of location of recording", hidden = false)
	private Long gpsLongitude;
	
	@Column(name="gps_latitude")
	@ApiModelProperty (value = "geographic latitude of location of recording", hidden = false)
	private Long gpsLatitude;
	
	@Column(name="bluetooth_devices")
	@ApiModelProperty (hidden = false)
	private String bluetoothDevices;
	
	@Column(name="wifi_devices")
	@ApiModelProperty (hidden = false)
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
	
	public Integer getSensor_id() {
		return sensor_id;
	}
	
	public void setSensor_id(Integer sensor_id) {
		this.sensor_id = sensor_id;
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

	@ApiModelProperty (hidden = true)
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
