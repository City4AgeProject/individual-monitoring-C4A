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
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.Stakeholder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class RoleRepositoryTest {

	@Autowired
	private StakeholderRepository stakeholderRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByStakeholderAbbr() {

		Stakeholder sh1 = new Stakeholder();
		sh1.setStakeholderAbbreviation("CGS");
		sh1.setStakeholderName("Caregivers");
		sh1.setStakeholderDescription("All caregiver types.");
		stakeholderRepository.save(sh1);
		
		Stakeholder sh2 = new Stakeholder();
		sh2.setStakeholderAbbreviation("GES");
		sh2.setStakeholderName("Geriatricians");
		sh2.setStakeholderDescription("All geriatricians.");
		stakeholderRepository.save(sh2);
		
		Stakeholder sh3 = new Stakeholder();
		sh3.setStakeholderAbbreviation("GPS");
		sh3.setStakeholderName("General practice doctors");
		sh3.setStakeholderDescription("General practiotioner team.");
		stakeholderRepository.save(sh3);
		
		Stakeholder sh4 = new Stakeholder();
		sh4.setStakeholderAbbreviation("DRL");
		sh4.setStakeholderName("Dropdown list");
		sh4.setStakeholderDescription("All roles in dropdown list.");
		stakeholderRepository.save(sh4);

		Role r1 = new Role();
		r1.setId(1L);
		r1.setRoleName("Caregiver");
		r1.setRoleAbbreviation("CRG");
		r1.setStakeholderAbbreviation("DRL");
		roleRepository.save(r1);
		
		Role r2 = new Role();
		r2.setId(2L);
		r2.setRoleName("Geriatrician");
		r2.setRoleAbbreviation("GER");
		r2.setStakeholderAbbreviation("DRL");
		roleRepository.save(r2);
		
		Role r3 = new Role();
		r3.setId(3L);
		r3.setRoleName("Intervention staff");
		r3.setRoleAbbreviation("INS");
		r3.setStakeholderAbbreviation("DRL");
		roleRepository.save(r3);
		
		Role r4 = new Role();
		r4.setId(4L);
		r4.setRoleName("City 4 Age staff");
		r4.setRoleAbbreviation("C4S");
		r4.setStakeholderAbbreviation("DRL");
		roleRepository.save(r4);
		
		List<Role> result = roleRepository.findByStakeholderAbbreviation("DRL");

		Assert.assertNotNull(result);

		Assert.assertEquals(4, result.size());
		
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());

	}

}
