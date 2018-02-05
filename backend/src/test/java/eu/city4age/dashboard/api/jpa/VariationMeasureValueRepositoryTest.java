package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class VariationMeasureValueRepositoryTest {
		
	static protected Logger logger = LogManager.getLogger(VariationMeasureValueRepositoryTest.class);

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
	public void testFindAllForMonthByPilotCodeNui() {
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
		uir1.setPilotCode(Pilot.PilotCode.LCC);
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
		
		Timestamp intervalStart = Timestamp.valueOf("2017-05-03 00:00:00");
		Timestamp intervalEnd = Timestamp.valueOf("2017-05-03 00:00:00");
		List<Pilot.PilotCode> pilots = Arrays.asList(Pilot.PilotCode.LCC);
		
		List result = variationMeasureValueRepository.findAllForMonthByPilotCodeNui(intervalStart, intervalEnd, pilots);
		Assert.assertNotNull(result);
		
		Assert.assertEquals(3, result.size());
	}

}
