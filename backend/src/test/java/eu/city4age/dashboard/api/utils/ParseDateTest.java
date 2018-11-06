package eu.city4age.dashboard.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
	
	@Test
	public void stringSplitting() {
		String url = "http://localhost:8080/C4A-dashboard/rest/view/graphData?pilotCode=ath bhx&detectionVariable=501 514&category=sex cohabiting education quality_housing&intervalStart=2018-01-01&intervalEnd=2018-10-31&comparison=true";
		
		//List<String> urlList = Arrays.asList(url.split("&"));
		
		String startDate = "";
		String endDate = "";
		
		int i = 0;
		for (String str : Arrays.asList(url.split("&"))) {
			if (str.contains("intervalStart")) startDate = str.split("=")[1];
			if (str.contains("intervalEnd")) endDate = str.split("=")[1];
		}
		
		LocalDate intervalStartLD = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate intervalEndLD = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		List<String> datesStringList = new ArrayList<String>();
		
		while (intervalStartLD.isBefore(intervalEndLD)) {
			String intervalStart = intervalStartLD.format(DateTimeFormatter.ofPattern("MMM yyyy"));
			datesStringList.add(intervalStart);
			intervalStartLD = intervalStartLD.plusMonths(1L);
		}
		
		for (String date : datesStringList) System.out.println(date);
		
		System.out.println("i: " + i);
		System.out.println("startDate: " + startDate);
		System.out.println("endDate: " + endDate);
		
	}

}
