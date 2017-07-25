package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.rest.MeasuresService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.typeCompatibleWith;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class VariationMeasureValueRepositoryTest {

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

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByUserInRoleId() {
		
		Long uirId = 13L;

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2017-05-03 00:00:00"), TypicalPeriod.MONTH);

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
		uir1.setId(uirId);
		userInRoleRepository.save(uir1);
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
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
				.appendPattern("yyyy MMM").toFormatter(Locale.ENGLISH);
		
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));

		List<VariationMeasureValue> result = variationMeasureValueRepository.findByUserInRoleId(uirId, startOfMonth);

		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(Timestamp.valueOf("2017-05-03 00:00:00"), result.get(0).getTimeInterval().getIntervalStart());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByPilotCode() {
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		timeIntervalRepository.save(ti1);

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
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
				.appendPattern("yyyy MMM").toFormatter(Locale.ENGLISH);
		
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));

		List<Long> dvIds = Arrays.asList(91L, 95L, 98L);
		Long uId = 13L;
		List<VariationMeasureValue> result = variationMeasureValueRepository
				.findByPilotCode("LCC", startOfMonth, endOfMonth);

		Assert.assertEquals(3, result.size());
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
		
		Long result = variationMeasureValueRepository
				.findMinId(dv1, uir1);

		Assert.assertNotNull(result);
		Assert.assertNotNull(result);
		assertThat(result, greaterThan(0L));
		assertThat(result.getClass(), typeCompatibleWith(Long.class));
		Assert.assertEquals(1, result.longValue());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDoWeightedAvg() {
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm2);

		BigDecimal result = variationMeasureValueRepository.doWeightedAvg();

		Assert.assertNotNull(result);

		Assert.assertEquals((new BigDecimal(15)).setScale(1, RoundingMode.HALF_EVEN), result);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDoWeightedStDev() {
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm2);

		BigDecimal result = variationMeasureValueRepository.doWeightedStDev();

		Assert.assertNotNull(result);

		Assert.assertEquals((new BigDecimal(.47)).setScale(2, RoundingMode.HALF_EVEN),
				result.setScale(2, RoundingMode.HALF_EVEN));

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDoWeightedBest25Perc() {
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm2);

		BigDecimal result = variationMeasureValueRepository.doWeightedBest25Perc();

		Assert.assertNotNull(result);

		Assert.assertEquals((new BigDecimal(1.17)).setScale(2, RoundingMode.HALF_EVEN),
				result.setScale(2, RoundingMode.HALF_EVEN));

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDoWeightedDelta25PercAvg() {
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		variationMeasureValueRepository.save(vm2);

		BigDecimal result = variationMeasureValueRepository.doWeightedDelta25PercAvg();

		Assert.assertNotNull(result);

		Assert.assertEquals((new BigDecimal(.17)).setScale(2, RoundingMode.HALF_EVEN),
				result.setScale(2, RoundingMode.HALF_EVEN));

	}

	@Test
	@Transactional
	@Rollback(true)
	public void runFormula() {

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-03 00:00:00"), TypicalPeriod.DAY);


		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		userInRoleRepository.flush();

		VariationMeasureValue vm1 = new VariationMeasureValue();
		vm1.setId(1L);
		vm1.setMeasureValue(new BigDecimal(10));
		vm1.setDetectionVariable(dv1);
		vm1.setUserInRole(uir1);
		vm1.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm1);

		VariationMeasureValue vm2 = new VariationMeasureValue();
		vm2.setId(2L);
		vm2.setMeasureValue(new BigDecimal(20));
		vm2.setDetectionVariable(dv1);
		vm2.setUserInRole(uir1);
		vm2.setTimeInterval(ti1);
		variationMeasureValueRepository.save(vm2);

		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setFormula(
				"SELECT AVG(vm.measure_value*dv.derivation_weight) FROM variation_measure_value AS vm INNER JOIN cd_detection_variable AS dv ON dv.id = vm.measure_type_id INNER JOIN time_interval AS ti ON ti. ID = vm.time_interval_id WHERE vm.user_in_role_id = :uId AND vm.measure_type_id = :dvId AND ti.interval_start > :tiStart AND ti.interval_start < :tiEnd");
		;
		pdv1.setPilotCode("LCC");
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1.setDerivationWeight(new BigDecimal(1));
		pdv1.setValidFrom(new Date());
		pilotDetectionVariableRepository.save(pdv1);

		String formula = pilotDetectionVariableRepository.runFormula();

		Assert.assertEquals(
				"SELECT AVG(vm.measure_value*dv.derivation_weight) FROM variation_measure_value AS vm INNER JOIN cd_detection_variable AS dv ON dv.id = vm.measure_type_id INNER JOIN time_interval AS ti ON ti. ID = vm.time_interval_id WHERE vm.user_in_role_id = :uId AND vm.measure_type_id = :dvId AND ti.interval_start > :tiStart AND ti.interval_start < :tiEnd",
				formula);

		Query sql = variationMeasureValueRepository.getEntityManager().createNativeQuery(formula);
		variationMeasureValueRepository.getEntityManager().getEntityManagerFactory().addNamedQuery("dynamic", sql);
		Query namedQuery = variationMeasureValueRepository.getEntityManager().createNamedQuery("dynamic");
		namedQuery.setParameter("uId", 13L);
		namedQuery.setParameter("dvId", 91L);
		namedQuery.setParameter("tiStart", Timestamp.valueOf("2016-04-01 00:00:00"));
		namedQuery.setParameter("tiEnd", Timestamp.valueOf("2016-06-01 00:00:00"));

		Double result = (Double) namedQuery.getSingleResult();

		Assert.assertNotNull(result);
		Assert.assertEquals(new Double(15.0), result);
	
	}

}
