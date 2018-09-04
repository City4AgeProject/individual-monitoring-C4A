package eu.city4age.dashboard.api.rest;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.ActivityRepository;
import eu.city4age.dashboard.api.jpa.MTestingReadingsRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;
import eu.city4age.dashboard.api.pojo.json.AndroidTokenDeserializer;
import eu.city4age.dashboard.api.pojo.ws.C4AAndroidResponse;
import eu.city4age.dashboard.api.pojo.ws.JerseyResponse;
import eu.city4age.dashboard.api.service.AndroidService;
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

	static protected Logger logger = LogManager.getLogger(AndroidEndpoint.class);
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Autowired
	UserInRoleRepository userInRoleRepository;

	@Autowired
	MTestingReadingsRepository mTestingReadingsRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	AndroidService androidService;

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
			})) @RequestBody InputStream json, @Context HttpServletRequest req) throws IOException {
		
		logger.info("service started");

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
		
		logger.info("length: " + req.getContentLength());
		
		androidService.storeInfoInDb(data);
		logger.info("service finnished");
		
		response.setResult(1L);
		//response.setMtss(mtss);
		response.getStatus().setResponseCode("200 - OK.");
		response.getStatus().setConsole("Activities saved to database!");
		
		return JerseyResponse.buildTextPlain(objectMapper.writeValueAsString(response));
	}
	
	@GET
	@Path("testFirebase")
	public Response testFirebase () {
				
		try {
			
			/*ClassLoader classLoader = getClass().getClassLoader();
			FileInputStream serviceAccount = new FileInputStream(new File(classLoader.getResource("m-testing-54584-firebase-adminsdk-w3n9c-ae9f5356f1.json").getFile()));

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://m-testing-54584.firebaseio.com")
					.build();

			FirebaseApp app = FirebaseApp.initializeApp(options);
			
			logger.info("app.getName: " + app.getName());*/
			
			// galaxy
			//String registrationToken = "eDWtsYJ3RoQ:APA91bE0qTpq5la2iIB9xNZWm_-FiniBVjmP_MBGDYhm7XvOAT5pY3MklNlmTt2k_Pqe3b264kYKltms5prm9p3yAdDfeCKHjQOg5m3O5HWjYTtZaN3Kk6g5AGAWSzShmI9_QrWi3Y3k";
			
			// honor
			
			Map<String, String> data = new HashMap <String, String> ();
			data.put("title", "Question 34");
			data.put("body", "Please select the exact Activity you have performed on July 28th from 8:15 to 10:35AM?");
			data.put( "answer1", "Attended Senior Centre");
			data.put( "answer2", "Been in a public park");
			data.put("answer3", "Both of the above");
			data.put( "answer4", "None of the above");
			data.put( "id", "34");
			
			List<FirebaseApp> list = FirebaseApp.getApps();
			logger.info(list.size());
			String registrationToken = "e-5uiS9BT0Y:APA91bHty81bycE-TlOzUgsBtTPsApET5XKl7Xn6AhuEsSU7k19yWwf0aCO9AXYDhf0mt4IDQCbAC3JJwCej1whYUoi7ZKPQy8GWKMlrDPAqlykDfoHhuiE1cFSC73MfkTsqcpmNjkVuTyKjz_Zbm26GpC5WU5cvuw";
			
			/*Message message = Message.builder().setNotification(new Notification(
			        "Notifikacija za oba uredjaja",
			        "gospodin honor: pokusaj pitanja")).setAndroidConfig(AndroidConfig.builder().setPriority(Priority.HIGH).putAllData(data).build())./*putData("type", "1").setToken(registrationToken).build();*/
			
			Message message = Message.builder().setAndroidConfig(AndroidConfig.builder().putAllData(data).setTtl(600l).setPriority(Priority.HIGH).build()).setToken(registrationToken).build();
			
			String response; /*= FirebaseMessaging.getInstance().sendAsync(message).get();
			logger.info("proslo");
			logger.info("response: " + response);
			
			registrationToken = "eJGOg48JBMA:APA91bEo681lTGQ8kPVJA1ZEwx7IxRCO8fiFZ80AHybg4luIxSEnAID79bf0qlCgaagx2TOUyJ2-gjGjzw3jwCzt3Y5C7uczOvUdjpYGr6gtJWIpuYGQf-dxCcUpCbqBi9tTArojHm_rVIDnl0lF18iRLn1iHtzWGg";
			
			message = Message.builder().setAndroidConfig(AndroidConfig.builder().putAllData(data).setTtl(600l).setPriority(Priority.HIGH).build()).setToken(registrationToken).build();
			
			response = FirebaseMessaging.getInstance().sendAsync(message).get();
			
			registrationToken = "evk7hB0no0A:APA91bGE_GewQO3KMHfRFhaJPtNz6pZ85onCxhAWoR5LzJ-8Z7Nl7U9PM8h0U6b3EN51qZ3VPp1IvMOkYv5HFUU5qXDADFZ157z8kPLbhA8tG9lYGoLF--BYc-UsWD5KLvhK-mBWvfEIYwit4vqhhjcGZU-i_LzqDQ";
			
			message = Message.builder().setAndroidConfig(AndroidConfig.builder().putAllData(data).setTtl(600l).setPriority(Priority.HIGH).build()).setToken(registrationToken).build();
			
			logger.info(message);
			
			response = FirebaseMessaging.getInstance().sendAsync(message).get();		
			logger.info("proslo");
			logger.info("response: " + response);*/
			
			registrationToken = "dGoCIkpb5Tc:APA91bGnjxaTSDpQuYTOG9LIAM7Zj4u9hqPM4-Ny7C6ztx96tVErzDqjpgEQ3ZS6-2zdJxp6Zsvo1GwkGo_OC1C2SRJzc8OHYfGZQIfNA6CxPdFsdEJTYCI_o0IFkogFwZY5vmMlW0L_";
			message = Message.builder().setAndroidConfig(AndroidConfig.builder().putAllData(data).setTtl(600l).setPriority(Priority.HIGH).build()).setToken(registrationToken).build();
			
			response = FirebaseMessaging.getInstance().sendAsync(message).get();
			
			logger.info("proslo");
			logger.info("response: " + response);
			
			//Message.builder().setAndroidConfig(AndroidConfig.builder().putAllData(data).setTtl(600l).setPriority(Priority.HIGH).setNotification(AndroidNotification.builder().setTitle("Question").build()).build());
			
					
						
		} catch (Exception e) {
			logger.info("ne moze");
			e.printStackTrace();
			logger.info(e.getMessage());
			return Response.ok().build();
		}
		
		return Response.ok().build();
		
	}
	
	@POST
	@Path("firebaseToken")
	public Response firebaseToken (@RequestBody String json) {
		
		AndroidTokenDeserializer atd = new AndroidTokenDeserializer ();
		return null;
		
	}

}