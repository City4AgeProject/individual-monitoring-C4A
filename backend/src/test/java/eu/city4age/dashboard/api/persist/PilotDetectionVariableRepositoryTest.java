package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PilotDetectionVariableRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableRepositoryTest.class);

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
	public void testFindWeightByDetectionVariableAndPilotCodeGesGef () {
			
		DetectionVariableType dvt1 = DetectionVariableType.GEF;
		DetectionVariableType dvt2 = DetectionVariableType.GES;
		DetectionVariableType dvt3 = DetectionVariableType.MEA;
		
		detectionVariableTypeRepository.save(dvt1);
		detectionVariableTypeRepository.save(dvt2);
		detectionVariableTypeRepository.save(dvt3);
		
		DetectionVariable dv1 = new DetectionVariable ();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable ();
		dv2.setId(2L);
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable ();
		dv3.setId(3L);
		dv3.setDetectionVariableType(dvt3);
		detectionVariableRepository.save(dv3);
				
		PilotDetectionVariable pdv1 = new PilotDetectionVariable ();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv2);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(new BigDecimal (1));
		pdv1.setDerivedDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv2);
		pdv2.setPilotCode("ATH");
		pdv2.setDerivationWeight(new BigDecimal (2));
		pdv2.setDerivedDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable ();
		pdv3.setId(3L);
		pdv3.setDetectionVariable(dv3);
		pdv3.setPilotCode("LCC");
		pdv3.setDerivationWeight(new BigDecimal (3));
		pdv3.setDerivedDetectionVariable(dv1);
		pilotDetectionVariableRepository.save(pdv3);
		
		PilotDetectionVariable pdv4 = new PilotDetectionVariable ();
		pdv4.setId(4L);
		pdv4.setDetectionVariable(dv2);
		pdv4.setPilotCode("LCC");
		pdv4.setDerivationWeight(new BigDecimal (3));
		pdv4.setDerivedDetectionVariable(dv3);
		pilotDetectionVariableRepository.save(pdv4);
		
		BigDecimal result = pilotDetectionVariableRepository.findWeightByDetectionVariableAndPilotCodeGesGef(2L, "LCC");
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv1.getDerivationWeight().doubleValue(), result.doubleValue(), 0.001);
		
		result = pilotDetectionVariableRepository.findWeightByDetectionVariableAndPilotCodeGesGef(2L, "ATH");
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv2.getDerivationWeight().doubleValue(), result.doubleValue(), 0.001);
		
		result = pilotDetectionVariableRepository.findWeightByDetectionVariableAndPilotCodeGesGef(1L, "LCC");
		Assert.assertNull(result);
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId () {
		String pilotCode1 = "LCC";
		String pilotCode2 = "ATH";
		
		DetectionVariableType dvt = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt);
		
		DetectionVariable dv1 = new DetectionVariable ();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable ();
		dv2.setId(2L);
		dv2.setDetectionVariableType(dvt);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable ();
		dv3.setId(3L);
		dv3.setDetectionVariableType(dvt);
		detectionVariableRepository.save(dv3);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable ();
		pdv1.setId(1L);
		pdv1.setPilotCode(pilotCode1);
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setId(2L);
		pdv2.setPilotCode(pilotCode1);
		pdv2.setDetectionVariable(dv1);
		pdv2.setDerivedDetectionVariable(dv3);
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable ();
		pdv3.setId(3L);
		pdv3.setPilotCode(pilotCode1);
		pdv3.setDetectionVariable(dv3);
		pdv3.setDerivedDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv3);
		
		PilotDetectionVariable pdv4 = new PilotDetectionVariable ();
		pdv4.setId(4L);
		pdv4.setPilotCode(pilotCode2);
		pdv4.setDetectionVariable(dv1);
		pdv4.setDerivedDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv4);
		
		logger.info("count: " + pilotDetectionVariableRepository.count());
		
		PilotDetectionVariable result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode1, 1L, 2L);
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv1.getPilotCode(), result.getPilotCode());
		Assert.assertEquals(pdv1.getDetectionVariable().getId(), result.getDetectionVariable().getId());
		Assert.assertEquals(pdv1.getDerivedDetectionVariable().getId(), result.getDerivedDetectionVariable().getId());
		
		result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode1, 1L, 3L);
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv2.getPilotCode(), result.getPilotCode());
		Assert.assertEquals(pdv2.getDetectionVariable().getId(), result.getDetectionVariable().getId());
		Assert.assertEquals(pdv2.getDerivedDetectionVariable().getId(), result.getDerivedDetectionVariable().getId());
		
		result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode1, 3L, 2L);
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv3.getPilotCode(), result.getPilotCode());
		Assert.assertEquals(pdv3.getDetectionVariable().getId(), result.getDetectionVariable().getId());
		Assert.assertEquals(pdv3.getDerivedDetectionVariable().getId(), result.getDerivedDetectionVariable().getId());
		
		result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode2, 1L, 2L);
		Assert.assertNotNull(result);
		Assert.assertEquals(pdv4.getPilotCode(), result.getPilotCode());
		Assert.assertEquals(pdv4.getDetectionVariable().getId(), result.getDetectionVariable().getId());
		Assert.assertEquals(pdv4.getDerivedDetectionVariable().getId(), result.getDerivedDetectionVariable().getId());
		
		result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode1, 2L, 2L);
		Assert.assertNull(result);
		
		result = pilotDetectionVariableRepository.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pilotCode2, 2L, 2L);
		Assert.assertNull(result);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDetectionVariableAndPilotCode() {
		
		Long dvId1 = 1L;
		Long dvId2 = 2L;
		String pilotCode = "LCC";
		
		DetectionVariable dv1 = new DetectionVariable();
		DetectionVariable dv2 = new DetectionVariable ();
		
		dv1.setId(dvId1);		
		detectionVariableRepository.save(dv1);
		
		dv2.setId(dvId2);
		detectionVariableRepository.save(dv2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setPilotCode(pilotCode);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable result = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dvId1, pilotCode );		
		Assert.assertNotNull(result);
		Assert.assertEquals(pilotCode, result.getPilotCode());
		
		result = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dvId2, pilotCode );		
		Assert.assertNull(result);
		
		result = pilotDetectionVariableRepository.findByDetectionVariableAndPilotCode(dvId1, "ATH" );		
		Assert.assertNull(result);
		
	}

}
