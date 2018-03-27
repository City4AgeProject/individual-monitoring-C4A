package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

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
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewGefValuesPersistedSourceGesTypesRepository;
import eu.city4age.dashboard.api.jpa.ViewNuiValuesPersistedSourceMeaTypesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
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
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1 = userInRoleRepository.save(uir1);
	
		DetectionVariableType dvt1 = DetectionVariableType.GES;
		dvt1 = detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.GEF;
		dvt2 = detectionVariableTypeRepository.save(dvt2);
	
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(111L);
		dv1.setDetectionVariableName("First");
		dv1.setDetectionVariableType(dvt1);
		dv1 = detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(222L);
		dv2.setDetectionVariableName("Second");
		dv2.setDetectionVariableType(dvt2);
		dv2 = detectionVariableRepository.save(dv2);

		TimeInterval ti1 = new TimeInterval();
		ti1.setTypicalPeriod("MON");
		ti1.setIntervalStart(Timestamp.valueOf("2016-08-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-09-01 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariable(dv2);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(BigDecimal.valueOf(2.1));
		gef1 = gefRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(1L);
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariable(dv2);
		gef2.setTimeInterval(ti1);
		gef2.setGefValue(BigDecimal.valueOf(3.1));
		gef2 = gefRepository.save(gef2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv2);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);

		List<ViewGefValuesPersistedSourceGesTypes> result = viewGefValuesPersistedSourceGesTypesRepository.findAll();

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
	}

}
