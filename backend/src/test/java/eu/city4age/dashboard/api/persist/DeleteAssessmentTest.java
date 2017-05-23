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
	private UserInSystemRepository userInSystemRepository;

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



		
		logger.info("1****"+assessmentRepository.count());
		logger.info("1****"+audienceRolesRepository.count());
		logger.info("1*****"+assessedGefValuesRepository.count());
		logger.info("11****"+geriatricFactorRepository.count());
		logger.info("111****"+roleRepository.count());
		logger.info("111****"+userInSystemRepository.count());
		
		Role r1 = new Role();
		r1.setId(1L);
		roleRepository.save(r1);
		
		Role r2 = new Role();
		r2.setId(2L);
		roleRepository.save(r2);
		
		
		UserInSystem uis1 = new UserInSystem();
		uis1.setId(101L);
		userInSystemRepository.save(uis1);
		
		UserInSystem uis2 = new UserInSystem();
		uis2.setId(202L);
		userInSystemRepository.save(uis2);
		

		UserInSystem uis3 = new UserInSystem();
		uis3.setId(101L);
		userInSystemRepository.save(uis3);
		

		UserInSystem uis4 = new UserInSystem();
		uis4.setId(101L);
		userInSystemRepository.save(uis4);
		

		UserInSystem uis5 = new UserInSystem();
		uis5.setId(101L);
		userInSystemRepository.save(uis5);
		
		
		//***
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setUserInSystem(uis1);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2.setUserInSystem(uis2);
		userInRoleRepository.save(uir2);
		
		UserInRole uir3 = new UserInRole();
		uir3.setId(3L);
		uir3.setUserInSystem(uis3);
		userInRoleRepository.save(uir3);
		
		UserInRole uir4 = new UserInRole();
		uir4.setId(4L);
		uir4.setUserInSystem(uis4);
		userInRoleRepository.save(uir4);
		
		UserInRole uir5 = new UserInRole();
		uir5.setId(5L);
		uir5.setUserInSystem(uis5);
		userInRoleRepository.save(uir5);
		
		//*******
		logger.info("2****"+assessmentRepository.count());
		logger.info("2****"+audienceRolesRepository.count());
		logger.info("2*****"+assessedGefValuesRepository.count());
		
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
		
		
		logger.info("3*****"+assessmentRepository.count());
		logger.info("3*****"+audienceRolesRepository.count());
		logger.info("3*****"+assessedGefValuesRepository.count());
		logger.info("33****"+geriatricFactorRepository.count());
		
		
		//Assessment:
		Assessment a1 = new Assessment();
		a1.setId(1L);
		a1.setAssessmentComment("CASCADE DELETE TEST");
		a1.setDataValidity('F');
		a1.setGeriatricFactorValue(gef1);//add in assessedGefValuesRepository
		a1.setUserInRole(uir1);
		a1.getRoles().add(r1); //add in audienceRolesRepository
		a1.setRiskStatus('W');
		
		Assessment a2 = new Assessment();
		a2.setId(2L);
		a2.setAssessmentComment("CASCADE DELETE TEST");
		a2.setDataValidity('F');
		a2.setGeriatricFactorValue(gef2);
		a2.setUserInRole(uir2);
		a2.getRoles().add(r2); 
		a2.setRiskStatus('W');
		
		Assessment a3 = new Assessment();
		a3.setId(3L);
		a3.setAssessmentComment("CASCADE DELETE TEST");
		a3.setDataValidity('F');
		a3.setGeriatricFactorValue(gef3);
		a3.setUserInRole(uir3);
		a3.getRoles().add(r2); 
		a3.setRiskStatus('W');
		
		Assessment a4 = new Assessment();
		a4.setId(4L);
		a4.setAssessmentComment("CASCADE DELETE TEST");
		a4.setDataValidity('F');
		a4.setGeriatricFactorValue(gef4);
		a4.setUserInRole(uir3);
		a4.getRoles().add(r2); 
		a4.setRiskStatus('W');
		
		Assessment a5 = new Assessment();
		a5.setId(5L);
		a5.setAssessmentComment("CASCADE DELETE TEST");
		a5.setDataValidity('F');
		a5.setGeriatricFactorValue(gef5);
		a5.setUserInRole(uir2);
		a5.getRoles().add(r2); 
		a5.setRiskStatus('W');
		
		
		assessmentRepository.save(a1);
		assessmentRepository.save(a2);
		assessmentRepository.flush();
		logger.info("***a1.getId():"+a1.getId());
		logger.info("***a2.getId():"+a2.getId());
		
		logger.info("***gef1.getId():"+gef1.getId());
		logger.info("***gef2.getId():"+gef2.getId());
		
		logger.info("***uir1.getId():"+uir1.getId());
		logger.info("***uir2.getId():"+uir2.getId());
		
		logger.info("4*****"+assessmentRepository.count());
		logger.info("4*****"+audienceRolesRepository.count());
		logger.info("4*****"+assessedGefValuesRepository.count());
		logger.info("44****"+geriatricFactorRepository.count());

		//List<AssessedGefValueSet> agvs = assessedGefValuesRepository.findByAssessmentId(a1.getId().intValue());
		//List<AssessmentAudienceRole> aar = audienceRolesRepository.findByAssessmentId(a1.getId().intValue());

		Long idA1 = a1.getId();
		assessmentRepository.delete(a1);
		assessmentRepository.delete(a2);
		assessmentRepository.flush();
		
		logger.info("FFDD"+assessmentRepository.findOne(idA1));
		logger.info("#***a1.getId():"+a1.getId());
		logger.info("#***a2.getId():"+a2.getId());
		
		logger.info("#***gef1.getId():"+gef1.getId());
		logger.info("#***gef2.getId():"+gef2.getId());
		logger.info("#***uir1.getId():"+uir1.getId());
		logger.info("#***uir2.getId():"+uir2.getId());
		
		logger.info("****"+assessmentRepository.count());
		logger.info("****"+audienceRolesRepository.count());
		logger.info("*****"+assessedGefValuesRepository.count());
		logger.info("****"+geriatricFactorRepository.count());
		logger.info("****"+roleRepository.count());
		logger.info("****"+userInSystemRepository.count());

	}

}
