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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.ActivityRepository;
import eu.city4age.dashboard.api.jpa.MTestingReadingsRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Activity;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.JSONActivity;
import eu.city4age.dashboard.api.pojo.ws.C4AAndroidResponse;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**
 * @author milos.holclajtner
 * 
 */
@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(AndroidEndpoint.PATH)
@Api(value = "android", consumes = "application/json" ,produces = "application/json")
public class AndroidEndpoint {

	public static final String PATH = "android";

	static protected Logger logger = LogManager.getLogger(MeasuresEndpoint.class);
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

	@Autowired
	UserInRoleRepository userInRoleRepository;

	@Autowired
	MTestingReadingsRepository mTestingReadingsRepository;
	
	@Autowired
	ActivityRepository activityRepository;

	@POST
	@ApiOperation(value = "Post activity data from android app.", notes = "Processing of data collected with android app, creating new MTestingReading objects and persisting those objects in database"
	)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("postFromAndroid")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = C4AAndroidResponse.class),
			@ApiResponse(code = 400, message = "Header content-type is not 'application/json' or content empty."),
			@ApiResponse(code = 401, message = "No user with this id in database."),
			@ApiResponse(code = 402, message = "Activity type isn't recognized."),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	public Response postFromAndroid(@ApiParam (name = "json", value = "response string from android app", required = true,
			examples = @Example ( value = {
				@ExampleProperty (value = "{\"ID\": \"3\", \"date\": \"Tue Aug 01 22:11:31 GMT+00:00 2017\", \"activities\": [{\"type\": \"Walking\",\"start\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\", \"end\": \"Tue Aug 01 22:05:02 GMT+00:00 2017\",\"gps\": [{\"longitude\": -122.084, \"latitude\": 37.421998333333, \"date\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\"}]}]}")	
			})) @RequestBody String json) throws IOException {

		C4AAndroidResponse response = new C4AAndroidResponse();
		AndroidActivitiesDeserializer data;
		
		try {
			
			data = objectMapper.readerFor(AndroidActivitiesDeserializer.class)
					.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			response.setResult(0L);
			response.getStatus().setResponseCode("400 - CONTENT NOT JSON/CONTENT EMPTY.");
			response.getStatus().setConsole("Header content-type is not 'application/json' or content empty.");
			return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 400);
		
		}

		
		try {
			
			UserInRole uir = userInRoleRepository.findOne(data.getId());
			
			if (uir == null) {
			
				response.setResult(0L);
				response.getStatus().setResponseCode("401 - WRONG USER ID.");
				response.getStatus().setConsole("No user with this id in database.");
				return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 401);
			
			}
			
			List<MTestingReadings> mtss = new ArrayList<MTestingReadings>();
			
			for (JSONActivity jsonActivity : data.getActivities()) {

				MTestingReadings mts;
				
				mts = createMTR(jsonActivity, uir);
				if (mts != null) {
					mts.addGpss(jsonActivity.getGpss());
					mts.addBluetooth(jsonActivity.getBluetooths());
					mts.addWifi(jsonActivity.getWifis());
					mts.addRecognition(jsonActivity.getRecognitions());
					mtss.add(mts);
				} else {
					response.getStatus().setResponseCode("402 - ACTIVITY TYPE DOESN'T EXIST.");
					String status = "Activity type isn't recognized: " + jsonActivity.getType();
					response.getStatus().setConsole(status);
					//logger.info(status);
					return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 402);
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
			//logger.info(status);

			return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response), 500);

		}
		
		return JerseyResponse.buildTextPlain(response);
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