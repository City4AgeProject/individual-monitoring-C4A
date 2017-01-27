package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.json.view.View;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
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
		aa1.setId(184L);
		aa1.setUserInRole(uir1);
		aa1.setGeriatricFactorValue(gef1);
		aa1.getRoles().add(r1);
		assessmentRepository.save(aa1);

		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(184);
		assessedGefValuesRepository.save(ag1);

		AssessmentAudienceRole ar1 = new AssessmentAudienceRole();
		ar1.setAssessmentId(184);
		ar1.setUserInRoleId(1);
		ar1.setAssigned(new Timestamp(System.currentTimeMillis()));
		audienceRolesRepository.save(ar1);

		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(1L);
		gefIds.add(7L);

		List<Assessment> res1 = assessmentRepository.findAll();
		Assert.assertNotNull(res1);
		Assert.assertEquals(1, res1.size());
		Assert.assertEquals(Long.valueOf(1), res1.get(0).getGeriatricFactorValue().getId());

		List<GeriatricFactorValue> res2 = geriatricFactorRepository.findAll();
		Assert.assertNotNull(res2);
		Assert.assertEquals(1, res2.size());

		List<AssessedGefValueSet> res3 = assessedGefValuesRepository.findAll();
		Assert.assertNotNull(res3);
		Assert.assertEquals(1, res3.size());
		Assert.assertEquals(Long.valueOf(1), res3.get(0).getGefValueId());

		List<AssessmentAudienceRole> res4 = audienceRolesRepository.findAll();
		Assert.assertNotNull(res4);
		Assert.assertEquals(1, res4.size());
		Assert.assertEquals(1, res4.get(0).getUserInRoleId());

		// List<Assessment> result = null; //
		// assessmentRepository.findForSelectedDataSet(gefIds);
		// // ,
		// null,
		// null,
		// null);

		HashMap<String, Object> inFilterParams = new HashMap<String, Object>();
		inFilterParams.put("riskWarning", "W");
		inFilterParams.put("riskAlert", "A");
		inFilterParams.put("questionableData", "Q");
		inFilterParams.put("faultyData", "F");
		inFilterParams.put("userInRoleId", 1L);
		inFilterParams.put("orderByDateAsc", false);
		inFilterParams.put("orderByDateDesc", false);
		inFilterParams.put("orderByAuthorNameAsc", false);
		inFilterParams.put("orderByAuthorNameDesc", false);
		inFilterParams.put("orderByAuthorRoleAsc", false);
		inFilterParams.put("orderByAuthorRoleDesc", false);

		HashMap<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", gefIds);

		List<Assessment> list = assessmentRepository.doQueryWithFilter("filterByAll", "findForSelectedDataSet",
				inFilterParams, inQueryParams);

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		/*
		 * Assert.assertEquals(Long.valueOf(184), result.get(0).getId());
		 * Assert.assertEquals(Long.valueOf(185), result.get(1).getId());
		 * Assert.assertEquals(Long.valueOf(187), result.get(2).getId());
		 * Assert.assertEquals(Long.valueOf(191), result.get(3).getId());
		 * Assert.assertEquals(Long.valueOf(196), result.get(4).getId());
		 * Assert.assertEquals(Long.valueOf(190), result.get(5).getId());
		 */

		Assert.assertEquals(Long.valueOf(1), list.get(0).getGeriatricFactorValue().getId());
		/*
		 * Assert.assertEquals(Long.valueOf(1),
		 * result.get(1).getGeriatricFactorValue().getId());
		 * Assert.assertEquals(Long.valueOf(1),
		 * result.get(2).getGeriatricFactorValue().getId());
		 * Assert.assertEquals(Long.valueOf(1),
		 * result.get(3).getGeriatricFactorValue().getId());
		 * Assert.assertEquals(Long.valueOf(1),
		 * result.get(4).getGeriatricFactorValue().getId());
		 * Assert.assertEquals(Long.valueOf(7),
		 * result.get(5).getGeriatricFactorValue().getId());
		 */

		Assert.assertEquals(1, ((Set<Role>) list.get(0).getRoles()).size());

		ObjectMapper objectMapper = new ObjectMapper();

		Hibernate5Module hbm = new Hibernate5Module();

		objectMapper.registerModule(hbm);

		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		String result = objectMapper.writerWithView(View.AssessmentView.class).writeValueAsString(list);

		Assert.assertNotNull(result);

		Assert.assertEquals(97, result.length());

		Assert.assertEquals(
				"[{\"id\":184,\"userInRole\":{},\"assessmentComment\":null,\"riskStatus\":null,\"dataValidityStatus\":null}]",
				result);

		/*
		 * Assert.assertEquals(2, ((Set<CdRole>)
		 * result.get(1).getRoles()).size()); Assert.assertEquals(2,
		 * ((Set<CdRole>) result.get(2).getRoles()).size());
		 * Assert.assertEquals(2, ((Set<CdRole>)
		 * result.get(3).getRoles()).size()); Assert.assertEquals(2,
		 * ((Set<CdRole>) result.get(4).getRoles()).size());
		 * Assert.assertEquals(1, ((Set<CdRole>)
		 * result.get(5).getRoles()).size());
		 */
	}

	/*
	 * <geriatric_factor_value id="1" gef_value="5" user_in_role_id="1" />
	 * <geriatric_factor_value id="2" gef_value="3.9" user_in_role_id="1" />
	 * <geriatric_factor_value id="7" gef_value="3" user_in_role_id="1" />
	 * 
	 * <assessment id="184" author_id="1" /> <assessment id="185" author_id="2"
	 * /> <assessment id="187" author_id="1" /> <assessment id="191"
	 * author_id="1" />
	 * 
	 * <assessment id="196" author_id="2" />
	 * 
	 * <assessment id="190" author_id="2" />
	 * 
	 * <assessed_gef_value_set assessment_id="184" gef_value_id="1" />
	 * <assessed_gef_value_set assessment_id="185" gef_value_id="1" />
	 * <assessed_gef_value_set assessment_id="187" gef_value_id="1" />
	 * <assessed_gef_value_set assessment_id="191" gef_value_id="1" />
	 * <assessed_gef_value_set assessment_id="184" gef_value_id="2" />
	 * <assessed_gef_value_set assessment_id="185" gef_value_id="2" />
	 * <assessed_gef_value_set assessment_id="187" gef_value_id="2" />
	 * <assessed_gef_value_set assessment_id="191" gef_value_id="2" />
	 * 
	 * <assessed_gef_value_set assessment_id="196" gef_value_id="1" />
	 * <assessed_gef_value_set assessment_id="196" gef_value_id="2" />
	 * 
	 * <assessed_gef_value_set assessment_id="190" gef_value_id="7" />
	 * 
	 * <assessment_audience_role assessment_id="184" role_id="1" />
	 * <assessment_audience_role assessment_id="184" role_id="2" />
	 * <assessment_audience_role assessment_id="185" role_id="1" />
	 * <assessment_audience_role assessment_id="185" role_id="2" />
	 * <assessment_audience_role assessment_id="187" role_id="1" />
	 * <assessment_audience_role assessment_id="187" role_id="2" />
	 * <assessment_audience_role assessment_id="191" role_id="1" />
	 * <assessment_audience_role assessment_id="191" role_id="2" />
	 * 
	 * <assessment_audience_role assessment_id="196" role_id="1" />
	 * <assessment_audience_role assessment_id="196" role_id="2" />
	 * 
	 * <assessment_audience_role assessment_id="190" role_id="7" />
	 * 
	 * <cd_role id="1" /> <cd_role id="2" /> <cd_role id="7" />
	 * 
	 * <user_in_role id="1" user_in_system_id="1" /> <user_in_role id="2"
	 * user_in_system_id="2" /> <user_in_role id="7" user_in_system_id="3" />
	 * 
	 * <user_in_system id="1" /> <user_in_system id="2" /> <user_in_system
	 * id="3" />
	 */

}
