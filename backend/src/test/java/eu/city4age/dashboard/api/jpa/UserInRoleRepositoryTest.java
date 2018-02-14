package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.jpa.CrProfileRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserInRoleRepositoryTest {
	
	private static Logger logger = LogManager.getLogger(UserInRoleRepositoryTest.class);

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CrProfileRepository crProfileRepository;
	
	@Autowired
	private UserInSystemRepository userInSystemRepository;
	
		
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByRoleIdAndPilotCode() throws Exception {
		
		Role r1 = new Role ();
		r1.setId(1L);
		roleRepository.save(r1);
		
		Role r2 = new Role ();
		r2.setId(2L);
		roleRepository.save(r2); 
		
		UserInRole uir1 = new UserInRole ();
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1.setRoleId((short)1);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setPilotCode(Pilot.PilotCode.LCC);
		uir2.setRoleId((short)1);
		userInRoleRepository.save(uir2);
		
		UserInRole uir3 = new UserInRole ();
		uir3.setPilotCode(Pilot.PilotCode.ATH);
		uir3.setRoleId((short)1);
		userInRoleRepository.save(uir3);
		
		UserInRole uir4 = new UserInRole ();
		uir4.setPilotCode(Pilot.PilotCode.ATH);
		uir4.setRoleId((short)2);
		userInRoleRepository.save(uir4);
		
				
		List<UserInRole> result = userInRoleRepository.findByRoleIdAndPilotCode((short)1, Pilot.PilotCode.valueOf("LCC"));
		Assert.assertNotNull (result);
		Assert.assertEquals (2, result.size());
		Assert.assertEquals(uir1.getId(), result.get(0).getId());
		Assert.assertEquals(uir2.getId(), result.get(1).getId());
		
		result = userInRoleRepository.findByRoleIdAndPilotCode((short)2, Pilot.PilotCode.valueOf("LCC"));
		Assert.assertNotNull (result);
		Assert.assertEquals (0, result.size());
		
		result = userInRoleRepository.findByRoleIdAndPilotCode((short)1, Pilot.PilotCode.valueOf("ATH"));
		Assert.assertNotNull (result);
		Assert.assertEquals (1, result.size());
		Assert.assertEquals(uir3.getId(), result.get(0).getId());
		
		result = userInRoleRepository.findByRoleIdAndPilotCode((short)2, Pilot.PilotCode.valueOf("ATH"));
		Assert.assertNotNull (result);
		Assert.assertEquals (1, result.size());
		Assert.assertEquals(uir4.getId(), result.get(0).getId()); 
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindBySystemUsernameAndPassword () {
		
		UserInSystem uis1 = new UserInSystem ();
		uis1.setUsername("aaaa");
		uis1.setPassword("aaaa");
		userInSystemRepository.save(uis1);
		
		UserInSystem uis2 = new UserInSystem ();
		uis2.setUsername("bbbb");
		uis2.setPassword("bbbb");
		userInSystemRepository.save(uis2);
		
		UserInRole uir1 = new UserInRole ();
		uir1.setUserInSystem(uis1);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setUserInSystem(uis2);
		userInRoleRepository.save(uir2);
		
		UserInRole result = userInRoleRepository.findBySystemUsernameAndPassword("aaaa", "aaaa");
		Assert.assertNotNull(result);
		Assert.assertEquals(uir1.getId(), result.getId());
		
		result = userInRoleRepository.findBySystemUsernameAndPassword("bbbb", "bbbb");
		Assert.assertNotNull(result);
		Assert.assertEquals(uir2.getId(), result.getId());
		
		result = userInRoleRepository.findBySystemUsernameAndPassword("cccc", "cccc");
		Assert.assertNull(result);
		
	}

}
