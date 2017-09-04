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
import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class GeriatricFactorRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(GeriatricFactorRepositoryTest.class);
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
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
		userInRole.setPilotCode("LCC");
		userInRoleRepository.save(userInRole);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		detectionVariableRepository.save(dv2);
		
		TimeInterval ti1 = measuresService
				.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		timeIntervalRepository.save(ti1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setPilotCode("LCC");
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv2);
		pilotDetectionVariableRepository.save(pdv1);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(userInRole);
		gef1.setDetectionVariable(dv1);
		gef1.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setUserInRole(userInRole);
		gef2.setDetectionVariable(dv1);
		gef2.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(3L);
		gef3.setUserInRole(userInRole);
		gef3.setDetectionVariable(dv1);
		gef3.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef3);

		List<GeriatricFactorValue> result = geriatricFactorRepository.findByDetectionVariableId(2L, 1L);
		
		logger.info("result.size(): " + result.size());

		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());

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