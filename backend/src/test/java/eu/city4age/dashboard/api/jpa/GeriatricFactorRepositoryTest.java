package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
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
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class GeriatricFactorRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(GeriatricFactorRepositoryTest.class);
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDetectionVariableId() throws Exception {
		
		UserInRole userInRole = new UserInRole();
		userInRole.setId(1L);
		userInRole.setPilotCode(Pilot.PilotCode.LCC);
		userInRole = userInRoleRepository.save(userInRole);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setId(2L);
		uir2.setPilotCode(Pilot.PilotCode.LCC);
		uir2 = userInRoleRepository.save(uir2);
		
		DetectionVariableType dvt = DetectionVariableType.GEF;
		dvt = detectionVariableTypeRepository.save(dvt);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("DV1");
		dv1 = detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("DV2");
		dv2 = detectionVariableRepository.save(dv2);
		
		TimeInterval ti1 = measuresService
				.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		//ti1 = timeIntervalRepository.save(ti1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setId(2L);
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pdv2.setDetectionVariable(dv2);
		pdv2.setDerivedDetectionVariable(dv1);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(userInRole);
		gef1.setDetectionVariable(dv1);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(new BigDecimal (1));
		gef1 = geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setUserInRole(userInRole);
		gef2.setDetectionVariable(dv1);
		gef2.setTimeInterval(ti1);
		gef2.setGefValue(new BigDecimal (2));
		gef2 = geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(3L);
		gef3.setUserInRole(userInRole);
		gef3.setDetectionVariable(dv1);
		gef3.setTimeInterval(ti1);
		gef3.setGefValue(new BigDecimal (3));
		gef3 = geriatricFactorRepository.save(gef3);
		
		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setId(4L);
		gef4.setUserInRole(uir2);
		gef4.setDetectionVariable(dv1);
		gef4.setTimeInterval(ti1);
		gef4.setGefValue(new BigDecimal (4));
		gef4 = geriatricFactorRepository.save(gef4);
		
		GeriatricFactorValue gef5 = new GeriatricFactorValue ();
		gef5.setId(5L);
		gef5.setUserInRole(userInRole);
		gef5.setDetectionVariable(dv2);
		gef5.setTimeInterval(ti1);
		gef5.setGefValue(new BigDecimal (5));
		gef5 = geriatricFactorRepository.save(gef5);
		
		List<GeriatricFactorValue> result = geriatricFactorRepository.findByDetectionVariableId(2L, 1L);
		
		logger.info("result.size(): " + result.size());
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		
		result = geriatricFactorRepository.findByDetectionVariableId(1L, 1L);
		
		logger.info("result.size(): " + result.size());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		result = geriatricFactorRepository.findByDetectionVariableId(2L, 2L);
		
		logger.info("result.size(): " + result.size());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		result = geriatricFactorRepository.findByDetectionVariableId(1L, 2L);
		
		logger.info("result.size(): " + result.size());
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());

	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveGEF() throws Exception {
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(91L);
		dv1.setDerivationWeight(new BigDecimal(1));
		detectionVariableRepository.save(dv1);

		UserInRole uir1 = new UserInRole();
		uir1.setId(13L);
		userInRoleRepository.save(uir1);
		
		GeriatricFactorValue ges = new GeriatricFactorValue();
		ges.setGefValue(new BigDecimal(.4));
		TimeInterval ti = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2017-07-01 00:00:00"), TypicalPeriod.MONTH);
		ges.setTimeInterval(ti);
		ges.setDetectionVariable(dv1);
		ges.setUserInRole(uir1);
		geriatricFactorRepository.save(ges);
		
		Assert.assertNotNull(ges);
		Assert.assertNotNull(ges.getTimeInterval());
		Assert.assertEquals(Timestamp.valueOf("2017-07-01 00:00:00"), ges.getTimeInterval().getIntervalStart());
	}

}