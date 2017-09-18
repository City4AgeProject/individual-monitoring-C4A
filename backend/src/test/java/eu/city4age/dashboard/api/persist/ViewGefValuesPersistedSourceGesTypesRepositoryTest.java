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
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;
import eu.city4age.dashboard.api.pojo.domain.ViewNuiValuesPersistedSourceMeaTypes;
import eu.city4age.dashboard.api.rest.AssessmentServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ViewGefValuesPersistedSourceGesTypesRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(ViewGefValuesPersistedSourceGesTypesRepositoryTest.class);
	
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
	private ViewGefValuesPersistedSourceGesTypesRepository viewGefValuesPersistedSourceGesTypesRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private NUIRepository nuiRepository;

	@Autowired
	private GeriatricFactorRepository gefRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllEntries() {
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);
	
		DetectionVariableType dvt1 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.GEF;
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
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariable(dv2);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(BigDecimal.valueOf(2.1));
		gefRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(1L);
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariable(dv2);
		gef2.setTimeInterval(ti1);
		gef2.setGefValue(BigDecimal.valueOf(3.1));
		gefRepository.save(gef2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);

		List<ViewGefValuesPersistedSourceGesTypes> result = viewGefValuesPersistedSourceGesTypesRepository.findAll();
		
		System.out.println("#######################################: " + result.get(0).getId().getDerivedDetectionVariableId());

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllForMonthByPilotCode() {

		String pilotCode = "LCC";
		Timestamp startOfMonth = Timestamp.valueOf("2016-08-01 00:00:00");
		String derivedType= "GEF";
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode(pilotCode);
		userInRoleRepository.save(uir1);
	
		DetectionVariableType dvt1 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.GEF;
		detectionVariableTypeRepository.save(dvt2);
	
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("ges");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("gef");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setTypicalPeriod("MON");
		ti1.setIntervalStart(startOfMonth);
		ti1.setIntervalEnd(Timestamp.valueOf("2016-09-01 00:00:00"));
		timeIntervalRepository.save(ti1);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariable(dv2);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(BigDecimal.valueOf(2.5));
		gefRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariable(dv2);
		gef2.setTimeInterval(ti1);
		gef2.setGefValue(BigDecimal.valueOf(4.0));
		gefRepository.save(gef2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);
		
		List<ViewGefValuesPersistedSourceGesTypes> result = viewGefValuesPersistedSourceGesTypesRepository.findAllForMonthByPilotCode(pilotCode, startOfMonth, derivedType);
		
		Assert.assertNotNull(result);
		
		Assert.assertEquals(2L, result.size());
		
	}
}
