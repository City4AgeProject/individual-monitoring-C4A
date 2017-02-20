package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@SpringBootTest(classes = ApplicationTest.class)
@ActiveProfiles("test")
public class AssessmentRepositoryTest {

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

	@Test
	@Transactional
	@Rollback(true)
	public void testFindForSelectedDataSet() throws Exception {

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
		gef1.setId(1L);
		gef1.setGefValue(new BigDecimal("5"));
		gef1.setUserInRole(uir1);
		geriatricFactorRepository.save(gef1);

		Role r1 = new Role();
		r1.setId(1L);
		roleRepository.save(r1);

		Assessment aa1 = new Assessment();
		aa1.setId(1L);
		aa1.setUserInRole(uir1);
		aa1.setGeriatricFactorValue(gef1);
		aa1.getRoles().add(r1);
		aa1.setDataValidity('Q');
		aa1.setRiskStatus('A');
		assessmentRepository.save(aa1);

		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		assessedGefValuesRepository.save(ag1);

		AssessmentAudienceRole ar1 = new AssessmentAudienceRole();
		ar1.setAssessmentId(1);
		ar1.setUserInRoleId(1);
		ar1.setAssigned(new Timestamp(System.currentTimeMillis()));
		audienceRolesRepository.save(ar1);

		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(1L);
		gefIds.add(7L);

		/*
		 * inFilterParams.put("riskStatusWarning", false);
		 * inFilterParams.put("riskStatusAlert", false);
		 * inFilterParams.put("dataValidityQuestionable", false);
		 * inFilterParams.put("dataValidityFaulty", true);
		 * inFilterParams.put("assessmentComment", false);
		 * inFilterParams.put("userInRoleId", 1L);
		 * inFilterParams.put("orderByDateAsc", false);
		 * inFilterParams.put("orderByDateDesc", false);
		 * inFilterParams.put("orderByAuthorNameAsc", false);
		 * inFilterParams.put("orderByAuthorNameDesc", false);
		 * inFilterParams.put("orderByAuthorRoleAsc", false);
		 * inFilterParams.put("orderByAuthorRoleDesc", false);
		 */

		/*
		List<eu.city4age.dashboard.api.pojo.persist.Filter> filters = new ArrayList<eu.city4age.dashboard.api.pojo.persist.Filter>();

		eu.city4age.dashboard.api.pojo.persist.Filter riskStatus = new eu.city4age.dashboard.api.pojo.persist.Filter();
		riskStatus.setName("riskStatus");

		riskStatus.getInParams().put("riskStatus", 'W');

		eu.city4age.dashboard.api.pojo.persist.Filter dataValidity = new eu.city4age.dashboard.api.pojo.persist.Filter();
		dataValidity.setName("dataValidity");

		dataValidity.getInParams().put("dataValidity", 'F');

		filters.add(riskStatus);
		filters.add(dataValidity);*/
		
		
		List<Filter> filters = new ArrayList<Filter>();

		Filter riskStatus = new Filter();
		riskStatus.setName("riskStatus");
		List<Character> inParams = new ArrayList<Character>();

		inParams.add('W');
		
		riskStatus.getInParams().put("riskStatus", inParams);
		filters.add(riskStatus);


		Filter dataValidity = new Filter();
		dataValidity.setName("dataValidity");
		inParams = new ArrayList<Character>();

		inParams.add('F');

		dataValidity.getInParams().put("dataValidity", inParams);
		filters.add(dataValidity);
		
		Filter orderByDateDesc = new Filter();
		orderByDateDesc.setName("orderByDateDesc");
		filters.add(orderByDateDesc);		

		HashMap<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", gefIds);

		/*
		 * filter.setParameter("dataValidityFaulty", true);
		 * filter.setParameter("riskStatusAlert", false);
		 * filter.setParameter("riskStatusWarning", false);
		 * filter.setParameter("dataValidityQuestionable", false);
		 * filter.setParameter("assessmentComment", false);
		 * filter.setParameter("orderByDateAsc", false);
		 * filter.setParameter("orderByDateDesc", false);
		 * filter.setParameter("orderByAuthorNameAsc", false);
		 * filter.setParameter("orderByAuthorNameDesc", false);
		 * filter.setParameter("orderByAuthorRoleAsc", false);
		 * filter.setParameter("orderByAuthorRoleDesc", false);
		 */

		List<Assessment> list = assessmentRepository.doQueryWithFilter(filters, "findForSelectedDataSet",
				inQueryParams);

		Assert.assertNotNull(list);
		Assert.assertEquals(0, list.size());

	}

}
