package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserInRoleRepositoryTest {

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private UserInSystemRepository userInSystemRepository;

	// getCareRecipients
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByRoleId() throws Exception {

		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir1);

		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir2);

		UserInRole uir3 = new UserInRole();
		uir3.setId(3L);
		uir3.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir3);

		UserInRole uir4 = new UserInRole();
		uir4.setId(4L);
		uir4.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir4);

		UserInRole uir5 = new UserInRole();
		uir5.setId(5L);
		uir5.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir5);

		UserInRole uir6 = new UserInRole();
		uir6.setId(6L);
		uir6.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir6);

		UserInRole uir7 = new UserInRole();
		uir7.setId(7L);
		uir7.setRoleId(Short.valueOf("1"));
		userInRoleRepository.save(uir7);

		List<UserInRole> result = userInRoleRepository.findByRoleId(Short.valueOf("1"));

		Assert.assertNotNull(result);

		Assert.assertEquals(7, result.size());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindBySystemUsernameAndPassword() throws Exception {

		UserInSystem uis = new UserInSystem();
		uis.setId(1L);
		uis.setUsername("admin");
		uis.setPassword("secret");
		userInSystemRepository.save(uis);

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		uir.setUserInSystem(uis);
		userInRoleRepository.save(uir);

		UserInRole result = userInRoleRepository.findBySystemUsernameAndPassword("admin", "secret");

		Assert.assertNotNull(result);

		Assert.assertEquals("admin", result.getUserInSystem().getUsername());

	}

}
