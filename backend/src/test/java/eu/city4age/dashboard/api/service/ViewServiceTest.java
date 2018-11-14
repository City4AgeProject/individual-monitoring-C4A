package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.service.impl.ViewServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class ViewServiceTest {
	
	static protected Logger logger = LogManager.getLogger(ViewServiceTest.class);
	
	@Autowired
	private ViewService viewService;

	@Test
	@Transactional
	@Rollback(true)
	public void createAllFiltersTest() {
		
		List<List<Filter>> allVariablesFilters = new ArrayList<List<Filter>>();
		Filter byDetectionVariables = new Filter();
		byDetectionVariables.setName("detectionVariable");
		byDetectionVariables.getInParams().put("detectionVariable", 501);
		Filter byDetectionVariables2 = new Filter();
		byDetectionVariables2.setName("detectionVariable");
		byDetectionVariables2.getInParams().put("detectionVariable", 514);
		allVariablesFilters.add(Arrays.asList(byDetectionVariables));
		allVariablesFilters.add(Arrays.asList(byDetectionVariables2));

		List<List<Filter>> allPilotsFilters = new ArrayList<List<Filter>>();
		Filter byPilotCodes = new Filter();
		byPilotCodes.setName("pilot");
		byPilotCodes.getInParams().put("pilot", "ath");
		Filter byPilotCodes2 = new Filter();
		byPilotCodes2.setName("pilot");
		byPilotCodes2.getInParams().put("pilot", "lcc");
		allPilotsFilters.add(Arrays.asList(byPilotCodes));
		allPilotsFilters.add(Arrays.asList(byPilotCodes2));
		
		List<List<Filter>> allCategoryFilters = new ArrayList<List<Filter>>();
		/*Filter filter = new Filter();
		filter.setName(type);
		filter.getInParams().put(type, category);
		allCategoryFilters.add(Arrays.asList(filter));*/
		
		List<List<Filter>> allTimesFilters = new ArrayList<List<Filter>>();
		Filter byIntervalStart = new Filter();
		byIntervalStart.setName("intervalStart");
		byIntervalStart.getInParams().put("intervalStart", new Timestamp((new Date()).getTime()));
		allTimesFilters.add(Arrays.asList(byIntervalStart));

		List<List<Filter>> result = viewService.createAllFilters(allVariablesFilters, allPilotsFilters, allCategoryFilters, allTimesFilters);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(4, result.size());
		
		Assert.assertEquals(3, result.get(0).size());
		Assert.assertEquals(3, result.get(1).size());
		Assert.assertEquals(3, result.get(2).size());
		Assert.assertEquals(3, result.get(3).size());
		
		for(Filter rt:result.get(0)) {
			logger.info("rt:" + rt.getName());
		}

		
	}

}
