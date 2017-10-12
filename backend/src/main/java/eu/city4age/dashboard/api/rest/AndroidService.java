package eu.city4age.dashboard.api.rest;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.ActivityRepository;
import eu.city4age.dashboard.api.persist.MTestingReadingsRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Activity;
import eu.city4age.dashboard.api.pojo.json.desobj.Gps;

/**
 * @author milos.holclajtner
 * 
 */
@Component
@Transactional("transactionManager")
@Path(AndroidService.PATH)
public class AndroidService {

	public static final String PATH = "android";

	static protected Logger logger = LogManager.getLogger(MeasuresService.class);
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

	@Autowired
	UserInRoleRepository userInRoleRepository;

	@Autowired
	MTestingReadingsRepository mTestingReadingsRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	//private int i = 0;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("postFromAndroid")
	public Response postFromAndroid(String json) throws JsonProcessingException, IOException, ProcessingException, ParseException {


		@SuppressWarnings("deprecation")
		AndroidActivitiesDeserializer data = objectMapper.reader(AndroidActivitiesDeserializer.class)
				.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);

		String response;
		UserInRole uir = userInRoleRepository.findOne(data.getId());

		if (uir == null) {
			response = "There are no users in role with this ID !";
			return Response.ok(objectMapper.writeValueAsString(response)).build();
		}
		
		List<MTestingReadings> mtss = new ArrayList<MTestingReadings>();
		for(Activity activity: data.getActivities()) {

			MTestingReadings mts;
			if(activity.getGpss() != null && activity.getGpss().size() > 0) {
				for(Gps gps : activity.getGpss()) {
					mts = createMTR(activity, uir);
					mts.setGpsLatitude(gps.getLatitude());
					mts.setGpsLongitude(gps.getLongitude());
					mts.addBluetooth(activity.getBluetooths());
					mts.addWifi(activity.getWifis());
					mtss.add(mts);
				}
			} else {
				mts = createMTR(activity, uir);
				mts.addBluetooth(activity.getBluetooths());
				mts.addWifi(activity.getWifis());
				mtss.add(mts);
			}
		
		}
		
		mTestingReadingsRepository.save(mtss);

		response = "Activities saved to database!";
		return Response.ok(objectMapper.writeValueAsString(response)).build();
	}
	
	private MTestingReadings createMTR(Activity activity, UserInRole uir) throws ParseException {
		logger.info("activity.getType() " + activity.getType());
		logger.info("activity.getStart() " + activity.getStart());
		logger.info("activity.getEnd() " + activity.getEnd());
		
		MTestingReadings mts = new MTestingReadings();
		//i++;
		//mts.setId(Long.valueOf(i));
		mts.setUserInRole(uir);
		mts.setActivity(activityRepository.findOneByName(activity.getType()));
		mts.setStart(sdf.parse(activity.getStart()));
		mts.setEnd(sdf.parse(activity.getEnd()));
		mts.setActionName("action");
		
		return mts;
	}

}