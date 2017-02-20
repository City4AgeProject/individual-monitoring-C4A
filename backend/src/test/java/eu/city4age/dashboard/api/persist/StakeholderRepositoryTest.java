package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.persist.StakeholderRepository;
import eu.city4age.dashboard.api.pojo.domain.Stakeholder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ApplicationTest.class)
@ActiveProfiles("test")
public class StakeholderRepositoryTest {

	@Autowired
	private StakeholderRepository stakeholderRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAll() {

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
		
		List<Stakeholder> result = stakeholderRepository.findAll();

		Assert.assertNotNull(result);

		Assert.assertEquals(4, result.size());
		
	}

}
