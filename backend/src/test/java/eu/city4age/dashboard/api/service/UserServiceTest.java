package eu.city4age.dashboard.api.service;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
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
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.CrProfile;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.ws.C4ALoginResponse;
import eu.city4age.dashboard.api.rest.UserEndpoint;
import eu.city4age.dashboard.api.security.JwtIssuer;

/*
 * author: marina.andric
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class UserServiceTest {

	@Spy
	@InjectMocks
	UserEndpoint userService = new UserEndpoint();
	
	static protected Logger logger = LogManager.getLogger(UserServiceTest.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Autowired
	UserInRoleRepository userInRoleRepository;	
	
	@Mock
	UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	PilotRepository pilotRepository;
	
	@Mock
	PilotRepository pilotRepositoryMock;
	
	@Autowired
	UserInSystemRepository userInSystemRepository;
	
    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }
	
	@Test
	@Transactional
	@Rollback(true)
	public void loginTest() throws IOException {
		
		Pilot plt = new Pilot();
		plt.setPilotCode(PilotCode.LCC);
		plt.setName(Pilot.PilotCode.LCC.getName());
		pilotRepository.save(plt);
		
		String username = "username1";
		String password = "password1";
		
		UserInSystem uis = new UserInSystem();
		uis.setUsername(username);
		uis.setPassword(password);
		uis.setDisplayName("test");
		userInSystemRepository.save(uis);
		
		UserInRole uir = new UserInRole();
		uir.setPilot(plt);
		uir.setPilotCode(PilotCode.LCC);
		uir.setUserInSystem(uis);
		uir.setRoleId((short) 1);
		userInRoleRepository.save(uir);
		
		UserInRole user = userInRoleRepository.findBySystemUsernameAndPassword(username, password);
		Mockito.when(userInRoleRepositoryMock.findBySystemUsernameAndPassword(username, password)).thenReturn(user);
		
		Pilot pilot = pilotRepository.findOne(uir.getPilotCode());
		Mockito.when(pilotRepositoryMock.findOne(uir.getPilotCode())).thenReturn(pilot);
		
        String token = JwtIssuer.INSTANCE.createAndSign(uir.getUserInSystem().getUsername(), uir.getRoleId(), uir.getPilotCode());

		C4ALoginResponse response = (C4ALoginResponse) userService.login(username, password).getEntity();
		Assert.assertEquals(response.getPilotName(), uir.getPilotCode().getName());
		Assert.assertEquals(response.getDisplayName(), uis.getDisplayName());
		Assert.assertEquals(response.getUirId(), uir.getId());
		Assert.assertEquals(response.getResponseCode(), 200);
		Assert.assertEquals(response.getJwToken(), token);
		
	}
	
}
