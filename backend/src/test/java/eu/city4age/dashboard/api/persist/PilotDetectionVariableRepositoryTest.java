package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.util.Date;
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
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PilotDetectionVariableRepositoryTest {

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	
	
	

	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllForDvtNUI() {
		
		DetectionVariableType dvt1 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setId(3L);
		pdv3.setDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv3);

		List<PilotDetectionVariable> result = pilotDetectionVariableRepository.findAllForDvtNUI();
		
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());

	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRunFormula() {

		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setFormula("formula");
		pilotDetectionVariableRepository.save(pdv1);

		String result = pilotDetectionVariableRepository.runFormula();
		
		Assert.assertNotNull(result);
		Assert.assertEquals("formula", result);

	}
	
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
		
		List<PilotDetectionVariable> result1 = pilotDetectionVariableRepository.findAllMEADvTypeByPilotCode("LCC");
		List<PilotDetectionVariable> result2 = pilotDetectionVariableRepository.findAllMEADvTypeByPilotCode("ATH");
		
		Assert.assertNotNull(result1);
		Assert.assertNotNull(result2);
		Assert.assertEquals(1, result1.size());
		Assert.assertEquals(2, result2.size());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDetectionVariableAndPilotCode() {
		
		Long dvId = 1L;
		String pilotCode = "LCC";
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		detectionVariableRepository.save(dv1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode(pilotCode);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable result = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dvId, pilotCode );
		
		Assert.assertNotNull(result);
		Assert.assertEquals(pilotCode, result.getPilotCode());
		
	}

}
