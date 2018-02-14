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
import eu.city4age.dashboard.api.rest.CareRecipientService;
import eu.city4age.dashboard.api.rest.CareRecipientServiceTest;
import eu.city4age.dashboard.api.rest.MeasuresService;
import eu.city4age.dashboard.api.rest.UserService;
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
@WebAppConfiguration
@ActiveProfiles("test")
public class CareReciepentServiceTest {
	
	static protected Logger logger = LogManager.getLogger(CareRecipientServiceTest.class);
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CareRecipientService careRecipentService;
	
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
	private UserService userService;
	
	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;
	
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

		Assert.assertEquals("{\"message\":\"success\",\"responseCode\":10,\"status\":{},\"itemList\":[{\"userId\":" + uir1.getId() + ",\"age\":0,\"frailtyStatus\":\"N/A\",\"frailtyNotice\":\"N/A\",\"attention\":\"\\u0000\",\"textline\":\"N/A\",\"interventionstatus\":\"\\u0000\",\"interventionDate\":\"N/A\",\"detectionStatus\":\"N/A\",\"detectionDate\":\"N/A\",\"pilotCode\":\"LCC\"}]}", response.getEntity().toString());


	}

	@Test
	@Transactional
	@Rollback(true)
	public void getGroupsTest() throws Exception {
	
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp.setTypicalPeriod("MON");
		tp.setPeriodDescription("Month");
		typicalPeriodRepository.save(tp);

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00")
				,eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
		
		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-02-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));

		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-03-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
