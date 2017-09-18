package eu.city4age.dashboard.api.persist;

import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DetectionVariableRepositoryTest {
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllMEADvTypeByPilotCode() {
		
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt1);
		dv1.setDefaultTypicalPeriod("DAY");
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setPilotCode("LCC");
		pdv1.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setPilotCode("ATH");
		pdv2.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setId(3L);
		pdv3.setPilotCode("LCC");
		pdv3.setDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv3);
		
		PilotDetectionVariable pdv4 = new PilotDetectionVariable();
		pdv4.setId(4L);
		pdv4.setPilotCode("ATH");
		pdv4.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv4);
		
		List<DetectionVariable> result1 = detectionVariableRepository.findAllMEADvTypeByPilotCode("LCC");
		List<DetectionVariable> result2 = detectionVariableRepository.findAllMEADvTypeByPilotCode("ATH");
		
		Assert.assertNotNull(result1);
		Assert.assertNotNull(result2);
		Assert.assertEquals(1, result1.size());
		Assert.assertEquals(2, result2.size());
		
	}

}
