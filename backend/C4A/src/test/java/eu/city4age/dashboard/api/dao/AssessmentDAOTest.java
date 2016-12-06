package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class AssessmentDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("assessmentDAO")
	private AssessmentDAO assessmentDAO;

	@Test
	@DataSet({"AssessmentDAOTest.xml"})
	public void testGetDiagramDataForUserInRoleId() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");
		
		List<GeriatricFactorValue> result = assessmentDAO.getDiagramDataForUserInRoleId(1, start, end);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(6, result.size());
		
		Assert.assertEquals(Long.valueOf(1), ((GeriatricFactorValue)result.get(0)).getId());
		
		Assert.assertEquals("Walking", ((GeriatricFactorValue)result.get(0)).getCdDetectionVariable().getDetectionVariableName());
		
		Assert.assertEquals(Long.valueOf(1), ((GeriatricFactorValue)result.get(0)).getTimeInterval().getId());
		
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.xml"})
	public void testGetDiagramDataForUserInRoleId2() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");
		
		List<TimeInterval> result = assessmentDAO.getDiagramDataForUserInRoleId2(1, start, end);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(Long.valueOf(1), ((TimeInterval)result.get(0)).getId());
		
		Assert.assertEquals(0, ((TimeInterval)result.get(0)).getGeriatricFactorValues().size());
		
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.xml"})
	public void testGetDiagramDataForUserInRoleId3() throws Exception {

		List<TimeInterval> result = assessmentDAO.getDiagramDataForUserInRoleId(1);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(5, result.size());
		
		Assert.assertEquals(Long.valueOf(1), ((TimeInterval)result.get(0)).getId());
		
		Assert.assertEquals(0, ((TimeInterval)result.get(0)).getGeriatricFactorValues().size());
		
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.xml"})
	public void testGetAssessmentsForGeriatricFactorId() throws Exception {
		
		List<Assessment> result = assessmentDAO.getAssessmentsForGeriatricFactorId(Long.valueOf(1));
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(1, result.size());
	}

}