//
//		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"),
//				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
//		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
//
//		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
//				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
//		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));

		UserInSystem uis1 = new UserInSystem ();
		uis1.setUsername("aaaa");
		uis1.setPassword("aaaa");
		userInSystemRepository.save(uis1);
		
		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);
		
		UserInRole uir1 = new UserInRole ();
		uir1.setUserInSystem(uis1);
		uir1.setPilot(p1);
		uir1.setPilotCode(p1.getPilotCode());
		userInRoleRepository.save(uir1);
		
		DetectionVariableType dvt1 = DetectionVariableType.OVL;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GFG;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("OVL");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("GFG1");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable();
		dv3.setDetectionVariableName("GFG2");
		dv3.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv3);
		
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setTimeInterval(ti2);
		gef2.setUserInRoleId(uir1.getId());
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariableId(dv2.getId());
		gef2.setDetectionVariable(dv2);
		gef2.setGefValue(new BigDecimal(1.8).setScale(1, RoundingMode.HALF_UP));
		geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setTimeInterval(ti3);
		gef3.setUserInRoleId(uir1.getId());
		gef3.setUserInRole(uir1);
		gef3.setDetectionVariableId(dv3.getId());
		gef3.setDetectionVariable(dv3);
		gef3.setGefValue(new BigDecimal(2.8).setScale(1, RoundingMode.HALF_UP));
		geriatricFactorRepository.save(gef3);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setTimeInterval(ti1);
		gef1.setUserInRoleId(uir1.getId());
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariableId(dv1.getId());
		gef1.setDetectionVariable(dv1);
		gef1.setGefValue(new BigDecimal(4.0));
		geriatricFactorRepository.save(gef1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable ();
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1.setPilotCode(p1.getPilotCode());
		pdv1.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setDerivedDetectionVariable(dv1);
		pdv2.setPilotCode(p1.getPilotCode());
		pdv2.setDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable ();
		pdv3.setDerivedDetectionVariable(dv1);
		pdv3.setPilotCode(p1.getPilotCode());
		pdv3.setDetectionVariable(dv3);
		pilotDetectionVariableRepository.save(pdv3);
		
		ti1.getGeriatricFactorValue().add(gef1);
		ti1.getGeriatricFactorValue().add(gef2);
		ti1.getGeriatricFactorValue().add(gef3);
		timeIntervalRepository.save(ti1);
		
		ti2.getGeriatricFactorValue().add(gef2);
		timeIntervalRepository.save(ti2);
		
		ti3.getGeriatricFactorValue().add(gef3);
		timeIntervalRepository.save(ti3);
		
		List<DetectionVariableType.Type> parentFactors = new ArrayList<>();
		parentFactors.add(DetectionVariableType.Type.valueOf("OVL"));
		parentFactors.add( DetectionVariableType.Type.valueOf("GFG"));
		
		List<TimeInterval> tis = timeIntervalRepository.getGroups(uir1.getId(), parentFactors);
		Mockito.when(timeIntervalRepositoryMock.getGroups(uir1.getId(), parentFactors)).thenReturn(tis);
		
		List<FrailtyStatusTimeline> fst = frailtyStatusTimelineRepository.findByPeriodAndUserId(tis, uir1.getId());
		Mockito.when(frailtyStatusTimelineRepositoryMock.findByPeriodAndUserId(tis, uir1.getId())).thenReturn(fst);

		
		PilotDetectionVariable pdv1t = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dv1.getId(), uir1.getPilotCode());
		
		
		PilotDetectionVariable pdv2t = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dv2.getId(), uir1.getPilotCode());
		PilotDetectionVariable pdv3t = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dv3.getId(), uir1.getPilotCode());
		Mockito.when(pilotDetectionVariableRepositoryMock.findByDetectionVariableAndPilotCode(dv1.getId(), uir1.getPilotCode())).thenReturn(pdv1t);
		Mockito.when(pilotDetectionVariableRepositoryMock.findByDetectionVariableAndPilotCode(dv2.getId(), uir1.getPilotCode())).thenReturn(pdv2t);
		Mockito.when(pilotDetectionVariableRepositoryMock.findByDetectionVariableAndPilotCode(dv3.getId(), uir1.getPilotCode())).thenReturn(pdv3t);
		
		PathSegment ovl = new PathSegment() {
			
			@Override 
			public String toString() {
				return "OVL";
			}
			
			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getMatrixParameters() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		PathSegment gfg = new PathSegment() {
			
			@Override 
			public String toString() {
				return "GFG";
			}
			
			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getMatrixParameters() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		List<PathSegment> parentFactorsPathSegment = new ArrayList<>();
		parentFactorsPathSegment.add(ovl);
		parentFactorsPathSegment.add(gfg);
	
		Response response = careRecipentService.getGroups(uir1.getId().toString(), parentFactorsPathSegment);
		
		Assert.assertTrue(response.getEntity().toString().contains("\"message\":\"success\",\"responseCode\":10,\"careRecipientName\":\"aaaa\",\"frailtyStatus\":{\"months\":[{\"id\":" + ti1.getId() + ",\"name\":\"2016/01\"},{\"id\":" + ti2.getId() + ",\"name\":\"2016/02\"},{\"id\":" + ti3.getId() + ",\"name\":\"2016/03\"}],\"series\":[{\"name\":\"Pre-Frail\"},{\"name\":\"Frail\"},{\"name\":\"Fit\"}]}"));
		Assert.assertTrue(response.getEntity().toString().contains("{\"groupName\":\"GFG2\",\"idList\":[" + gef3.getId() + "," + gef3.getId() + "],\"dateList\":[\"2016/01\",\"2016/02\",\"2016/03\"],\"itemList\":[2.8,2.8]}],\"groupName\":\"GFG2\",\"gefTypeId\":" + dv3.getId() + ",\"parentGroupName\":\"OVL\"}"));
		Assert.assertTrue(response.getEntity().toString().contains("{\"groupName\":\"OVL\",\"idList\":[" + gef1.getId() + "],\"dateList\":[\"2016/01\",\"2016/02\",\"2016/03\"],\"itemList\":[4.0]}],\"groupName\":\"OVL\",\"gefTypeId\":" + dv1.getId() + ",\"parentGroupName\":\"OVL\"}"));
		Assert.assertTrue(response.getEntity().toString().contains("{\"groupName\":\"GFG1\",\"idList\":[" + gef2.getId() + "," + gef2.getId() + "],\"dateList\":[\"2016/01\",\"2016/02\",\"2016/03\"],\"itemList\":[1.8,1.8]}],\"groupName\":\"GFG1\",\"gefTypeId\":" + dv2.getId() + ",\"parentGroupName\":\"OVL\"}"));

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
