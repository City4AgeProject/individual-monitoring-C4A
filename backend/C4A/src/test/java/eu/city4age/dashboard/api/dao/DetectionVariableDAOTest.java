package eu.city4age.dashboard.api.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class DetectionVariableDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("detectionVariableDAO")
	private DetectionVariableDAO detectionVariableDAO;

	@Test
	@DataSet({"DetectionVariableDAOTest.xml"})
	public void testGetDiagramDataForUserInRoleId() throws Exception {
		
		List<String> result = detectionVariableDAO.getAllDetectionVariableNamesForParentId(Short.valueOf("1"));
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(5, result.size());
		
	}
	

}
