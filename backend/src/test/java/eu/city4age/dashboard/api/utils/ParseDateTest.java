package eu.city4age.dashboard.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import eu.city4age.dashboard.api.ApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class ParseDateTest {
	
	@Test
	public void ParseDateTest() throws ParseException  {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		String string = "Tue Aug 01 22:04:50 GMT+00:00 2017";
		
		Date date = sdf.parse(string);
		
		Assert.notNull(date);
		
	}

}
