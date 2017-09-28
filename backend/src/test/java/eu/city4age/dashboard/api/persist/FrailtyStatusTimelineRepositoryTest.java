package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimelineId;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
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

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		userInRoleRepository.save(uir);

		FrailtyStatus fs = new FrailtyStatus();
		fs.setFrailtyStatus("F");
		fs.setFrailtyStatusDescription("Frail");
		frailtyStatusRepository.save(fs);

		FrailtyStatusTimeline fst1 = new FrailtyStatusTimeline();
		fst1.setTimeIntervalId(1L);
		fst1.setUserInRoleId(1L);
		fst1.setFrailtyStatus("F");
		fst1.setChanged(new Date());
		fst1.setChangedBy(uir);
		frailtyStatusTimelineRepository.save(fst1);

		FrailtyStatusTimeline fst2 = new FrailtyStatusTimeline();
		fst2.setTimeIntervalId(2L);
		fst2.setUserInRoleId(1L);
		fst2.setFrailtyStatus("F");
		fst2.setChanged(new Date());
		fst2.setChangedBy(uir);
		frailtyStatusTimelineRepository.save(fst2);

		FrailtyStatusTimeline fst3 = new FrailtyStatusTimeline();
		fst3.setTimeIntervalId(3L);
		fst3.setUserInRoleId(1L);
		fst3.setFrailtyStatus("F");
		fst3.setChanged(new Date());
		fst3.setChangedBy(uir);
		frailtyStatusTimelineRepository.save(fst3);

		List<TimeInterval> timeintervals = Arrays.asList(new TimeInterval(), new TimeInterval(), new TimeInterval());

		timeintervals.get(0).setId(1L);
		timeintervals.get(1).setId(2L);
		timeintervals.get(2).setId(3L);

		List<FrailtyStatusTimeline> result = frailtyStatusTimelineRepository.findByPeriodAndUserId(timeintervals, 1L);

		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());

		Assert.assertEquals("F", result.get(0).getFrailtyStatus());

	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void testFindLatest() throws Exception {
		
		Long uId = 1L;
		
		UserInRole uir = new UserInRole();
		uir.setId(uId);
		userInRoleRepository.save(uir);
		
		FrailtyStatus fs = new FrailtyStatus();
		fs.setFrailtyStatus("F");
		frailtyStatusRepository.save(fs);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setIntervalStart(Timestamp.valueOf("2016-04-01 00:00:00"));
		timeIntervalRepository.save(ti1);
		
		TimeInterval ti2 = new TimeInterval();
		ti2.setIntervalStart(Timestamp.valueOf("2016-05-01 00:00:00"));
		timeIntervalRepository.save(ti2);

		FrailtyStatusTimeline fst1 = new FrailtyStatusTimeline();
		fst1.setTimeIntervalId(ti1.getId());
		fst1.setUserInRoleId(uId);
		fst1.setFrailtyStatus("F");
		fst1.setChangedBy(uir);
		fst1.setChanged(new Date());
		fst1.setTimeInterval(ti1);
		fst1.setUserInRole(uir);
		frailtyStatusTimelineRepository.save(fst1);
		
		FrailtyStatusTimeline fst2 = new FrailtyStatusTimeline();
		fst2.setTimeIntervalId(ti2.getId());
		fst2.setUserInRoleId(uId);
		fst2.setFrailtyStatus("F");
		fst2.setChangedBy(uir);
		fst2.setChanged(new Date());
		fst2.setTimeInterval(ti2);
		fst2.setUserInRole(uir);
		frailtyStatusTimelineRepository.save(fst2);

		FrailtyStatusTimeline result = frailtyStatusTimelineRepository.findLatest(uId);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(Timestamp.valueOf("2016-04-01 00:00:00"), result.getTimeInterval().getIntervalStart());
		
	}

}