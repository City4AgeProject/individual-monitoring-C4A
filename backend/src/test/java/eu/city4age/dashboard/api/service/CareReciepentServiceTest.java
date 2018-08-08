package eu.city4age.dashboard.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.CrProfileRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.CrProfile;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.ws.C4ALoginResponse;
import eu.city4age.dashboard.api.rest.CareRecipientEndpoint;
import eu.city4age.dashboard.api.rest.CareRecipientServiceTest;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;
import eu.city4age.dashboard.api.rest.UserEndpoint;
import eu.city4age.dashboard.api.security.JwtIssuer;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import java.math.BigDecimal;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import org.junit.Assert;

/*
 * authors: marina.andric
 * 		    milos.holclajtner
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class CareReciepentServiceTest {
	
	static protected Logger logger = LogManager.getLogger(CareRecipientServiceTest.class);
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CareRecipientEndpoint careRecipentService;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	private PilotRepository pilotRepository;

	@Mock
	private PilotRepository pilotRepositoryMock;
	
	@Autowired
	private UserInSystemRepository userInSystemRepository;
	
	@Autowired
	private UserEndpoint userService;
	
	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;
	
	@Autowired
	private MeasuresEndpoint measuresEndpoint;
	
	@Autowired
	private MeasuresService measuresService;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private FrailtyStatusRepository frailtyStatusRepository;
	
	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;
	
	@Mock
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepositoryMock;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private CrProfileRepository crProfileRepository;
	
	@Mock
	private CrProfileRepository crProfileRepositoryMock;
	
	@Mock
	private PilotDetectionVariableRepository pilotDetectionVariableRepositoryMock;
	
	@Mock
	private TimeIntervalRepository timeIntervalRepositoryMock;
	
    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }
	
	@Test
	@Transactional
	@Rollback(true)
	public void getCareRecipientsTest() throws Exception {
		
		Role r1 = new Role();
		r1.setId(1L);
		roleRepository.save(r1);

		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		p1.setName("LECCE");
		pilotRepository.save(p1);
		
		UserInRole uir1 = new UserInRole();
		uir1.setRoleId(r1.getId().shortValue());
		uir1.setPilot(p1);
		uir1.setPilotCode(p1.getPilotCode());
		userInRoleRepository.save(uir1);
		
		
		UserInSystem uis = new UserInSystem();
		uis.setUsername("username");
		uis.setPassword("password");
		userInSystemRepository.save(uis);
		
		UserInRole user = new UserInRole();
		user.setUserInSystem(uis);
		user.setPilot(p1);
		user.setPilotCode(p1.getPilotCode());
		user.setRoleId((short) 7);
		userInRoleRepository.save(user);
		
		UserInRole uirResponse = userInRoleRepository.findBySystemUsernameAndPassword("username", "password");		
		Mockito.when(userInRoleRepositoryMock.findBySystemUsernameAndPassword("username", "password")).thenReturn(uirResponse);
		
		Pilot userPilotResponse = pilotRepository.findOne(p1.getPilotCode());		
		Mockito.when(pilotRepositoryMock.findOne(p1.getPilotCode())).thenReturn(userPilotResponse);

		C4ALoginResponse responseLogin = (C4ALoginResponse) userService.login("username", "password").getEntity();
		
		String token = responseLogin.getJwToken();
		
		List<UserInRole> usersList1 = userInRoleRepository.findByRoleIdAndPilotCode(r1.getId().shortValue(), user.getPilotCode()); 		
		Mockito.when(userInRoleRepositoryMock.findByRoleIdAndPilotCode(user.getRoleId(), user.getPilotCode())).thenReturn(usersList1);

		List<UserInRole> usersList2 = userInRoleRepository.findByRoleId(user.getRoleId());
		Mockito.when(userInRoleRepositoryMock.findByRoleId(user.getRoleId())).thenReturn(usersList2);
		
		Pilot userPilot = pilotRepository.findOne(uir1.getPilotCode());
		Mockito.when(pilotRepositoryMock.findOne(uir1.getPilotCode())).thenReturn(userPilot);

		Response response = careRecipentService.getCareRecipients(token);

		Assert.assertTrue(response.getEntity().toString().contains("\"message\":\"success\",\"responseCode\":10,\"status\":{}"));
		Assert.assertTrue(response.getEntity().toString().contains("\"itemList\":[{\"userId\":" + uir1.getId() + ",\"age\":0,\"frailtyStatus\":\"N/A\",\"frailtyNotice\":\"N/A\",\"attention\":\"\\u0000\",\"textline\":\"N/A\",\"interventionstatus\":\"\\u0000\",\"interventionDate\":\"N/A\",\"detectionStatus\":\"N/A\",\"detectionDate\":\"N/A\",\"pilotCode\":\"LCC\""));

	}
	
//	@Test
//	@Transactional
//	@Rollback(true)
//	public void getDiagramDataTest() throws Exception {
//		
//		
//	}	
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOneTest() throws Exception {
		
		UserInRole uir = new UserInRole();
		userInRoleRepository.save(uir);
		
		CrProfile crProfile = new CrProfile();
		crProfile.setBirthDate(new GregorianCalendar(1986, 2, 11).getTime());
		crProfile.setGender(true);
		crProfile.setRefHeight(174F);
		crProfile.setRefWeight(53F);
		crProfile.setUserInRole(uir);
		crProfileRepository.save(crProfile);
		
		CrProfile crP = crProfileRepository.findOne(crProfile.getId());
		Mockito.when(crProfileRepositoryMock.findOne(crProfile.getId())).thenReturn(crP);
		
		Response response = careRecipentService.findOne(crProfile.getId());
		
		CrProfile result = objectMapper.readerFor(CrProfile.class).readValue(response.getEntity().toString());
		Assert.assertEquals(result.getId(), crProfile.getId());
		Assert.assertEquals(result.getRefHeight(), crProfile.getRefHeight());
		Assert.assertEquals(result.getRefWeight(), crProfile.getRefWeight());
		Assert.assertEquals(result.getBirthDate(), crProfile.getBirthDate());
		Assert.assertEquals(result.isGender(), crProfile.isGender());

	}	
	
}
