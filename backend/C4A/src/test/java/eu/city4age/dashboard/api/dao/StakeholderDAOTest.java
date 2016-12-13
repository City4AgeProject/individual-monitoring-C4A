package eu.city4age.dashboard.api.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.model.Stakeholder;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class StakeholderDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("stakeholderDAO")
	private StakeholderDao stakeholderDAO;

	@Test
	@DataSet({"StakeholderDAOTest.xml"})
	public void test() throws Exception {
		
		List<Stakeholder> result = stakeholderDAO.getAllStockholders();
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());

	}
	

}
