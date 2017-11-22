package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FrailtyStatusTimelineRepositoryTest {

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private FrailtyStatusRepository frailtyStatusRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByPeriodAndUserId() throws Exception {

		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1 = userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2 = userInRoleRepository.save(uir2);

		FrailtyStatus fs = new FrailtyStatus();
		fs.setFrailtyStatus("F");
		fs.setFrailtyStatusDescription("Frail");
		fs = frailtyStatusRepository.save(fs);

		FrailtyStatusTimeline fst1 = new FrailtyStatusTimeline();
		fst1.setTimeIntervalId(1L);
		fst1.setUserInRoleId(uir1.getId());
		fst1.setFrailtyStatus("F");
		fst1.setChanged(new Date());
		fst1.setChangedBy(uir1);
		fst1 = frailtyStatusTimelineRepository.save(fst1);

		FrailtyStatusTimeline fst2 = new FrailtyStatusTimeline();
		fst2.setTimeIntervalId(2L);
		fst2.setUserInRoleId(uir2.getId());
		fst2.setFrailtyStatus("Q");
		fst2.setChanged(new Date());
		fst2.setChangedBy(uir2);
		fst2 = frailtyStatusTimelineRepository.save(fst2);

		FrailtyStatusTimeline fst3 = new FrailtyStatusTimeline();
		fst3.setTimeIntervalId(3L);
		fst3.setUserInRoleId(uir1.getId());
		fst3.setFrailtyStatus("W");
		fst3.setChanged(new Date());
		fst3.setChangedBy(uir1);
		fst3 = frailtyStatusTimelineRepository.save(fst3);

		List<TimeInterval> timeintervals = Arrays.asList(new TimeInterval(), new TimeInterval(), new TimeInterval());

		timeintervals.get(0).setId(1L);
		timeintervals.get(1).setId(2L);
		timeintervals.get(2).setId(3L);

		List<FrailtyStatusTimeline> result = frailtyStatusTimelineRepository.findByPeriodAndUserId(timeintervals, 1L);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("F", result.get(0).getFrailtyStatus());
		Assert.assertNotEquals("F", result.get(1).getFrailtyStatus());
		
		result = frailtyStatusTimelineRepository.findByPeriodAndUserId(timeintervals, 2L);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("Q", result.get(0).getFrailtyStatus());

	}

}