package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.city4age.dashboard.api.pojo.json.desobj.Bluetooth;
import eu.city4age.dashboard.api.pojo.json.desobj.Gps;
import eu.city4age.dashboard.api.pojo.json.desobj.Recognition;
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
	private String gpsLongitude;
	
	@Column(name="gps_latitude")
	@ApiModelProperty (value = "geographic latitude of location of recording", hidden = false)
	private String gpsLatitude;
	
	@Column(name="bluetooth_devices")
	@ApiModelProperty (hidden = false)
	private String bluetoothDevices;
	
	@Column(name="wifi_devices")
	@ApiModelProperty (hidden = false)
	private String wifiDevices;
	
	@Column (name = "google_api_movement_recognition")
	@ApiModelProperty (hidden = false)
	private String recognitions;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mtesting_parent")
	private MTestingReadings mtesting_parent;
	
	@OneToMany(mappedBy = "mtesting_parent", fetch = FetchType.LAZY)
	private Set<MTestingReadings> readings = new HashSet <MTestingReadings> ();
	
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

	public String getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(String gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	@ApiModelProperty (hidden = true)
	public String getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(String gpsLatitude) {
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

	public String getRecognitions() {
		return recognitions;
	}

	public void setRecognitions(String recognitions) {
		this.recognitions = recognitions;
	}
	
	/**
	 * @return the mtesting_parent
	 */
	public MTestingReadings getMtesting_parent() {
		return mtesting_parent;
	}

	/**
	 * @param mtesting_parent the mtesting_parent to set
	 */
	public void setMtesting_parent(MTestingReadings mtesting_parent) {
		this.mtesting_parent = mtesting_parent;
	}

	/**
	 * @return the readings
	 */
	public Set<MTestingReadings> getReadings() {
		return readings;
	}

	/**
	 * @param readings the readings to set
	 */
	public void setReadings(Set<MTestingReadings> readings) {
		this.readings = readings;
	}

	public void addGpss (List<Gps> gpss) {
		if (gpss != null && gpss.size() > 0) {
			StringBuilder sbLongitude = new StringBuilder ();
			StringBuilder sbLatitude = new StringBuilder ();
			for (Gps gps : gpss) {
				sbLongitude.append(gps.getLongitude()).append(";");
				sbLatitude.append(gps.getLatitude()).append(";");
			}
			this.setGpsLatitude(sbLatitude.toString());
			this.setGpsLongitude(sbLongitude.toString());
		}
	}
	
	public void addBluetooth(List<Bluetooth> bluetooth) {
		if(bluetooth != null && bluetooth.size() > 0) {
			StringBuffer bts = new StringBuffer();
			for(Bluetooth bt : bluetooth){
				//logger.info("bt device: " + bt.getDevice());
				bts.append(bt.getDevice()).append(";");
			}
			this.setBluetoothDevices(bts.toString());
		}	
	}
	
	public void addWifi(List<Wifi> wifi) {
		if(wifi != null && wifi.size() > 0) {
			StringBuffer bts = new StringBuffer();
			for(Wifi wf : wifi) {
				//logger.info("wf device: " + wf.getDevices());
				bts.append(wf.getDevices()).append(";");
			}
			this.setWifiDevices(bts.toString());
		}
	}
	
	public void addRecognition (List<Recognition> recognitions) {
		if(recognitions != null && recognitions.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for(Recognition recognition : recognitions) {
				//logger.info("recog: " + recognition.getType());
				sb.append(recognition.getType()).append(";");
			}
			this.setRecognitions(sb.toString());
		}
	}
	
	@Override
	public int hashCode() {
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof MTestingReadings) {
			MTestingReadings mtr = (MTestingReadings) obj;
			if (mtr.getId().equals(this.id)) return true;
			else return false;
		}
		return false;
	}

}
