package eu.city4age.dashboard.api.persist;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.rest.MeasuresServiceTest;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AssessmentRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(AssessmentRepositoryTest.class);
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

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

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository; 
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindForSelectedDataSet() throws Exception {
		
		System.out.println("1***Number of assessments in repository:"+assessmentRepository.findAll().size());
		//data for assessment and other joined tables:
		
		UserInSystem uis1 = new UserInSystem();
		uis1.setId(1L);
		userInSystemRepository.save(uis1);

		UserInSystem uis2 = new UserInSystem();
		uis2.setId(2L);
		userInSystemRepository.save(uis2);

		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setUserInSystem(uis1);
		userInRoleRepository.save(uir1);

		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2.setUserInSystem(uis2);
		userInRoleRepository.save(uir2);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setGefValue(new BigDecimal("5"));
		gef1.setUserInRole(uir1);
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setGefValue(new BigDecimal("6"));
		gef2.setUserInRole(uir2);
		geriatricFactorRepository.save(gef2);

		Role r1 = new Role();
		r1.setId(1L);
		roleRepository.save(r1);


		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		assessedGefValuesRepository.save(ag1);
		
		AssessedGefValueSet ag2 = new AssessedGefValueSet();
		ag2.setGefValueId(2);
		ag2.setAssessmentId(2);
		assessedGefValuesRepository.save(ag2);

		AssessmentAudienceRole ar1 = new AssessmentAudienceRole();
		ar1.setAssessmentId(1);
		ar1.setRoleId(1);
		ar1.setAssigned(new Timestamp(System.currentTimeMillis()));
		audienceRolesRepository.save(ar1);
		
		AssessmentAudienceRole ar2 = new AssessmentAudienceRole();
		ar2.setAssessmentId(2);
		ar2.setRoleId(2);
		ar2.setAssigned(new Timestamp(System.currentTimeMillis()));
		audienceRolesRepository.save(ar2);
		
		Role r2 = new Role();
		r2.setId(2L);
		roleRepository.save(r2);
			
		//data for second assessment and joined tables:


		Assessment aa1 = new Assessment();
		aa1.setId(1L);
		aa1.setUserInRole(uir1);
		aa1.setGeriatricFactorValue(gef1);
		aa1.getRoles().add(r1);
		aa1.setDataValidity('F');//F,Q
		aa1.setRiskStatus('W');//W,A
		assessmentRepository.save(aa1);
	
		Assessment aa2 = new Assessment();
		aa2.setId(2L);
		aa2.setUserInRole(uir2);//
		aa2.setGeriatricFactorValue(gef1);
		aa2.getRoles().add(r2);
		aa2.setDataValidity('F');//F,Q
		aa2.setRiskStatus('W');//W,A
		assessmentRepository.save(aa2);

		Assessment aa3 = new Assessment();
		aa3.setId(3L);
		aa3.setUserInRole(uir1);
		aa3.setGeriatricFactorValue(gef1);
		aa3.getRoles().add(r1);
		aa3.setDataValidity('Q');//F,Q
		aa3.setRiskStatus('W');//W,A
		assessmentRepository.save(aa3);
		
		Assessment aa4 = new Assessment();
		aa4.setId(4L);
		aa4.setUserInRole(uir1);
		aa4.setGeriatricFactorValue(gef1);
		aa4.getRoles().add(r1);
		aa4.setDataValidity('F');//F,Q
		aa4.setRiskStatus('A');//W,A
		assessmentRepository.save(aa4);
		
		Assessment aa5 = new Assessment();
		aa5.setId(5L);
		aa5.setUserInRole(uir1);
		aa5.setGeriatricFactorValue(gef2);
		aa5.getRoles().add(r1);
		aa5.setDataValidity('F');//F,Q
		aa5.setRiskStatus('W');//W,A
		assessmentRepository.save(aa5);
		
		Assessment aa6 = new Assessment();
		aa6.setGeriatricFactorValue(gef2);
		aa6.setId(6L);
		assessmentRepository.save(aa6);
		
		Assessment aa7 = new Assessment();
		assessmentRepository.save(aa7);
		
		System.out.println("2***Number of assessments in repository:"+assessmentRepository.findAll().size());
		

		
		
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
		System.out.println("1BEFORE***(BEFORE filters)Number of assessments in repository:"+assessmentRepository.findAll().size());
		
		List<Filter> noFilters = new ArrayList<Filter>();
		HashMap<String, Object> inQueryParams2 = new HashMap<String, Object>();
		System.out.println("1@@@ assessmentRepository.findAll().size(): "+assessmentRepository.findAll().size());
		List<Assessment> resultWithoutFilters = assessmentRepository.doQueryWithFilter(noFilters , "findForSelectedDataSet",
				inQueryParams);
		System.out.println("2@@@ assessmentRepository.findAll().size(): "+assessmentRepository.findAll().size());
		
		List<Assessment> resultWithFilters = assessmentRepository.doQueryWithFilter(filters, "findForSelectedDataSet",
				inQueryParams);
		
		System.out.println("3@@@ assessmentRepository.findAll().size(): "+assessmentRepository.findAll().size());
		//filters are on:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(3, assessmentRepository.findAll().size());
		Assert.assertEquals(5, resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());

		System.out.println("2ENABLED***(filters ENABLED)Number of assessments in repository:"+assessmentRepository.findAll().size());
		System.out.println("2ENABLED***For query without filter, FILTERS ENABLED number of assessments:"+resultWithoutFilters.size());
		System.out.println("2ENABLED***For query WITH FILTERS, FILTERS ENABLED number of assessments:"+resultWithFilters.size());
		
		//Disabling filters:
		assessmentRepository.disableFilter("riskStatus");
		
		
		//After riskStatus filter switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(4, assessmentRepository.findAll().size());
		Assert.assertEquals(5, resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());
		logger.info("end of testFindForSelectedDataSet");

		System.out.println("3ONE DISABLED***(after riskStatus (1) filter disabled)Number of assessments in repository:"+assessmentRepository.findAll().size());
		System.out.println("3ONE DISABLED***For query without filter, AFTER riskStatus (1) FILTERS DISABLED number of assessments:"+resultWithoutFilters.size());
		System.out.println("3ONE DISABLED***For query WITH FILTERS, AFTER riskStatus (1) FILTERS DISABLED number of assessments:"+resultWithFilters.size());
		


		assessmentRepository.disableFilter("dataValidity");
		
		//After dataValidity filter switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		Assert.assertEquals(5,resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());
		logger.info("end of testFindForSelectedDataSet");

		System.out.println("4TWO DISABLED***(after dataValidity (2) filter disabled)Number of assessments in repository:"+assessmentRepository.findAll().size());
		System.out.println("4TWO DISABLED***For query without filter, AFTER dataValidity (2)FILTERS DISABLED number of assessments:"+resultWithoutFilters.size());
		System.out.println("4TWO DISABLED***For query WITH FILTERS, AFTER dataValidity (2)FILTERS DISABLED number of assessments:"+resultWithFilters.size());
		

		
		assessmentRepository.disableFilter("userInRoleId");
		
		//After all filters switched off:
		Assert.assertNotNull(resultWithFilters);
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		Assert.assertEquals(5,resultWithoutFilters.size());
		Assert.assertEquals(2, resultWithFilters.size());
		logger.info("end of testFindForSelectedDataSet");
		
		System.out.println("5ALL DISABLED***(after all filters disabled)Number of assessments in repository:"+assessmentRepository.findAll().size());
		System.out.println("5ALL DISABLED***For query without filter, AFTER FILTERS DISABLED number of assessments:"+resultWithoutFilters.size());
		System.out.println("5ALL DISABLED***For query WITH FILTERS, AFTER FILTERS DISABLED number of assessments:"+resultWithFilters.size());
		
		
	

	}

}
