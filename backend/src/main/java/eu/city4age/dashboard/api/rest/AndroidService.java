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

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.ActivityRepository;
import eu.city4age.dashboard.api.persist.MTestingReadingsRepository;
import eu.city4age.dashboard.api.persist.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.C4AAndroidResponse;
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

	@SuppressWarnings("deprecation")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("postFromAndroid")
	public Response postFromAndroid(String json) throws IOException {

		C4AAndroidResponse response = new C4AAndroidResponse();
		AndroidActivitiesDeserializer data;
		
		try {
			
			data = objectMapper.reader(AndroidActivitiesDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			response.setResult(0L);
			response.getStatus().setResponseCode("400 - CONTENT NOT JSON/CONTENT EMPTY.");
			response.getStatus().setConsole("Header content-type is not 'application/json' or content empty.");
			return Response.status(400).type(MediaType.TEXT_PLAIN).entity(objectMapper.writeValueAsString(response)).build();
		
		}

		
		try {
			
			UserInRole uir = userInRoleRepository.findOne(data.getId());
			
			if (uir == null) {
			
				response.setResult(0L);
				response.getStatus().setResponseCode("401 - WRONG USER ID.");
				response.getStatus().setConsole("No user with this id in database.");
				return Response.status(401).type(MediaType.TEXT_PLAIN).entity(objectMapper.writeValueAsString(response)).build();
			
			}
			
			List<MTestingReadings> mtss = new ArrayList<MTestingReadings>();
			
			for (Activity activity : data.getActivities()) {

				MTestingReadings mts;
				
				if (activity.getGpss() != null && activity.getGpss().size() > 0) {
				
					for (Gps gps : activity.getGpss()) {
					
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
			response.setResult(1L);
			response.setMtss(mtss);
			response.getStatus().setResponseCode("200 - OK.");
			response.getStatus().setConsole("Activities saved to database!");
		
		} catch (Exception e) {
			
			e.printStackTrace();
			response.getStatus().setResponseCode("500 - INTERNAL_SERVER_ERROR");
			String status = "The server encountered an unexpected condition that prevented it from fulfilling the request.";
			response.getStatus().setConsole(status);
			logger.info(status);

			return Response.status(500).type(MediaType.TEXT_PLAIN).entity(objectMapper.writeValueAsString(response)).build();
		
		}
		
		return Response.ok().type(MediaType.TEXT_PLAIN).entity(objectMapper.writeValueAsString(response)).build();
	}
	
	private MTestingReadings createMTR(Activity activity, UserInRole uir) throws ParseException {
		logger.info("activity.getType() " + activity.getType());
		logger.info("activity.getStart() " + activity.getStart());
		logger.info("activity.getEnd() " + activity.getEnd());
		
		MTestingReadings mts = new MTestingReadings();
		mts.setUserInRole(uir);
		mts.setActivity(activityRepository.findOneByName(activity.getType()));
		mts.setStart(sdf.parse(activity.getStart()));
		mts.setEnd(sdf.parse(activity.getEnd()));
		mts.setActionName("action");
		
		return mts;
	}

}