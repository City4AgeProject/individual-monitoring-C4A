package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("classpath:test-context-dao.xml")
public class TimeIntervalDAOTest extends UnitilsJUnit4  {
	
	@SpringBean("timeIntervalDAO")
	private TimeIntervalDAO timeIntervalDAO;

	@Test
	@DataSet({"TimeIntervalDAOTest.xml"})
	public void testGetTimeIntervalsForPeriod() throws Exception {
		
		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");
		
		List<Object[]> result = timeIntervalDAO.getTimeIntervalsForPeriod(start, end);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(Timestamp.valueOf("2016-01-01 00:00:00.0"), result.get(0)[0]);
	}
	

}
