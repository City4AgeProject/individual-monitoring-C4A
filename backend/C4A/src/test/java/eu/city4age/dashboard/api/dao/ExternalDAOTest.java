package eu.city4age.dashboard.api.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

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
	@DataSet({"ExternalDAOTest.xml"})
	public void testGetParentGroupName() throws Exception {
		
		String result = externalDAO.getParentGroupName(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals("GFG name", result);

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

}
