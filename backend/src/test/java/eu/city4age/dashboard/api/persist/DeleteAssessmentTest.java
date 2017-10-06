package eu.city4age.dashboard.api.persist;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.persist.Filter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DeleteAssessmentTest {
	
	static protected Logger logger = LogManager.getLogger(DeleteAssessmentTest.class);

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AudienceRolesRepository audienceRolesRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;
	


	@Test
	@Transactional
	@Rollback(true)
	public void DeleteAssessmentTest() throws Exception {
		
		Role r1 = new Role();
		r1.setId(1L);
		roleRepository.save(r1);
		
		Role r2 = new Role();
		r2.setId(2L);
		roleRepository.save(r2);	
		
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		userInRoleRepository.save(uir2);
		
		UserInRole uir3 = new UserInRole();
		uir3.setId(3L);
		userInRoleRepository.save(uir3);
		
		UserInRole uir4 = new UserInRole();
		uir4.setId(4L);
		userInRoleRepository.save(uir4);
		
		UserInRole uir5 = new UserInRole();
		uir5.setId(5L);
		userInRoleRepository.save(uir5);
				
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(11L);
		gef1.setGefValue(new BigDecimal("1"));
		gef1.setUserInRole(uir1);		
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(22L);
		gef2.setGefValue(new BigDecimal("2"));
		gef2.setUserInRole(uir2);
		geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(33L);
		gef3.setGefValue(new BigDecimal("3"));
		gef3.setUserInRole(uir3);
		geriatricFactorRepository.save(gef3);

		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setId(44L);
		gef4.setGefValue(new BigDecimal("4"));
		gef4.setUserInRole(uir4);
		geriatricFactorRepository.save(gef4);
		
		GeriatricFactorValue gef5 = new GeriatricFactorValue();
		gef5.setId(55L);
		gef5.setGefValue(new BigDecimal("5"));
		gef5.setUserInRole(uir5);
		geriatricFactorRepository.save(gef5);
				
		
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole> ();
		
		Assessment a1 = new Assessment();
		a1.setId(1L);
		a1.setAssessmentComment("CASCADE DELETE TEST");
		a1.setDataValidity('F');
		a1.setGeriatricFactorValue(gef1);//add in assessedGefValuesRepository
		gef1.addAssessment(a1);
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a1.getId().intValue()).gefValueId(gef1.getId().intValue()).build());
		a1.setUserInRole(uir1);
		a1.getRoles().add(r1); //add in audienceRolesRepository
		a1.getRoles().add(r2);
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a1.getId().intValue()).userInRoleId(uir1.getId().intValue()).build());
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a1.getId().intValue()).userInRoleId(uir2.getId().intValue()).build());
		a1.setRiskStatus('W');
		
		Assessment a2 = new Assessment();
		a2.setId(2L);
		a2.setAssessmentComment("CASCADE DELETE TEST");
		a2.setDataValidity('F');
		a2.setGeriatricFactorValue(gef2);
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a1.getId().intValue()).gefValueId(gef2.getId().intValue()).build());
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a2.getId().intValue()).gefValueId(gef2.getId().intValue()).build());
		a2.setUserInRole(uir2);
		a2.getRoles().add(r2); 
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a2.getId().intValue()).userInRoleId(uir2.getId().intValue()).build());
		a2.setRiskStatus('W');
		
		Assessment a3 = new Assessment();
		a3.setId(3L);
		a3.setAssessmentComment("CASCADE DELETE TEST");
		a3.setDataValidity('F');
		a3.setGeriatricFactorValue(gef3);
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a3.getId().intValue()).gefValueId(gef3.getId().intValue()).build());
		a3.setUserInRole(uir3);
		a3.getRoles().add(r2); 
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a3.getId().intValue()).userInRoleId(uir3.getId().intValue()).build());
		a3.setRiskStatus('W');
		
		Assessment a4 = new Assessment();
		a4.setId(4L);
		a4.setAssessmentComment("CASCADE DELETE TEST");
		a4.setDataValidity('F');
		a4.setGeriatricFactorValue(gef4);
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a4.getId().intValue()).gefValueId(gef4.getId().intValue()).build());
		a4.setUserInRole(uir3);
		a4.getRoles().add(r2); 
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a4.getId().intValue()).userInRoleId(uir4.getId().intValue()).build());
		a4.setRiskStatus('W');
		
		Assessment a5 = new Assessment();
		a5.setId(5L);
		a5.setAssessmentComment("CASCADE DELETE TEST");
		a5.setDataValidity('F');
		a5.setGeriatricFactorValue(gef5);
		assessedGefValueSets.add (new AssessedGefValueSet.AssessedGefValueSetBuilder().assessmentId(a5.getId().intValue()).gefValueId(gef5.getId().intValue()).build());
		a5.setUserInRole(uir2);
		a5.getRoles().add(r2);
		assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder().assessmentId(a5.getId().intValue()).userInRoleId(uir5.getId().intValue()).build());
		a5.setRiskStatus('W');		
		
		assessmentRepository.save(a1);
		assessmentRepository.save(a2);
		assessmentRepository.save(a3);
		assessmentRepository.save(a4);
		assessmentRepository.save(a5);		
		
		assessedGefValuesRepository.save(assessedGefValueSets);
		audienceRolesRepository.save(assessmentAudienceRoles);
		

		List<AssessedGefValueSet> resultGEF = assessedGefValuesRepository.findByAssessmentId(a1.getId().intValue());
		List<AssessmentAudienceRole> resultRoles = audienceRolesRepository.findByAssessmentId(a1.getId().intValue());
		
		Assert.assertNotNull(resultGEF);
		Assert.assertNotNull(resultRoles);
		Assert.assertEquals(2, resultRoles.size());
		Assert.assertEquals(2, resultGEF.size());	
		
		resultGEF = assessedGefValuesRepository.findByAssessmentId (a2.getId().intValue());
		
		Assert.assertNotNull(resultGEF);
		Assert.assertEquals(1, resultGEF.size());
		Assert.assertEquals(gef2.getId().intValue(), resultGEF.get(0).getGefValueId().intValue());

	}

}
