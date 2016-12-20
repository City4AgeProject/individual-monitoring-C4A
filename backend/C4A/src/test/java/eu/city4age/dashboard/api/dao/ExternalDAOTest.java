package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.model.CareProfile;
import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.CdFrailtyStatus;
import eu.city4age.dashboard.api.model.CrProfile;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.FrailtyStatusTimelineId;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;
import eu.city4age.dashboard.api.model.UserInRole;
import eu.city4age.dashboard.api.model.UserInSystem;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class ExternalDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("externalDAO")
	private ExternalDAO externalDAO;

	@Test
	@DataSet({"ExternalDAOTest.xml"})
	public void testGetDetectionVariableForDetectionVariableType() throws Exception {
		
		List<String> parentFactors = Arrays.asList("OVL", "GFG");
		List<CdDetectionVariable> result = externalDAO.getDetectionVariableForDetectionVariableType(parentFactors);

		Assert.assertNotNull(result);
		
		Assert.assertEquals(4, result.size());		
		
	}
	
	@Test
	@DataSet({"ExternalDAOTest.xml"})
	public void testGetDetectionVariableForDetectionVariableTypee() throws Exception {
		
		String parentFactor = "GEF";
		List<CdDetectionVariable> result = externalDAO.getDetectionVariableForDetectionVariableType(parentFactor);

		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());		
		
	}
	
	@Test
	@DataSet({"ExternalDAOTest.xml"})
	public void testGetGeriatricFactorValueForDetectionVariableId() throws Exception {
		
		List<GeriatricFactorValue> result = externalDAO.getGeriatricFactorValueForDetectionVariableId(1L, 1L);

		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());		
		
	}
	
	@Test
	@DataSet({"ExternalDAOTest.xml"})
	public void testGetUserInSystemUsername() throws Exception {
		
		String result = externalDAO.getUserInSystemUsername(1L);

		Assert.assertNotNull(result);
		
		Assert.assertEquals("ivica", result);		
		
	}

	
	@Test
	@DataSet({"ExternalDAOTest.getFrailtyStatus.xml"})
	public void testGetFrailtyStatus() throws Exception {
		
		List<Long> timeintervalIds = Arrays.asList(1L, 2L, 3L);
		
		List<FrailtyStatusTimeline> result = externalDAO.getFrailtyStatus(timeintervalIds, 1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals("F", result.get(0).getCdFrailtyStatus().getFrailtyStatus());

	}
	
	@Test
	@DataSet({"ExternalDAOTest.login.xml"})
	public void testGetUserInSystem() throws Exception {
		UserInSystem result = externalDAO.getUserInSystem("admin", "secret");
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals("admin", result.getUsername());		
	}
	
	@Test
	@DataSet({"ExternalDAOTest.login.xml"})
	public void testGetUserInRoleByUserInSystemId() throws Exception {
		UserInRole result = externalDAO.getUserInRoleByUserInSystemId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(Long.valueOf(1), result.getId());
		
		Assert.assertEquals(Short.valueOf("8"), result.getRoleId());
	}
	
	@Test
	@DataSet({"ExternalDAOTest2.xml"})
	public void testGetUserInRoleByRoleId() throws Exception {
		List<UserInRole> result = externalDAO.getUserInRoleByRoleId(Short.valueOf("1"));
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(7, result.size());
		
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}
	
	@Test
	@DataSet({"ExternalDAOTest2.xml"})
	public void testGetProfileByUserInRoleId() throws Exception {
		List<CrProfile> result = externalDAO.getProfileByUserInRoleId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());		
	}
	
	@Test
	@DataSet({"ExternalDAOTest2.xml"})
	public void testGetCareProfileByUserInRoleId() throws Exception {
		List<CareProfile> result = externalDAO.getCareProfileByUserInRoleId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(1, result.size());	
	}
	
	@Test
	@DataSet({"ExternalDAOTest2.xml"})
	public void testGetFrailtyStatusByUserInRoleId() throws Exception {
		List<FrailtyStatusTimeline> result = externalDAO.getFrailtyStatusByUserInRoleId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(1, result.size());
		
		Assert.assertEquals("XXX", result.get(0).getCdFrailtyStatus().getFrailtyStatus());
	}
	
	@Test
	@DataSet({"ExternalDAOTest2.xml"})
	public void testGetUserInSystemUsernameByUserInRoleId() throws Exception {
		String result = externalDAO.getUserInSystemUsernameByUserInRoleId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals("admin", result);	
	}

}
