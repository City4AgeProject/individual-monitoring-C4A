package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;

import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class PilotRepositoryTest {

	static protected Logger logger = LogManager.getLogger(PilotRepositoryTest.class);

	@Autowired
	private PilotRepository pilotRepository;	
	
	@Test
	@Transactional
	@Rollback(true)
	public void findAllComputedTest () {
		
		YearMonth lastSubmitted = YearMonth.from(Timestamp.valueOf("2018-03-20 00:00:00").toLocalDateTime());
		YearMonth lastComputed = YearMonth.from(Timestamp.valueOf("2018-03-21 00:00:00").toLocalDateTime());
		YearMonth lastConfigured = YearMonth.from(Timestamp.valueOf("2018-02-20 00:00:00").toLocalDateTime());
		
		Pilot pilot = new Pilot();
	
		pilot.setLastSubmitted(lastSubmitted);
		pilot.setPilotCode(PilotCode.LCC);
		pilot.setLastComputed(lastComputed);
		pilot.setLastConfigured(lastConfigured);
		pilotRepository.save(pilot);
		
		List<Pilot> pilotList = pilotRepository.findAllComputed();
		Assert.assertEquals(0, pilotList.size());
		
		}
		
		
}
