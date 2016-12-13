package eu.city4age.dashboard.api.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.model.CdRiskStatus;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class RiskStatusDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("riskStatusDAO")
	private RiskStatusDAO riskStatusDAO;

	@Test
	@DataSet({"RiskStatusDAOTest.xml"})
	public void test() throws Exception {
		
		List<CdRiskStatus> result = riskStatusDAO.getAllRiskStatus();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(2, result.size());
		
	}
	

}
