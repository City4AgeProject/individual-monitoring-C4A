package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.rest.AssessmentServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ViewPilotDetectionVariableRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);

	@Autowired
	private ViewPilotDetectionVariableRepository viewPilotDetectionVariableRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllMeaGes() {
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(11L);
		uir1.setRoleId(Short.valueOf("1"));
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);

		UserInRole uir2 = new UserInRole();
		uir2.setId(22L);
		uir2.setRoleId(Short.valueOf("2"));
		uir2.setPilotCode("ATH");
		userInRoleRepository.save(uir2);

		UserInRole uir3 = new UserInRole();
		uir3.setId(33L);
		uir3.setRoleId(Short.valueOf("3"));
		uir3.setPilotCode("MPL");
		userInRoleRepository.save(uir3);
		
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(111L);
		dv1.setDetectionVariableName("First");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(222L);
		dv2.setDetectionVariableName("Second");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.5));
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv1);
		pdv2.setPilotCode("ATH");
		pdv2.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv2.setDerivedDetectionVariable(dv2);
		pdv2.setFormula("Formula2");
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setId(3L);
		pdv3.setDetectionVariable(dv1);
		pdv3.setPilotCode("MPL");
		pdv3.setDerivationWeight(BigDecimal.valueOf(0.1));
		pdv3.setDerivedDetectionVariable(dv2);
		pdv3.setFormula("Formula3");
		pilotDetectionVariableRepository.save(pdv3);
		
		
		List<ViewPilotDetectionVariable> result = viewPilotDetectionVariableRepository.findAllMeaGes("LCC", uir1.getId());
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("LCC", result.get(0).getId().getPilotCode());
	}


}
