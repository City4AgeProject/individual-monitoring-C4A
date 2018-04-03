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
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class GeriatricFactorInterpolationValueRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(GeriatricFactorInterpolationValueRepositoryTest.class);
	
	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private MeasuresEndpoint measuresService;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Test
	@Transactional
	@Rollback(true)
	public void testFindAll() throws Exception {
		
		UserInRole uir1 = new UserInRole();
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1 = userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setPilotCode(Pilot.PilotCode.LCC);
		uir2 = userInRoleRepository.save(uir2);
		
		DetectionVariableType dvt = DetectionVariableType.GEF;
		dvt = detectionVariableTypeRepository.save(dvt);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("DV1");
		dv1 = detectionVariableRepository.save(dv1);
		
		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("DV2");
		dv2 = detectionVariableRepository.save(dv2);
		
		TimeInterval ti1 = measuresService
				.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		//ti1 = timeIntervalRepository.save(ti1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv2);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pdv2.setDetectionVariable(dv2);
		pdv2.setDerivedDetectionVariable(dv1);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);
		
		GeriatricFactorInterpolationValue gef1 = new GeriatricFactorInterpolationValue();
		gef1.setUserInRole(uir1);
		gef1.setUserInRoleId(uir1.getId());
		gef1.setDetectionVariable(dv1);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(new BigDecimal (1));
		gef1 = geriatricFactorInterpolationValueRepository.save(gef1);
		
		GeriatricFactorInterpolationValue gef2 = new GeriatricFactorInterpolationValue();
		gef2.setUserInRole(uir1);
		gef2.setUserInRoleId(uir1.getId());
		gef2.setDetectionVariable(dv1);
		gef2.setTimeInterval(ti1);
		gef2.setGefValue(new BigDecimal (2));
		gef2 = geriatricFactorInterpolationValueRepository.save(gef2);
		
		GeriatricFactorInterpolationValue gef3 = new GeriatricFactorInterpolationValue();
		gef3.setUserInRole(uir1);
		gef3.setUserInRoleId(uir1.getId());
		gef3.setDetectionVariable(dv1);
		gef3.setTimeInterval(ti1);
		gef3.setGefValue(new BigDecimal (3));
		gef3 = geriatricFactorInterpolationValueRepository.save(gef3);
		
		GeriatricFactorInterpolationValue gef4 = new GeriatricFactorInterpolationValue();
		gef4.setUserInRole(uir2);
		gef4.setUserInRoleId(uir2.getId());
		gef4.setDetectionVariable(dv1);
		gef4.setTimeInterval(ti1);
		gef4.setGefValue(new BigDecimal (4));
		gef4 = geriatricFactorInterpolationValueRepository.save(gef4);
		
		GeriatricFactorInterpolationValue gef5 = new GeriatricFactorInterpolationValue();
		gef5.setUserInRole(uir1);
		gef5.setUserInRoleId(uir1.getId());
		gef5.setDetectionVariable(dv2);
		gef5.setTimeInterval(ti1);
		gef5.setGefValue(new BigDecimal (5));
		gef5 = geriatricFactorInterpolationValueRepository.save(gef5);
		
		List<GeriatricFactorInterpolationValue> result = geriatricFactorInterpolationValueRepository.findAll();
		
		logger.info("result.size(): " + result.size());
		Assert.assertNotNull(result);
		Assert.assertEquals(5, result.size());

	}
	
}
