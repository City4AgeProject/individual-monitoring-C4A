package eu.city4age.dashboard.api.persist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.typeCompatibleWith;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.dto.Nuis;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class VariationMeasureValueRepositoryTest {
		
	static protected Logger logger = LogManager.getLogger(VariationMeasureValueRepositoryTest.class);
	
	private static DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy MMM")
			.toFormatter(Locale.ENGLISH);

	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;

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
						
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt1);
		detectionVariableTypeRepository.save(dvt2);
		
		//dv measures
		DetectionVariable mea1 = new DetectionVariable();
		mea1.setId(91L);
		mea1.setDetectionVariableName("MEA1");
		mea1.setDetectionVariableType(dvt1);
		mea1 = detectionVariableRepository.save(mea1);

		DetectionVariable mea2 = new DetectionVariable();
		mea2.setId(95L);
		mea2.setDetectionVariableName("MEA2");
		mea2.setDetectionVariableType(dvt1);
		mea2 = detectionVariableRepository.save(mea2);

		DetectionVariable mea3 = new DetectionVariable();
		mea3.setId(98L);
		mea3.setDetectionVariableName("MEA3");
		mea3.setDetectionVariableType(dvt1);
		mea3 = detectionVariableRepository.save(mea3);
		
		//ges
		DetectionVariable ges = new DetectionVariable();
		ges.setId(gesId);
		ges.setDetectionVariableName("GES");
		ges.setDetectionVariableType(dvt2);
		ges = detectionVariableRepository.save(ges);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1);
		
		//user
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		uir1 = userInRoleRepository.save(uir1);
		userInRoleRepository.flush();
		
				
		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setDetectionVariable(mea1);
		vm1.setUserInRole(uir1);
		vm1.setTimeInterval(ti1);
		vm1 = variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setDetectionVariable(mea2);
		vm2.setUserInRole(uir1);	
		vm2.setTimeInterval(ti1);
		vm2 = variationMeasureValueRepository.save(vm2);
		
		VariationMeasureValue vm3 = new VariationMeasureValue();
		vm3.setId(3L);
		vm3.setDetectionVariable(mea3);
		vm3.setUserInRole(uir1);
		vm3.setTimeInterval(ti1);
		vm3 = variationMeasureValueRepository.save(vm3);
		
		//pilot variables as measures
		PilotDetectionVariable pdvMea1 = new PilotDetectionVariable();
		pdvMea1.setId(10L);
		pdvMea1.setDetectionVariable(mea1);
		pdvMea1.setDerivedDetectionVariable(ges);
		pdvMea1 = pilotDetectionVariableRepository.save(pdvMea1);
		
		PilotDetectionVariable pdvMea2 = new PilotDetectionVariable();
		pdvMea2.setId(11L);
		pdvMea2.setDetectionVariable(mea2);
		pdvMea2.setDerivedDetectionVariable(ges);
		pdvMea2 = pilotDetectionVariableRepository.save(pdvMea2);
		
		PilotDetectionVariable pdvMea3 = new PilotDetectionVariable();
		pdvMea3.setId(12L);
		pdvMea3.setDetectionVariable(mea3);
		pdvMea3.setDerivedDetectionVariable(ges);
		pdvMea3 = pilotDetectionVariableRepository.save(pdvMea3);
				
		
		List<VariationMeasureValue> result = variationMeasureValueRepository.findByUserAndGes(10L, 514L );
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
		
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
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllNui1Value() {
		
		
		Long uirId = 1L;
		Long dvId = 1L;
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));
		
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		userInRoleRepository.save(uir1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		detectionVariableRepository.save(dv1);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1); 
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setUserInRole(uir1);
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		vmv1.setMeasureValue(new BigDecimal(5));
		variationMeasureValueRepository.save(vmv1);
	
		List<Nuis> result = variationMeasureValueRepository.doAllNuis(startOfMonth, endOfMonth);
		
		Assert.assertNotNull(result);
		
		/*Assert.assertEquals(new BigDecimal(5.0), result.getAvg());
		Assert.assertEquals(new BigDecimal(.0), result.getStDev());
		Assert.assertEquals(new BigDecimal(.0), result.getStd());
		Assert.assertEquals(new BigDecimal(5.0), result.getBest25());
		Assert.assertEquals(new BigDecimal(1.0), result.getBest());
		Assert.assertEquals(new BigDecimal(0.0), result.getDelta());*/
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllNui2Values() {
		
		
		Long uirId = 1L;
		Long dvId = 1L;
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));
		
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		userInRoleRepository.save(uir1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		detectionVariableRepository.save(dv1);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1); 
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setUserInRole(uir1);
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		vmv1.setMeasureValue(new BigDecimal(5));
		variationMeasureValueRepository.save(vmv1);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(2L);
		vmv2.setUserInRole(uir1);
		vmv2.setDetectionVariable(dv1);
		vmv2.setTimeInterval(ti1);
		vmv2.setMeasureValue(new BigDecimal(4));
		variationMeasureValueRepository.save(vmv2);

		List<Nuis> result = variationMeasureValueRepository.doAllNuis(startOfMonth, endOfMonth);
		
		Assert.assertNotNull(result);
		
		/*Assert.assertEquals(new BigDecimal(4.5), result.getAvg());
		Assert.assertEquals(new BigDecimal(.70710678118654757).setScale(2, RoundingMode.HALF_UP), result.getStDev().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(.15713484026367724).setScale(2, RoundingMode.HALF_UP), result.getStd().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(4.75), result.getBest25());
		Assert.assertEquals(new BigDecimal(1.0555555555555556).setScale(2, RoundingMode.HALF_UP), result.getBest().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(0.05555555555555555).setScale(2, RoundingMode.HALF_UP), result.getDelta().setScale(2, RoundingMode.HALF_UP));*/
		
	}

}
