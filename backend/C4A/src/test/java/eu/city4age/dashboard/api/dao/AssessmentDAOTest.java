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
import eu.city4age.dashboard.api.dto.DiagramQuerryDTO;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class AssessmentDAOTest extends UnitilsJUnit4  {
	
	static protected Logger logger = Logger.getLogger(AssessmentDAOTest.class);
	
	@SpringBean("assessmentDAO")
	private AssessmentDAO assessmentDAO;

	@Test
	@DataSet({"AssessmentDAOTest.getDiagramDataForUserInRoleId.xml"})
	public void testGetDiagramDataForUserInRoleId() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");
		
		List<DiagramQuerryDTO> result = assessmentDAO.getDiagramDataForUserInRoleId(1, start, end);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(6, result.size());
		
		Assert.assertEquals(Long.valueOf(1), result.get(0).getGef().getId());
		
		Assert.assertEquals("Walking", result.get(0).getGef().getCdDetectionVariable().getDetectionVariableName());
		
		Assert.assertEquals(Long.valueOf(1), result.get(0).getGef().getTimeInterval().getId());
		
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.getDiagramDataForUserInRoleIdAndParentId.xml"})
	public void testGetDiagramDataForUserInRoleIdAndParentId() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2018-01-01 00:00:00");
		
		List<GeriatricFactorValue> result = assessmentDAO.getDiagramDataForUserInRoleId(1, Short.valueOf("1"), start, end);
		
		Assert.assertNotNull(result);

		Assert.assertEquals(30, result.size());
		
		Assert.assertEquals("Climbing stairs", result.get(0).getCdDetectionVariable().getDetectionVariableName());
		
	}

	@Test
	@DataSet({"AssessmentDAOTest.getLastFiveAssessments.xml"})
	public void testGetLastFiveAssessments() throws Exception {
	
		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");
		
		List<GeriatricFactorValue> result = assessmentDAO.getLastFiveAssessmentsForDiagram(1, start, end);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(2, result.get(0).getAssessedGefValueSets().size());
	}
	
	@Test
	@DataSet({"AssessmentDAOTest.getAssessmentsForGeriatricFactorId.xml"})
	public void testGetAssessmentsForGeriatricFactorId() throws Exception {
		
		List<Assessment> result = assessmentDAO.getAssessmentsForGeriatricFactorId(1L);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(2, result.size());
				
		Assert.assertEquals(0, ((Assessment)result.get(0)).getAssessmentAudienceRoles().size()); //!!!!
		
		//Assert.assertEquals(4, ((AssessmentAudienceRole)((Assessment)result.get(0)).getAssessmentAudienceRoles().iterator().next()).getAssessmentAudienceRoleId().getUserInRoleId());
		
		Assert.assertEquals(1, ((Assessment)result.get(0)).getAssessedGefValueSets().size());
	}

	@Test
	@DataSet({"AssessmentDAOTest.getAssessmentsByFilter.xml"})
	public void testGetAssessmentsByFilter() throws Exception {
		
		List<Boolean> status = new ArrayList<Boolean>();
		for(int i=0; i < 5; i++) {
			status.add(true);
		}
		
		Short roleId = Short.valueOf("1");
		List<Object[]> result = assessmentDAO.getAssessmentsByFilter(1L, status, roleId, OrderBy.AUTHOR_ROLE_ASC);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}	
	

}
