package eu.city4age.dashboard.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.city4age.dashboard.api.ApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class LocalTimeTest {

	static protected Logger logger = LogManager.getLogger(LocalTimeTest.class);

	@Test
	public void TestTimeJunit() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
		Date date = sdf.parse("2008-08-07T18:08:59.697");

		int age = 0;

		logger.info("***DATE2: " + date);

		LocalDate birthDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		logger.info("***birthDate2: " + birthDate);

		age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());

		logger.info("***AGE2: " + age);

		String s = sdf.format(date);
		Assert.assertEquals("2008-08-07T18:08:59.697", s);
		Assert.assertNotEquals("2008-08-07T18:08:59.696", s);
		Assert.assertNotEquals("2008-08-07T18:08:59.698", s);

	}

}
