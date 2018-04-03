package eu.city4age.dashboard.api.service;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.ActivityRepository;
import eu.city4age.dashboard.api.jpa.MTestingReadingsRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepositoryTest;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Activity;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.ws.C4AAndroidResponse;
import eu.city4age.dashboard.api.rest.AndroidEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class AndroidServiceTest {
	
	static protected Logger logger = LogManager.getLogger(AndroidServiceTest.class);
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	private ActivityRepository activityRepository;
	
	@Mock
	private ActivityRepository activityRepositoryMock;
	
	@Autowired
	private MTestingReadingsRepository mTestingReadingsRepository;
	
	@Mock
	private MTestingReadingsRepository mTestingReadingsRepositoryMock;
	
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
    }
	@Spy
	@InjectMocks
	AndroidEndpoint androidService;
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Test
	@Transactional
	@Rollback(true)
	public void testPostFromAndroid () throws Exception {
		
		UserInRole uir = new UserInRole ();
		//uir.setId(new Long (3));
		uir = userInRoleRepository.save(uir);
		
		Activity activity = new Activity ();
		activity.setName("Walking");
		//activity.setInstrumental(instrumental);
		activity = activityRepository.save(activity);
		
		MTestingReadings mts = new MTestingReadings ();
		
		logger.info("uir.id " + uir.getId());
		logger.info("activity.id " + activity.getId());
		
		StringBuilder input = new StringBuilder ();
		
		input.append("{ \"ID\": \"").append(uir.getId().toString()).append("\", \"date\": \"Tue Aug 01 22:11:31 GMT+00:00 2017\","
				+ " \"activities\": [{\"type\": \"Walking\",\"start\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\","
				+ " \"end\": \"Tue Aug 01 22:05:02 GMT+00:00 2017\",\"gpss\": [{\"longitude\": -122.084,"
				+ "\"latitude\": 37.421998333333, \"date\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\"}]}]}");
		
		UserInRole resultUIR = userInRoleRepository.findOne(uir.getId());
		Mockito.when(userInRoleRepositoryMock.findOne(uir.getId())).thenReturn(resultUIR);
		
		Activity resultACT = activityRepository.findOneByName(activity.getName());
		Mockito.when(activityRepositoryMock.findOneByName(activity.getName())).thenReturn(resultACT);
		
		MTestingReadings resultMTS = mTestingReadingsRepository.save(mts);
		Mockito.when(mTestingReadingsRepositoryMock.save(mts)).thenReturn(resultMTS);
		
		Response response = androidService.postFromAndroid(input.toString());
		
		C4AAndroidResponse responseFormatted = (C4AAndroidResponse) response.getEntity();
		
		logger.info("response.getEntity: " + objectMapper.writeValueAsString(response.getEntity()));
		logger.info ("responseFormatted.getMtss().size()" + responseFormatted.getMtss().size());
	}

}
