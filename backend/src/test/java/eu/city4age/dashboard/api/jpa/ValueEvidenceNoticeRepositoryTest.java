package eu.city4age.dashboard.api.jpa;

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
import eu.city4age.dashboard.api.jpa.SourceEvidenceRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ValueEvidenceNoticeRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.SourceEvidence;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ValueEvidenceNotice;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ValueEvidenceNoticeRepositoryTest {
	
	private static Logger logger = LogManager.getLogger(ValueEvidenceNoticeRepositoryTest.class);
	
	@Autowired
	private ValueEvidenceNoticeRepository valueEvidenceNoticeRepository;
	
	@Autowired
	private SourceEvidenceRepository sourceEvidenceRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Test
	@Transactional
	@Rollback(false)
	public void test() throws Exception {
		
		List<ValueEvidenceNotice> ven = valueEvidenceNoticeRepository.findAll();
		
		Assert.assertEquals(0, ven.size());
		
		SourceEvidence se1 = new SourceEvidence ();
		se1.setId(1L);
		se1 = sourceEvidenceRepository.save(se1);
		
		Assert.assertNotNull(se1);
		
		SourceEvidence se2 = new SourceEvidence ();
		se2.setId(2L);
		se2 = sourceEvidenceRepository.save(se2);
		
		Assert.assertNotNull(se2);
		
		UserInRole uir1 = new UserInRole ();
		uir1.setId(1L);
		uir1 = userInRoleRepository.save (uir1);
		
		Assert.assertNotNull(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setId(2L);
		uir2 = userInRoleRepository.save (uir2);
		
		Assert.assertNotNull(uir2);
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setMeasureValue(new BigDecimal (2));
		vmv1 = variationMeasureValueRepository.save (vmv1);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(2L);
		vmv2.setMeasureValue(new BigDecimal (22));
		vmv2 = variationMeasureValueRepository.save (vmv2);
		
		ValueEvidenceNotice ven1 = new ValueEvidenceNotice ();
		ven1.setNotice("notice1");
		ven1.setSourceEvidence(se1);
		ven1.setUserInRole(uir1);
		ven1.setValue(vmv1);
		
		Assert.assertNotNull(ven1);
		ven1 = valueEvidenceNoticeRepository.save(ven1);
		
		ven = valueEvidenceNoticeRepository.findAll();
		
		Assert.assertEquals(1, ven.size()); 
	}
}
