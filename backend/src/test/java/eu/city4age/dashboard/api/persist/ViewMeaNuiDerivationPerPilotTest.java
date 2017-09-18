package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
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
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;
import eu.city4age.dashboard.api.rest.AssessmentServiceTest; 

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ViewMeaNuiDerivationPerPilotTest {
	
	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);

	@Autowired
	private ViewMeaNuiDerivationPerPilotRepository viewMeaNuiDerivationPerPilotRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private PilotRepository pilotRepository;

	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDerivedDetectionVariableAndPilotCode() {
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode("ATH");
		userInRoleRepository.save(uir1);
		
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("ATH");
		pdv1.setDerivedDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv1);
		
		Long nuiId = dv2.getId();
		String pilotCode = "ATH";
		ViewMeaNuiDerivationPerPilot result = viewMeaNuiDerivationPerPilotRepository.findByDerivedDetectionVariableAndPilotCode(nuiId, pilotCode);
		
		Assert.assertNotNull(result);
		Assert.assertEquals("ATH", result.getPilotCode());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllNuiForMea() {
			
		UserInRole uir1 = new UserInRole();
		uir1.setId(11L);
		uir1.setRoleId(Short.valueOf("1"));
		uir1.setPilotCode("LCC");
		userInRoleRepository.save(uir1);

		UserInRole uir2 = new UserInRole();
		uir2.setId(22L);
		uir2.setRoleId(Short.valueOf("2"));
		uir2.setPilotCode("LCC");
		userInRoleRepository.save(uir2);

		UserInRole uir3 = new UserInRole();
		uir3.setId(33L);
		uir3.setRoleId(Short.valueOf("3"));
		uir3.setPilotCode("LCC");
		userInRoleRepository.save(uir3);
		
		UserInRole uir4 = new UserInRole();
		uir4.setId(44L);
		uir4.setRoleId(Short.valueOf("4"));
		uir4.setPilotCode("ATH");
		userInRoleRepository.save(uir4);

		UserInRole uir5 = new UserInRole();
		uir5.setId(55L);
		uir5.setRoleId(Short.valueOf("5"));
		uir5.setPilotCode("MPL");
		userInRoleRepository.save(uir5);
		
		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariableType dvt3 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt3);
		
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
		
		DetectionVariable dv3 = new DetectionVariable();
		dv3.setId(333L);
		dv3.setDetectionVariableName("Third");
		dv3.setDetectionVariableType(dvt3);
		detectionVariableRepository.save(dv3);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv1);
		pdv1.setPilotCode("LCC");
		pdv1.setDerivationWeight(BigDecimal.valueOf(0.5));
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1.setFormula("Formula1");
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv2);
		pdv2.setPilotCode("ATH");
		pdv2.setDerivationWeight(BigDecimal.valueOf(0.3));
		pdv2.setDerivedDetectionVariable(dv3);
		pdv2.setFormula("Formula2");
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setId(3L);
		pdv3.setDetectionVariable(dv1);
		pdv3.setPilotCode("MPL");
		pdv3.setDerivationWeight(BigDecimal.valueOf(0.5));
		pdv3.setDerivedDetectionVariable(dv3);
		pdv3.setFormula("Formula3");
		pilotDetectionVariableRepository.save(pdv3);
		
		
		List<ViewMeaNuiDerivationPerPilot> results = viewMeaNuiDerivationPerPilotRepository.findAllNuiForMea(111L, "ATH");

		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.size());

	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllDvNuisForMeasure() {
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("prefix_testtest");
		detectionVariableRepository.save(dv1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setPilotCode("LCC");
		pdv1.setDetectionVariable(dv1);
		pdv1.setFormula("prefix");
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setPilotCode("LCC");
		pdv2.setDetectionVariable(dv1);
		pdv2.setFormula("prefix");
		pilotDetectionVariableRepository.save(pdv2);
		
		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setId(3L);
		pdv3.setPilotCode("ATH");
		pdv3.setDetectionVariable(dv1);
		pdv3.setFormula("prefix");
		pilotDetectionVariableRepository.save(pdv3);

		List<ViewMeaNuiDerivationPerPilot> result = viewMeaNuiDerivationPerPilotRepository.findAllDvNuisForMeasure("testtest", "LCC");
		
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
	}


}

