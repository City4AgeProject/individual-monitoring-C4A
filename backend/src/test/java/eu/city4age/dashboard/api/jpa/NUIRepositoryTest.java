package eu.city4age.dashboard.api.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.typeCompatibleWith;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class NUIRepositoryTest {

	static protected Logger logger = LogManager.getLogger(NUIRepositoryTest.class);
	
	private static DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy MMM")
			.toFormatter(Locale.ENGLISH);
	
	@Autowired
	NUIRepository nuiRepository;

	@Autowired
	UserInRoleRepository userInRoleRepository;

	@Autowired
	DetectionVariableRepository detectionVariableRepository;

	@Autowired
	DetectionVariableTypeRepository detectionVariableTypeRepository;

	@Autowired
	PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private MeasuresService measuresService;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetNuisForSelectedGes() {
		Long uirId = 1L;
		Long dvId = 1L;

		Timestamp intervalStart1 = Timestamp.valueOf(YearMonth.of(2017, 1).atDay(1).atStartOfDay());

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(intervalStart1, TypicalPeriod.MONTH);

		Timestamp intervalStart2 = Timestamp.valueOf(YearMonth.of(2017, 2).atDay(1).atStartOfDay());

		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(intervalStart2, TypicalPeriod.MONTH);

		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1 = userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setId(2L);
		uir2.setPilotCode(Pilot.PilotCode.LCC);
		uir2 = userInRoleRepository.save(uir2);
		
		UserInRole uir3 = new UserInRole ();
		uir3.setId(3L);
		uir3.setPilotCode(Pilot.PilotCode.ATH);
		uir3 = userInRoleRepository.save(uir3);

		DetectionVariableType dvt1 = DetectionVariableType.GES;
		dvt1 = detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		dvt2 = detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		dv1.setDetectionVariableName("Ges");
		dv1.setDetectionVariableType(dvt1);
		dv1 = detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("Nui");
		dv2.setDetectionVariableType(dvt2);
		dv2 = detectionVariableRepository.save(dv2);

		NumericIndicatorValue nui1 = new NumericIndicatorValue();
		nui1.setId(1L);
		nui1.setNuiValue(new Double(0));
		nui1.setDetectionVariable(dv2);
		nui1.setUserInRole(uir1);
		nui1.setTimeInterval(ti1);
		nui1 = nuiRepository.save(nui1);

		NumericIndicatorValue nui2 = new NumericIndicatorValue();
		nui2.setId(2L);
		nui2.setNuiValue(new Double(1));
		nui2.setDetectionVariable(dv2);
		nui2.setUserInRole(uir1);
		nui2.setTimeInterval(ti2);
		nui2 = nuiRepository.save(nui2);
		
		NumericIndicatorValue nui3 = new NumericIndicatorValue();
		nui3.setId(3L);
		nui3.setNuiValue(new Double(2));
		nui3.setDetectionVariable(dv2);
		nui3.setUserInRole(uir2);
		nui3.setTimeInterval(ti1);
		nui3 = nuiRepository.save(nui3);
		
		NumericIndicatorValue nui4 = new NumericIndicatorValue();
		nui4.setId(4L);
		nui4.setNuiValue(new Double(3));
		nui4.setDetectionVariable(dv2);
		nui4.setUserInRole(uir3);
		nui4.setTimeInterval(ti2);
		nui4 = nuiRepository.save(nui4);
		
		NumericIndicatorValue nui5 = new NumericIndicatorValue();
		nui5.setId(5L);
		nui5.setNuiValue(new Double(4));
		nui5.setDetectionVariable(dv1);
		nui5.setUserInRole(uir1);
		nui5.setTimeInterval(ti2);
		nui5 = nuiRepository.save(nui5);
				
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv2);
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv1);
		pdv2.setDerivedDetectionVariable(dv2);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);

		List<NumericIndicatorValue> result = nuiRepository.getNuisForSelectedGes(uirId, dvId);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
		result = nuiRepository.getNuisForSelectedGes(2L, dvId);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		result = nuiRepository.getNuisForSelectedGes(uirId, 2L);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

}
