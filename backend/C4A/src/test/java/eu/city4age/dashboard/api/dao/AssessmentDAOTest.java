package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.CdRole;
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
		
		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(1L);
		gefIds.add(7L);

		List<Assessment> result = assessmentDAO.getAssessmentsForSelectedDataSet(gefIds, null, null, null);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(6, result.size());
		
		
		Assert.assertEquals(Long.valueOf(184), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(185), result.get(1).getId());
		Assert.assertEquals(Long.valueOf(187), result.get(2).getId());
		Assert.assertEquals(Long.valueOf(191), result.get(3).getId());
		Assert.assertEquals(Long.valueOf(196), result.get(4).getId());
		Assert.assertEquals(Long.valueOf(190), result.get(5).getId());

		Assert.assertEquals(Long.valueOf(1), result.get(0).getGeriatricFactorValue().getId());
		Assert.assertEquals(Long.valueOf(1), result.get(1).getGeriatricFactorValue().getId());
		Assert.assertEquals(Long.valueOf(1), result.get(2).getGeriatricFactorValue().getId());
		Assert.assertEquals(Long.valueOf(1), result.get(3).getGeriatricFactorValue().getId());
		Assert.assertEquals(Long.valueOf(1), result.get(4).getGeriatricFactorValue().getId());
		Assert.assertEquals(Long.valueOf(7), result.get(5).getGeriatricFactorValue().getId());
		
		Assert.assertEquals(2, ((Set<CdRole>)result.get(0).getRoles()).size());
		Assert.assertEquals(2, ((Set<CdRole>)result.get(1).getRoles()).size());
		Assert.assertEquals(2, ((Set<CdRole>)result.get(2).getRoles()).size());
		Assert.assertEquals(2, ((Set<CdRole>)result.get(3).getRoles()).size());
		Assert.assertEquals(2, ((Set<CdRole>)result.get(4).getRoles()).size());
		Assert.assertEquals(1, ((Set<CdRole>)result.get(5).getRoles()).size());

	}	
	

}
