package eu.city4age.dashboard.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.jpa.ActivityRepository;
import eu.city4age.dashboard.api.jpa.MTestingReadingsRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Activity;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Bluetooth;
import eu.city4age.dashboard.api.pojo.json.desobj.Gps;
import eu.city4age.dashboard.api.pojo.json.desobj.JSONActivity;
import eu.city4age.dashboard.api.pojo.json.desobj.Recognition;
import eu.city4age.dashboard.api.pojo.json.desobj.Wifi;

@Component
@Transactional()
public class AndroidService {
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
	
	static protected Logger logger = LogManager.getLogger(AndroidService.class);
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private MTestingReadingsRepository mTestingReadingsRepository;
	
	@Autowired
	private ActivityRepository activityRepository;
	
	@Async
	public void storeInfoInDb (AndroidActivitiesDeserializer data) {
		
		logger.info("started storing in db");
		
		try {
			
			UserInRole uir = userInRoleRepository.findOne(data.getId());
			
			List<MTestingReadings> mtss = new ArrayList<MTestingReadings>();
			
			for (JSONActivity jsonActivity : data.getActivities()) {

				MTestingReadings mts = createMTR(jsonActivity, uir);
				if (mts != null) {
					mtss.add(mts);
					/*mts.addGpss(jsonActivity.getGpss());
					mts.addBluetooth(jsonActivity.getBluetooths());
					mts.addWifi(jsonActivity.getWifis());
					mts.addRecognition(jsonActivity.getRecognitions());
					mtss.add(mts);*/
					
					for (Gps gps:jsonActivity.getGpss()) {
						MTestingReadings gpsMTS = new MTestingReadings ();
						gpsMTS.setGpsLongitude(gps.getLongitude().toString());
						gpsMTS.setGpsLatitude(gps.getLatitude().toString());
						gpsMTS.setStart(sdf.parse(gps.getDate()));
						gpsMTS.setMtesting_parent(mts);
						mtss.add(gpsMTS);
					}
					
					for (Bluetooth bt:jsonActivity.getBluetooths()) {
						MTestingReadings btMTS = new MTestingReadings();
						btMTS.setBluetoothDevices(bt.getDevice());
						btMTS.setStart(sdf.parse(bt.getDate()));
						btMTS.setMtesting_parent(mts);
						mtss.add(btMTS);
					}
					
					for (Wifi wifi:jsonActivity.getWifis()) {
						MTestingReadings wifiMTS = new MTestingReadings();
						wifiMTS.setWifiDevices(wifi.getDevices());
						wifiMTS.setStart(sdf.parse(wifi.getDate()));
						wifiMTS.setMtesting_parent(mts);
						mtss.add(wifiMTS);
					}
					for (Recognition rec:jsonActivity.getRecognitions()) {
						MTestingReadings recogMTS = new MTestingReadings ();
						recogMTS.setRecognitions(rec.getType());
						recogMTS.setStart(sdf.parse(rec.getDate()));
						recogMTS.setMtesting_parent(mts);
						mtss.add(recogMTS);
					}
				} else {
					//response.getStatus().setResponseCode("402 - ACTIVITY TYPE DOESN'T EXIST.");
					String status = "Activity type isn't recognized: " + jsonActivity.getType();
					//response.getStatus().setConsole(status);
					logger.info(status);
					//return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 402);
				}
				
			}			
			
			mTestingReadingsRepository.save(mtss);
			
			logger.info("finished storing in db");
			
			/*logger.info("ulazi u for");
			for (int i = 0; i < 20000000; i++) logger.info(i);
			logger.info("izlazi iz fora");*/
		
		} catch (Exception e) {
			
			e.printStackTrace();
			//response.getStatus().setResponseCode("500 - INTERNAL_SERVER_ERROR");
			String status = "The server encountered an unexpected condition that prevented it from fulfilling the request.";
			//response.getStatus().setConsole(status);
			logger.info(status);

			//return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 500);

		}
	}
	
	private MTestingReadings createMTR(JSONActivity jsonActivity, UserInRole uir) throws ParseException {
		//logger.info("activity.getType() " + jsonActivity.getType());
		//logger.info("activity.getStart() " + jsonActivity.getStart());
		//logger.info("activity.getEnd() " + jsonActivity.getEnd());
		
		Activity activity = activityRepository.findOneByName(jsonActivity.getType().toLowerCase());
		
		MTestingReadings mts = null;
		
		if(activity != null) {
			mts = new MTestingReadings();
			mts.setUserInRole(uir);
			mts.setActivity(activity);
			mts.setStart(sdf.parse(jsonActivity.getStart()));
			mts.setEnd(sdf.parse(jsonActivity.getEnd()));
		}
		
		return mts;
	}

}
