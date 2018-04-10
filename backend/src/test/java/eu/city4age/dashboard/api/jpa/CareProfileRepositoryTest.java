package eu.city4age.dashboard.api.jpa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.CareProfile;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class CareProfileRepositoryTest {

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private CareProfileRepository careProfileRepository;
	
	private static Logger logger = LogManager.getLogger(CareProfileRepositoryTest.class);

	@Test
	@Transactional
	@Rollback(true)
	public void findByUserIdTest() {
		
		long uid1, uid2;
		
		Pilot.PilotCode pilotCode = Pilot.PilotCode.LCC;

		UserInRole uir1 = new UserInRole();
		uir1.setPilotCode(pilotCode);
		uir1=userInRoleRepository.save(uir1);
		uid1=uir1.getId();
		
		CareProfile cp1 = new CareProfile();
		cp1.setUserInRoleId(uid1);
		
		careProfileRepository.save(cp1);
		
		CareProfile cp2=careProfileRepository.findByUserId(uid1);
		Assert.assertNotNull(cp2);
		uid2=cp2.getUserInRoleId();
		
		logger.info("UID1: "+uid1+" UID2: "+uid2);
		Assert.assertEquals(uid2, uid1);
		
	}

}
