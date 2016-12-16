package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class AssessmentDAOTest extends UnitilsJUnit4  {
	
	static protected Logger logger = Logger.getLogger(AssessmentDAOTest.class);
	
	@SpringBean("assessmentDAO")
	private AssessmentDAO assessmentDAO;

	
	@Test
	@DataSet({"AssessmentDAOTest.getDiagramDataForUserInRoleIdAndParentId.xml"})
	public void testGetDiagramDataForUserInRoleIdAndParentId() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2018-01-01 00:00:00");
		
		List<TimeInterval> result = assessmentDAO.getDiagramDataForUserInRoleId(1L, 1L, start, end);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(5, result.size());
		
		Assert.assertEquals("Walking", result.get(0).getGeriatricFactorValues().iterator().next().getCdDetectionVariable().getDetectionVariableName());
		
	}

	@Test
	@DataSet({"AssessmentDAOTest.getLastFiveAssessments.xml"})
	public void testGetLastFiveAssessments() throws Exception {
	
		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");
		
		List<TimeInterval> result = assessmentDAO.getLastFiveAssessmentsForDiagram(1, start, end);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(7, result.size());
		
		

		
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.getAssessmentsForSelectedDataSet.xml"})
	public void testGetAssessmentsByFilter() throws Exception {
		
		List<Boolean> status = new ArrayList<Boolean>(5);
		status.add(true);
		status.add(false);
		status.add(true);
		status.add(false);
		status.add(true);
		
		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(1L);
		gefIds.add(2L);
		gefIds.add(3L);

		Short roleId = Short.valueOf("1");
		List<GeriatricFactorValue> result = assessmentDAO.getAssessmentsForSelectedDataSet(gefIds, status, roleId, OrderBy.AUTHOR_ROLE_ASC);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}	
	

}
