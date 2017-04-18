package eu.city4age.dashboard.api.persist;

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
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class GeriatricFactorRepositoryTest {
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testFindByDetectionVariableId() throws Exception {
		
		UserInRole userInRole = new UserInRole();
		userInRole.setId(1L);
		userInRoleRepository.save(userInRole);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDerivedDetectionVariable(dv2);
		detectionVariableRepository.save(dv1);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		timeIntervalRepository.save(ti1);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setUserInRole(userInRole);
		gef1.setCdDetectionVariable(dv1);
		gef1.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setUserInRole(userInRole);
		gef2.setCdDetectionVariable(dv1);
		gef2.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(3L);
		gef3.setUserInRole(userInRole);
		gef3.setCdDetectionVariable(dv1);
		gef3.setTimeInterval(ti1);
		geriatricFactorRepository.save(gef3);

		List<GeriatricFactorValue> result = geriatricFactorRepository.findByDetectionVariableId(2L, 1L);

		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());

	}

}