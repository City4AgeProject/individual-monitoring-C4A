package eu.city4age.dashboard.api.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.python.icu.math.BigDecimal;
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
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.ws.C4ALoginResponse;
import eu.city4age.dashboard.api.rest.CareRecipientService;
import eu.city4age.dashboard.api.rest.CareRecipientServiceTest;
import eu.city4age.dashboard.api.rest.UserService;
import eu.city4age.dashboard.api.security.JwtIssuer;

import java.util.List;

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
		
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void getDiagramDataTest() throws Exception {
		
		
	}	
	
	@Test
	@Transactional
	@Rollback(true)
	public void findOneTest() throws Exception {
		
		
	}	
	
}
