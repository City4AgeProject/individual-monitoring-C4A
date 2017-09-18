package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;
import eu.city4age.dashboard.api.pojo.domain.ViewNuiValuesPersistedSourceMeaTypes;
import eu.city4age.dashboard.api.rest.AssessmentServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ViewNuiValuesPersistedSourceMeaTypesRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private ViewNuiValuesPersistedSourceMeaTypesRepository viewNuiValuesPersistedSourceMeaTypesRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private NUIRepository nuiRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindNuiFor1Month() {
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);
	
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt2);
	
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(111L);
		dv1.setDetectionVariableName("First");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(222L);
		dv2.setDetectionVariableName("Second");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);

		TimeInterval ti1 = new TimeInterval();
		ti1.setTypicalPeriod("MON");
		ti1.setIntervalStart(Timestamp.valueOf("2016-08-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-09-01 00:00:00"));
		timeIntervalRepository.save(ti1);
		
		NumericIndicatorValue nui1 = new NumericIndicatorValue();
		nui1.setId(1L);
		nui1.setUserInRole(uir1);
		nui1.setDetectionVariable(dv2);
		nui1.setTimeInterval(ti1);
		nui1.setNuiValue(BigDecimal.valueOf(10.1));
		nuiRepository.save(nui1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);

		ViewNuiValuesPersistedSourceMeaTypes result = viewNuiValuesPersistedSourceMeaTypesRepository.findNuiFor1Month(pdv1.getPilotCode(), nui1.getTimeInterval().getIntervalStart(), pdv1.getDerivedDetectionVariable().getId(), nui1.getUserInRole().getId()).get(0);
		System.out.println("@@@@@@@@@@@@@@@@@@" + result);
		Assert.assertNotNull(result);
		Assert.assertEquals("LCC", result.getPilotCode());
		
	}

}
