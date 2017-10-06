package eu.city4age.dashboard.api.persist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.typeCompatibleWith;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

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
import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class VariationMeasureValueRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(VariationMeasureValueRepositoryTest.class);

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private MeasuresService measuresService;
	
	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;

	
	//new test
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByUserAndGes() {

		Long uirId = 10L;
		Long gesId = 514L;
						
		
		//dv measures
		DetectionVariable mea1 = new DetectionVariable();
		mea1.setId(91L);
		mea1.setDetectionVariableType(DetectionVariableType.MEA);
		detectionVariableRepository.save(mea1);

		DetectionVariable mea2 = new DetectionVariable();
		mea2.setId(95L);
		mea2.setDetectionVariableType(DetectionVariableType.MEA);
		detectionVariableRepository.save(mea2);

		DetectionVariable mea3 = new DetectionVariable();
		mea3.setId(98L);
		mea3.setDetectionVariableType(DetectionVariableType.MEA);
		detectionVariableRepository.save(mea3);
		
		//ges
		DetectionVariable ges = new DetectionVariable();
		ges.setId(gesId);
		ges.setDetectionVariableType(DetectionVariableType.GES);
		detectionVariableRepository.save(ges);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		timeIntervalRepository.save(ti1);
		
		//user
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();
		
				
		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setDetectionVariable(mea1);
		vm1.setUserInRole(uir1);
		vm1.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setDetectionVariable(mea2);
		vm2.setUserInRole(uir1);	
		vm2.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm2);
		
		VariationMeasureValue vm3 = new VariationMeasureValue();
		vm3.setId(3L);
		vm3.setDetectionVariable(mea3);
		vm3.setUserInRole(uir1);
		vm3.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm3);
		
		//pilot variables as measures
		PilotDetectionVariable pdvMea1 = new PilotDetectionVariable();
		pdvMea1.setId(10L);
		pdvMea1.setDetectionVariable(mea1);
		pdvMea1.setDerivedDetectionVariable(ges);
		pilotDetectionVariableRepository.save(pdvMea1);
		
		PilotDetectionVariable pdvMea2 = new PilotDetectionVariable();
		pdvMea2.setId(11L);
		pdvMea2.setDetectionVariable(mea2);
		pdvMea2.setDerivedDetectionVariable(ges);
		pilotDetectionVariableRepository.save(pdvMea2);
		
		PilotDetectionVariable pdvMea3 = new PilotDetectionVariable();
		pdvMea3.setId(12L);
		pdvMea3.setDetectionVariable(mea3);
		pdvMea3.setDerivedDetectionVariable(ges);
		pilotDetectionVariableRepository.save(pdvMea3);
				
		
		List<VariationMeasureValue> result = variationMeasureValueRepository.findByUserAndGes(10L, 514L );
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindMMByUserInRoleId () {
		
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp1 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp1.setTypicalPeriod("1YR");		
		typicalPeriodRepository.save(tp1);
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp2 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp2.setTypicalPeriod("MON");
		typicalPeriodRepository.save(tp2);
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp3 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp3.setTypicalPeriod("DAY");
		typicalPeriodRepository.save(tp3);
		
		UserInRole uir1 = new UserInRole ();
		uir1.setId(1L);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setId(2L);
		userInRoleRepository.save(uir2);
		
		DetectionVariable dv1 = new DetectionVariable ();
		dv1.setId(1L);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable ();
		dv2.setId(2L);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable ();
		dv3.setId(3L);
		detectionVariableRepository.save(dv3);
		
		TimeInterval ti1 = new TimeInterval ();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2015-01-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti1.setTypicalPeriod(tp1.getTypicalPeriod());
		ti1 = timeIntervalRepository.save(ti1);
		//ti1.setId(timeIntervalRepository.);
				
		TimeInterval ti2 = new TimeInterval ();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2015-04-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti2 = timeIntervalRepository.save(ti2);
		
		TimeInterval ti3 = new TimeInterval ();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2015-02-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf("2015-03-01 00:00:00"));
		ti3.setTypicalPeriod(tp2.getTypicalPeriod());
		ti3 = timeIntervalRepository.save(ti3);
		
		logger.info("timeIntervalCount: " + timeIntervalRepository.count());
		
		for (TimeInterval i : timeIntervalRepository.findAll()) {
			logger.info("TimeIntervalId: " + i.getId());
		}
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setUserInRole(uir1);
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vmv1);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(2L);
		vmv2.setUserInRole(uir1);
		vmv2.setDetectionVariable(dv1);
		vmv2.setTimeInterval(ti2);
		variationMeasureValueRepository.save(vmv2);
		
		VariationMeasureValue vmv3 = new VariationMeasureValue();
		vmv3.setId(3L);
		vmv3.setUserInRole(uir1);
		vmv3.setDetectionVariable(dv1);
		vmv3.setTimeInterval(ti3);
		variationMeasureValueRepository.save(vmv3);
		
		/*test findByUserInRoleId */
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy MMM")
				.toFormatter(Locale.ENGLISH);
		Timestamp start = Timestamp.valueOf(YearMonth.parse("2015 JAN", formatter).atDay(1).atStartOfDay());
		Timestamp end = Timestamp.valueOf(YearMonth.parse("2016 JAN", formatter).atDay(1).atStartOfDay());
		List<VariationMeasureValue> result = variationMeasureValueRepository.findByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		/*for (VariationMeasureValue res : variationMeasureValueRepository.findByUserInRoleId(uir1.getId(), dv1.getId(), start, end)) {
			logger.info(" " + res.get);
		} */
		
		for (VariationMeasureValue res: variationMeasureValueRepository.findAll()) {
			logger.info("vmvID: " + res.getId());
			logger.info("vmvUiRID: " + res.getUserInRole().getId());
			logger.info("vmvDvID: " + res.getDetectionVariable().getId());
			logger.info("vmvTimeIntervalID:  " + res.getTimeInterval().getId());
		}
		Assert.assertNotNull(result);
		Assert.assertEquals (3, result.size());
		
		end = Timestamp.valueOf(YearMonth.parse("2015 MAR", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (2, result.size());
		
		end = Timestamp.valueOf(YearMonth.parse("2015 JAN", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (1, result.size());
		
		start = Timestamp.valueOf(YearMonth.parse("2016 MAR", formatter).atDay(1).atStartOfDay());
		end = Timestamp.valueOf(YearMonth.parse("2016 MAR", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (0, result.size());
		
		/* test findByUserMMInRoleId */
		
		start = Timestamp.valueOf(YearMonth.parse("2015 JAN", formatter).atDay(1).atStartOfDay());;
		end = Timestamp.valueOf(YearMonth.parse("2016 JAN", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findMMByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (2, result.size());
		
		end = Timestamp.valueOf(YearMonth.parse("2015 MAR", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findMMByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (1, result.size());
		
		end = Timestamp.valueOf(YearMonth.parse("2015 JAN", formatter).atDay(1).atStartOfDay());;
		result = variationMeasureValueRepository.findMMByUserInRoleId(uir1.getId(), dv1.getId(), start, end);
		
		Assert.assertNotNull(result);
		Assert.assertEquals (0, result.size());
		
	}
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllForMonthByPilotCodeNui() {
		
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp1 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp1.setTypicalPeriod("1YR");		
		typicalPeriodRepository.save(tp1);
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp2 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp2.setTypicalPeriod("MON");
		typicalPeriodRepository.save(tp2);
		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp3 = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
		tp3.setTypicalPeriod("DAY");
		typicalPeriodRepository.save(tp3);
		
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1);
		
		TimeInterval ti2 = new TimeInterval ();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf ("2017-01-01 00:00:00"));
		ti2 = timeIntervalRepository.save(ti2); 
		
		TimeInterval ti3 = new TimeInterval ();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf ("2016-02-01 00:00:00"));
		ti3.setTypicalPeriod(tp2.getTypicalPeriod());	
		ti3 = timeIntervalRepository.save(ti3);
		
		TimeInterval ti4 = new TimeInterval ();
		ti4.setId(4L);
		ti4.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti4.setIntervalEnd(Timestamp.valueOf ("2016-01-02 00:00:00"));
		ti4.setTypicalPeriod(tp3.getTypicalPeriod());	
		ti4 = timeIntervalRepository.save(ti4);
		
		TimeInterval ti5 = new TimeInterval ();
		ti5.setId(5L);
		ti5.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti5.setIntervalEnd(Timestamp.valueOf ("2017-01-01 00:00:00"));
		ti5.setTypicalPeriod(tp1.getTypicalPeriod());
		ti5 = timeIntervalRepository.save(ti5);
		
		timeIntervalRepository.flush();

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(95L);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setId(98L);
		detectionVariableRepository.save(dv3);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2.setId(23L);
		uir2.setPilotCode("ATH");
		userInRoleRepository.save(uir2);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		vm1.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setDetectionVariable(dv2);
		vm2.setUserInRole(uir1);
		vm2.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm2);

		VariationMeasureValue vm3 = new VariationMeasureValue();
		vm3.setId(3L);
		vm3.setDetectionVariable(dv3);
		vm3.setUserInRole(uir1);
		vm3.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm3);
		
		VariationMeasureValue vm4 = new VariationMeasureValue();
		vm4.setId(4L);
		vm4.setDetectionVariable(dv1);
		vm4.setUserInRole(uir2);
		vm4.setTimeInterval(ti2);
		variationMeasureValueRepository.save(vm4);
		
		VariationMeasureValue vm5 = new VariationMeasureValue();
		vm5.setId(5L);
		vm5.setDetectionVariable(dv1);
		vm5.setUserInRole(uir2);
		vm5.setTimeInterval(ti3);
		variationMeasureValueRepository.save(vm5);
		
		VariationMeasureValue vm6 = new VariationMeasureValue();
		vm6.setId(6L);
		vm6.setDetectionVariable(dv1);
		vm6.setUserInRole(uir2);
		vm6.setTimeInterval(ti4);
		variationMeasureValueRepository.save(vm6);
		
		VariationMeasureValue vm7 = new VariationMeasureValue();
		vm7.setId(7L);
		vm7.setDetectionVariable(dv1);
		vm7.setUserInRole(uir2);
		vm7.setTimeInterval(ti5);
		variationMeasureValueRepository.save(vm7);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy MMM")
				.toFormatter(Locale.ENGLISH);

		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp
				.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));

		List<Long> dvIds = Arrays.asList(91L, 95L, 98L);
		Long uId = 13L;
		List<VariationMeasureValue> result = variationMeasureValueRepository.findAllForMonthByPilotCodeNui("LCC",
				startOfMonth, endOfMonth);

		Assert.assertEquals(3, result.size());
		
		/* findAllForMonthByPilotCode TEST */
		
		startOfMonth = Timestamp.valueOf(YearMonth.parse("2016 JAN", formatter).atDay(1).atStartOfDay());
		endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 JAN", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findAllForMonthByPilotCode ("ATH", startOfMonth, endOfMonth);
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		
		startOfMonth = Timestamp.valueOf(YearMonth.parse("2016 JAN", formatter).atDay(3).atStartOfDay());
		endOfMonth = Timestamp.valueOf(YearMonth.parse("2016 FEB", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findAllForMonthByPilotCode ("ATH", startOfMonth, endOfMonth);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
		startOfMonth = Timestamp.valueOf(YearMonth.parse("2016 MAR", formatter).atDay(1).atStartOfDay());
		endOfMonth = Timestamp.valueOf(YearMonth.parse("2016 MAY", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findAllForMonthByPilotCode ("ATH", startOfMonth, endOfMonth);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAR", formatter).atDay(1).atStartOfDay());
		endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		result = variationMeasureValueRepository.findAllForMonthByPilotCode ("ATH", startOfMonth, endOfMonth);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindMinId() {

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm2);

		VariationMeasureValue vm3 = new VariationMeasureValue();
		vm3.setId(3L);
		vm3.setDetectionVariable(dv1);
		vm3.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm3);

		Long result = variationMeasureValueRepository.findMinId(dv1, uir1.getId());

		Assert.assertNotNull(result);
		Assert.assertNotNull(result);
		assertThat(result, greaterThan(0L));
		assertThat(result.getClass(), typeCompatibleWith(Long.class));
		Assert.assertEquals(1, result.longValue());
	}	

	@Test
	@Transactional
	@Rollback(true)
	public void testFilterListByDetectionVariable() {

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(95L);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setId(97L);
		detectionVariableRepository.save(dv3);

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv2);
		variationMeasureValueRepository.save(vm2);

		List<VariationMeasureValue> testList = new ArrayList<VariationMeasureValue>();

		testList.add(vm1);
		testList.add(vm1);
		testList.add(vm2);

		List<VariationMeasureValue> result1 = measuresService.filterListByDetectionVariable(testList, dv1);
		List<VariationMeasureValue> result2 = measuresService.filterListByDetectionVariable(testList, dv2);
		List<VariationMeasureValue> result3 = measuresService.filterListByDetectionVariable(testList, dv3);
		
		Assert.assertEquals(testList.subList(0, 2), result1);
		Assert.assertEquals(testList.subList(2, 3), result2);
		Assert.assertEquals(result3.size(), 0);
	}

}
