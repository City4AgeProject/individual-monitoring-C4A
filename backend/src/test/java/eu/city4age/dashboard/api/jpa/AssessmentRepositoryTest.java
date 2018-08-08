package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.AudienceRolesRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.persist.Filter;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class AssessmentRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(AssessmentRepositoryTest.class);
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

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

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository; 
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindForSelectedDataSet() throws Exception {
		
		logger.info("1***Number of assessments in repository:" + assessmentRepository.findAll().size());
		//data for assessment and other joined tables:

		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1 = userInRoleRepository.save(uir1);
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2 = userInRoleRepository.save(uir2);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setGefValue(new BigDecimal("5"));
		gef1.setUserInRole(uir1);
		gef1 = geriatricFactorRepository.save(gef1);		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setGefValue(new BigDecimal("6"));
		gef2.setUserInRole(uir2);
		gef2 = geriatricFactorRepository.save(gef2);

		Role r1 = new Role();
		r1.setId(1L);
		r1 = roleRepository.save(r1);		
		Role r2 = new Role();
		r2.setId(2L);
		r2 = roleRepository.save(r2);

		/*
		 * these seem to be added automaticaly, no need for manual addition
		 * AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		ag1 = assessedGefValuesRepository.save(ag1);		
		AssessedGefValueSet ag2 = new AssessedGefValueSet();
		ag2.setGefValueId(2);
		ag2.setAssessmentId(2);
		ag2 = assessedGefValuesRepository.save(ag2);
		
		AssessmentAudienceRole ar1 = new AssessmentAudienceRole();
		ar1.setAssessmentId(1);
		ar1.setRoleId(1);
		ar1.setAssigned(new Timestamp(System.currentTimeMillis()));
		ar1 = audienceRolesRepository.save(ar1);		
		AssessmentAudienceRole ar2 = new AssessmentAudienceRole();
		ar2.setAssessmentId(2);
		ar2.setRoleId(2);
		ar2.setAssigned(new Timestamp(System.currentTimeMillis()));
		ar2 = audienceRolesRepository.save(ar2); */
		
					
		//data for second assessment and joined tables:


		Assessment aa1 = new Assessment();
		aa1.setId(1L);
		aa1.setUserInRole(uir1);
		aa1.setGeriatricFactorValue(gef1);
		aa1.getRoles().add(r1);
		aa1.setDataValidity('F');//F,Q
		aa1.setRiskStatus('W');//W,A
		aa1 = assessmentRepository.save(aa1);
	
		Assessment aa2 = new Assessment();
		aa2.setId(2L);
		aa2.setUserInRole(uir2);//
		aa2.setGeriatricFactorValue(gef1);
		aa2.getRoles().add(r2);
		aa2.setDataValidity('F');//F,Q
		aa2.setRiskStatus('W');//W,A
		aa2 = assessmentRepository.save(aa2);

		Assessment aa3 = new Assessment();
		aa3.setId(3L);
		aa3.setUserInRole(uir1);
		aa3.setGeriatricFactorValue(gef1);
		aa3.getRoles().add(r1);
		aa3.setDataValidity('Q');//F,Q
		aa3.setRiskStatus('W');//W,A
		aa3 = assessmentRepository.save(aa3);
		
		Assessment aa4 = new Assessment();
		aa4.setId(4L);
		aa4.setUserInRole(uir1);
		aa4.setGeriatricFactorValue(gef1);
		aa4.getRoles().add(r1);
		aa4.setDataValidity('F');//F,Q
		aa4.setRiskStatus('A');//W,A
		aa4 = assessmentRepository.save(aa4);
		
		Assessment aa5 = new Assessment();
		aa5.setId(5L);
		aa5.setUserInRole(uir1);
		aa5.setGeriatricFactorValue(gef2);
		aa5.getRoles().add(r1);
		aa5.setDataValidity('F');//F,Q
		aa5.setRiskStatus('W');//W,A
		aa5 = assessmentRepository.save(aa5);
		
		Assessment aa6 = new Assessment();
		aa6.setGeriatricFactorValue(gef2);
		aa6.setId(6L);
		aa6 = assessmentRepository.save(aa6);
		
		Assessment aa7 = new Assessment();
		aa7.setId(7L);
		aa7 = assessmentRepository.save(aa7);
		
		logger.info ("2***Number of assessments in repository:" + assessmentRepository.findAll().size());
		Assert.assertEquals(7, assessmentRepository.findAll().size());		
		
		List<Filter> filters = new ArrayList<Filter>();
		List<Object> inParams = new ArrayList<Object>();
		
		//riskStatus filter (W,A):
		Filter riskStatus = new Filter();
		riskStatus.setName("riskStatus");
		
		inParams = new ArrayList<Object>();
		inParams.add('W');
		
		riskStatus.getInParams().put("riskStatus", inParams);
		filters.add(riskStatus);
		
		//dataValidity filter (F,Q)
		Filter dataValidity = new Filter();
		dataValidity.setName("dataValidity");
		
		inParams = new ArrayList<Object>();
		inParams.add('F');

		dataValidity.getInParams().put("dataValidity", inParams);
		filters.add(dataValidity);
		
		//userInRoleId filter (1L):
		Filter roleId = new Filter();
		roleId.setName("roleId");
		
		inParams = new ArrayList<Object>();
		inParams.add(1L);
		
		roleId.getInParams().put("roleId", inParams);
		filters.add(roleId);
		
		
		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(gef1.getId());
		gefIds.add(gef2.getId());
		

		HashMap<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", gefIds);

		
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		logger.info("1BEFORE***(BEFORE filters)Number of assessments in repository:" + assessmentRepository.findAll().size());
		
		List<Filter> noFilters = new ArrayList<Filter>();
		HashMap<String, Object> inQueryParams2 = new HashMap<String, Object>();
		logger.info ("1@@@ assessmentRepository.findAll().size(): "+assessmentRepository.findAll().size());
		List<Assessment> resultWithoutFilters = assessmentRepository.doQueryWithFilter(noFilters , "findForSelectedDataSet",
				inQueryParams);
		logger.info ("2@@@ assessmentRepository.findAll().size() no filters: " + assessmentRepository.findAll().size());
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		
		List<Assessment> resultWithFilters = assessmentRepository.doQueryWithFilter(filters, "findForSelectedDataSet",
				inQueryParams);		
		logger.info ("3@@@ assessmentRepository.findAll().size() filters: " + assessmentRepository.findAll().size());
				
		//filters are on:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(3, assessmentRepository.findAll().size());
		Assert.assertEquals(5, resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());

		logger.info ("2ENABLED***(filters ENABLED)Number of assessments in repository:" + assessmentRepository.findAll().size());
		logger.info ("2ENABLED***For query without filter, FILTERS ENABLED number of assessments:" + resultWithoutFilters.size());
		logger.info ("2ENABLED***For query WITH FILTERS, FILTERS ENABLED number of assessments:" + resultWithFilters.size());
		
		//Disabling filters:
		assessmentRepository.disableFilter("riskStatus");		
		
		//After riskStatus filter switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(4, assessmentRepository.findAll().size());
		Assert.assertEquals(5, resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());
		
		logger.info ("3ONE DISABLED***(after riskStatus (1) filter disabled)Number of assessments in repository:" + assessmentRepository.findAll().size());
		logger.info ("3ONE DISABLED***For query without filter, AFTER riskStatus (1) FILTERS DISABLED number of assessments:" + resultWithoutFilters.size());
		logger.info ("3ONE DISABLED***For query WITH FILTERS, AFTER riskStatus (1) FILTERS DISABLED number of assessments:" + resultWithFilters.size());

		assessmentRepository.disableFilter("dataValidity");
		
		//After dataValidity filter switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		Assert.assertEquals(5,resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());

		logger.info ("4TWO DISABLED***(after dataValidity (2) filter disabled)Number of assessments in repository:" + assessmentRepository.findAll().size());
		logger.info ("4TWO DISABLED***For query without filter, AFTER dataValidity (2)FILTERS DISABLED number of assessments:" + resultWithoutFilters.size());
		logger.info ("4TWO DISABLED***For query WITH FILTERS, AFTER dataValidity (2)FILTERS DISABLED number of assessments:" + resultWithFilters.size());
		
		assessmentRepository.disableFilter("userInRoleId");
		
		//After all filters switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		Assert.assertEquals(5,resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());
				
		logger.info ("5ALL DISABLED***(after all filters disabled)Number of assessments in repository:" + assessmentRepository.findAll().size());
		logger.info ("5ALL DISABLED***For query without filter, AFTER FILTERS DISABLED number of assessments:" + resultWithoutFilters.size());
		logger.info ("5ALL DISABLED***For query WITH FILTERS, AFTER FILTERS DISABLED number of assessments:" + resultWithFilters.size());
		
		//test sorting	
		
		List<Assessment> listA = resultWithoutFilters;		
		logger.info("listA(0).assessmentId : " + listA.get(0).getId());
		Assert.assertEquals(aa1.getId().intValue(), listA.get(0).getId ().intValue());
		
		listA.sort(Comparator.comparing(Assessment::getRoleId).reversed());
		logger.info("listA(0).assessmentId : " + listA.get(0).getId());
		Assert.assertEquals(aa2.getId().intValue(), listA.get(0).getId ().intValue());
		
		listA.sort(Comparator.comparing(Assessment::getRoleId));
		logger.info("listA(0).assessmentId : " + listA.get(0).getId());
		Assert.assertEquals(aa1.getId().intValue(), listA.get(0).getId ().intValue());

	}

}
