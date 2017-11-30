package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
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

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TimeIntervalRepositoryTest {

	static protected Logger logger = LogManager.getLogger(TimeIntervalRepositoryTest.class);

	@Autowired
	private TimeIntervalRepository timeIntervalRepository;

	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;

	@Autowired
	private MeasuresService measuresService;


	@Test
	@Transactional
	@Rollback(true)
	public void testGetGroups() throws Exception {

		logger.info("start of testGetGroups");

		TypicalPeriod tp = new TypicalPeriod();
		tp.setTypicalPeriod("MON");
		tp.setPeriodDescription("Month");
		typicalPeriodRepository.save(tp);
		
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
				
		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-02-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));
		
		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-03-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
				
		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
		
		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));
				
		UserInSystem uis1 = new UserInSystem ();
		uis1.setId(1L);
		UserInSystem uis2 = new UserInSystem ();
		uis2.setId(2L);
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setUserInSystem(uis1);
		uir1 = userInRoleRepository.save(uir1);
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2.setUserInSystem(uis2);
		uir2 = userInRoleRepository.save(uir2);

		FrailtyStatusTimeline fst1 = new FrailtyStatusTimeline();
		fst1.setTimeIntervalId(ti1.getId());
		fst1.setUserInRoleId(uir1.getId());
		fst1.setFrailtyStatus("test1");
		fst1.setChanged(new Date());
		fst1.setChangedBy(uir1);
		fst1 = frailtyStatusTimelineRepository.save(fst1);
		FrailtyStatusTimeline fst2 = new FrailtyStatusTimeline();
		fst2.setTimeIntervalId(ti2.getId());
		fst2.setUserInRoleId(uir2.getId());
		fst2.setFrailtyStatus("test2");
		fst2.setChanged(new Date());
		fst2.setChangedBy(uir2);
		fst2 = frailtyStatusTimelineRepository.save(fst2);
		
		//
		
		DetectionVariableType dvt1 = DetectionVariableType.GEF;
		dvt1 = detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GES;
		dvt2 = detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("DV1");
		dv1.setDetectionVariableType(dvt1);
		dv1 = detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("DV2");
		dv2.setDetectionVariableType(dvt2);
		dv2 = detectionVariableRepository.save(dv2);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setGefValue(new BigDecimal(3));
		gef1.setTimeInterval(ti1);
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariable(dv1);
		gef1 = geriatricFactorRepository.save(gef1);
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setGefValue(new BigDecimal(4));
		gef2.setTimeInterval(ti2);
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariable(dv1);
		gef2 = geriatricFactorRepository.save(gef2);
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(3L);
		gef3.setGefValue(new BigDecimal(5));
		gef3.setTimeInterval(ti3);
		gef3.setUserInRole(uir1);
		gef3.setDetectionVariable(dv2);
		gef3 = geriatricFactorRepository.save(gef3);
		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setId(4L);
		gef4.setGefValue(new BigDecimal(6));
		gef4.setTimeInterval(ti4);
		gef4.setUserInRole(uir2);
		gef4.setDetectionVariable(dv1);
		gef4 = geriatricFactorRepository.save(gef4);

		List<DetectionVariableType.Type> parentFactors = Arrays.asList(DetectionVariableType.Type.valueOf("GEF"), DetectionVariableType.Type.valueOf("GES"));
		
		logger.info("parentFactorsSize: " + parentFactors.size());
		for (Iterator<DetectionVariableType.Type> i = parentFactors.iterator(); i.hasNext();) {
			logger.info("parentFactor: " + i.next().name());
		}

		List<TimeInterval> result = timeIntervalRepository.getGroups(1L, parentFactors);
		
		for (TimeInterval ttii : result) {
			logger.info("time_interval_id: " + ttii.getId());
		}

		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());

		logger.info("end of testGetGroups");
	}

	@Test
	@Transactional
	@Rollback(true)
	public void setTimeIntervalExistsTest() {
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-05 10:00:16"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-05 10:00:16"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval tif = timeIntervalRepository
				.findByIntervalStartAndTypicalPeriod(Timestamp.valueOf("2016-01-05 10:00:16"), "MON");

		Long returnedId = tif.getId();

		Assert.assertEquals(ti1.getId().longValue(), returnedId.longValue());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void setTimeIntervalNotExistsTest() {
		TimeInterval tif = timeIntervalRepository
				.findByIntervalStartAndTypicalPeriod(Timestamp.valueOf("2016-01-05 10:00:16"), "MON");
		
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-05 10:00:16"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		Long returnedId = ti1.getId();

		Assert.assertNotNull(returnedId.longValue());
	}

}
