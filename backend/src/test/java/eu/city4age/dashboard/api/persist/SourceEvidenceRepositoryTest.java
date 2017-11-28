package eu.city4age.dashboard.api.persist;

import java.util.Date;
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
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.SourceEvidenceRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.SourceEvidence;
import eu.city4age.dashboard.api.pojo.domain.SourceEvidenceId;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SourceEvidenceRepositoryTest {
	
	private static Logger logger = LogManager.getLogger(SourceEvidenceRepositoryTest.class);
	
	@Autowired
	private SourceEvidenceRepository sourceEvidenceRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllWithGfg() {
		
		GeriatricFactorValue gfv1 = new GeriatricFactorValue();
		geriatricFactorRepository.save(gfv1);
	
		SourceEvidence se1 = new SourceEvidence();
		se1.setSourceEvidenceId(new SourceEvidenceId());
		se1.getSourceEvidenceId().setValueId(gfv1.getId().intValue());
		se1.getSourceEvidenceId().setDetectionVariableType("GEF");
		se1.setGeriatricFactorValue(gfv1);
		sourceEvidenceRepository.save(se1);
		
		List<SourceEvidence> result = sourceEvidenceRepository.findAll();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(1, result.size());

		Assert.assertNotNull(result.get(0).getGeriatricFactorValue());
		Assert.assertEquals(new Long(gfv1.getId()), result.get(0).getGeriatricFactorValue().getId());
		
		Assert.assertNull(result.get(0).getVariationMeasureValue());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllWithVmv() {
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		variationMeasureValueRepository.save(vmv1);
	
		SourceEvidence se1 = new SourceEvidence();
		se1.setSourceEvidenceId(new SourceEvidenceId());
		se1.getSourceEvidenceId().setValueId(vmv1.getId().intValue());
		se1.getSourceEvidenceId().setDetectionVariableType("MEA");
		se1.setVariationMeasureValue(vmv1);
		sourceEvidenceRepository.save(se1);
		
		List<SourceEvidence> result = sourceEvidenceRepository.findAll();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(1, result.size());
		
		Assert.assertEquals(1, result.get(0).getSourceEvidenceId().getValueId());
		
		Assert.assertNull(result.get(0).getGeriatricFactorValue());
	
		Assert.assertNotNull(result.get(0).getVariationMeasureValue());
		Assert.assertEquals(new Long(1L), result.get(0).getVariationMeasureValue().getId());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllWithGefAndVmv() {
		
		GeriatricFactorValue gfv1 = new GeriatricFactorValue();
		geriatricFactorRepository.save(gfv1);
	
		SourceEvidence se1 = new SourceEvidence();
		se1.setSourceEvidenceId(new SourceEvidenceId());
		se1.getSourceEvidenceId().setValueId(gfv1.getId().intValue());
		se1.getSourceEvidenceId().setDetectionVariableType("GEF");
		se1.setGeriatricFactorValue(gfv1);
		sourceEvidenceRepository.save(se1);
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(gfv1.getId());
		variationMeasureValueRepository.save(vmv1);
	
		SourceEvidence se2 = new SourceEvidence();
		se2.setSourceEvidenceId(new SourceEvidenceId());
		se2.getSourceEvidenceId().setValueId(vmv1.getId().intValue());
		se2.getSourceEvidenceId().setDetectionVariableType("MEA");
		se2.setVariationMeasureValue(vmv1);
		sourceEvidenceRepository.save(se2);
		
		GeriatricFactorValue gfv2 = new GeriatricFactorValue();
		geriatricFactorRepository.save(gfv2);
	
		SourceEvidence se3 = new SourceEvidence();
		se3.setSourceEvidenceId(new SourceEvidenceId());
		se3.getSourceEvidenceId().setValueId(gfv2.getId().intValue());
		se3.getSourceEvidenceId().setDetectionVariableType("GEF");
		se3.setGeriatricFactorValue(gfv2);
		sourceEvidenceRepository.save(se3);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(gfv2.getId());
		variationMeasureValueRepository.save(vmv2);
	
		SourceEvidence se4 = new SourceEvidence();
		se4.setSourceEvidenceId(new SourceEvidenceId());
		se4.getSourceEvidenceId().setValueId(vmv2.getId().intValue());
		se4.getSourceEvidenceId().setDetectionVariableType("MEA");
		se4.setVariationMeasureValue(vmv2);
		sourceEvidenceRepository.save(se4);
		
		List<SourceEvidence> result = sourceEvidenceRepository.findAll();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(4, result.size());

		Assert.assertEquals(gfv1.getId().intValue(), result.get(0).getSourceEvidenceId().getValueId());
		Assert.assertNotNull(result.get(0).getGeriatricFactorValue());
		Assert.assertEquals(new Long(gfv1.getId()), result.get(0).getGeriatricFactorValue().getId());
		Assert.assertNull(result.get(0).getVariationMeasureValue());
		
		Assert.assertEquals(gfv1.getId().intValue(), result.get(1).getSourceEvidenceId().getValueId());
		Assert.assertNull(result.get(1).getGeriatricFactorValue());
		Assert.assertNotNull(result.get(1).getVariationMeasureValue());
		Assert.assertEquals(gfv1.getId(), result.get(1).getVariationMeasureValue().getId());
		
		Assert.assertEquals(gfv2.getId().intValue(), result.get(2).getSourceEvidenceId().getValueId());
		Assert.assertNotNull(result.get(2).getGeriatricFactorValue());
		Assert.assertEquals(new Long(gfv2.getId()), result.get(2).getGeriatricFactorValue().getId());
		Assert.assertNull(result.get(2).getVariationMeasureValue());
		
		Assert.assertEquals(gfv2.getId().intValue(), result.get(3).getSourceEvidenceId().getValueId());
		Assert.assertNull(result.get(3).getGeriatricFactorValue());
		Assert.assertNotNull(result.get(3).getVariationMeasureValue());
		Assert.assertEquals(gfv2.getId(), result.get(3).getVariationMeasureValue().getId());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllGef() {
		
		GeriatricFactorValue gfv1 = new GeriatricFactorValue();
		geriatricFactorRepository.save(gfv1);
	
		SourceEvidence se1 = new SourceEvidence();
		se1.setSourceEvidenceId(new SourceEvidenceId());
		se1.getSourceEvidenceId().setValueId(gfv1.getId().intValue());
		se1.getSourceEvidenceId().setDetectionVariableType("GEF");
		se1.setGeriatricFactorValue(gfv1);
		sourceEvidenceRepository.save(se1);
		
		GeriatricFactorValue gfv2 = new GeriatricFactorValue();
		geriatricFactorRepository.save(gfv2);
	
		SourceEvidence se3 = new SourceEvidence();
		se3.setSourceEvidenceId(new SourceEvidenceId());
		se3.getSourceEvidenceId().setValueId(gfv2.getId().intValue());
		se3.getSourceEvidenceId().setDetectionVariableType("GEF");
		se3.setGeriatricFactorValue(gfv2);
		sourceEvidenceRepository.save(se3);
		
		List<SourceEvidence> result = sourceEvidenceRepository.findAllGef();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(2, result.size());

		Assert.assertEquals(gfv1.getId().intValue(), result.get(0).getSourceEvidenceId().getValueId());
		Assert.assertNotNull(result.get(0).getGeriatricFactorValue());
		Assert.assertEquals(new Long(gfv1.getId()), result.get(0).getGeriatricFactorValue().getId());
		Assert.assertNull(result.get(0).getVariationMeasureValue());
		
		Assert.assertEquals(gfv2.getId().intValue(), result.get(1).getSourceEvidenceId().getValueId());
		Assert.assertNotNull(result.get(1).getGeriatricFactorValue());
		Assert.assertEquals(new Long(gfv2.getId()), result.get(1).getGeriatricFactorValue().getId());
		Assert.assertNull(result.get(1).getVariationMeasureValue());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllMea() {
			
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		variationMeasureValueRepository.save(vmv1);
	
		SourceEvidence se2 = new SourceEvidence();
		se2.setSourceEvidenceId(new SourceEvidenceId());
		se2.getSourceEvidenceId().setValueId(vmv1.getId().intValue());
		se2.getSourceEvidenceId().setDetectionVariableType("MEA");
		se2.setVariationMeasureValue(vmv1);
		sourceEvidenceRepository.save(se2);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(2L);
		variationMeasureValueRepository.save(vmv2);
	
		SourceEvidence se4 = new SourceEvidence();
		se4.setSourceEvidenceId(new SourceEvidenceId());
		se4.getSourceEvidenceId().setValueId(vmv2.getId().intValue());
		se4.getSourceEvidenceId().setDetectionVariableType("MEA");
		se4.setVariationMeasureValue(vmv2);
		sourceEvidenceRepository.save(se4);
		
		List<SourceEvidence> result = sourceEvidenceRepository.findAllMea();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(2, result.size());

		Assert.assertEquals(1, result.get(0).getSourceEvidenceId().getValueId());
		Assert.assertNull(result.get(0).getGeriatricFactorValue());
		Assert.assertNotNull(result.get(0).getVariationMeasureValue());
		Assert.assertEquals(new Long(1), result.get(0).getVariationMeasureValue().getId());
		
		Assert.assertEquals(2, result.get(1).getSourceEvidenceId().getValueId());
		Assert.assertNull(result.get(1).getGeriatricFactorValue());
		Assert.assertNotNull(result.get(1).getVariationMeasureValue());
		Assert.assertEquals(new Long(2), result.get(1).getVariationMeasureValue().getId());
		
	}

}
