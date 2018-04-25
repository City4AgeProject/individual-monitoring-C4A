package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;
import eu.city4age.dashboard.api.service.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
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
	private UserInSystemRepository userInSystemRepository;

	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;
	
	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private MeasuresEndpoint measuresEndpoint;
	
	@Autowired
	private MeasuresService measuresService;


	@Test
	@Transactional
	@Rollback(true)
	public void testGetGroups() throws Exception {

		logger.info("start of testGetGroups");

		TypicalPeriod tp = new TypicalPeriod();
		tp.setTypicalPeriod("mon");
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
		userInSystemRepository.save(uis1);
		
		UserInSystem uis2 = new UserInSystem ();
		userInSystemRepository.save(uis2);
		
		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2.setUserInSystem(uis2);
		userInRoleRepository.save(uir2);
		
		DetectionVariableType dvt1 = DetectionVariableType.OVL;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GFG;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("DV1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("DV2");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setGefValue(new BigDecimal(3));
		gef1.setTimeInterval(ti1);
		gef1.setUserInRoleId(uir1.getId());
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariableId(dv1.getId());
		gef1.setDetectionVariable(dv1);
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setGefValue(new BigDecimal(4));
		gef2.setTimeInterval(ti2);
		gef2.setUserInRoleId(uir1.getId());
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariableId(dv1.getId());
		gef2.setDetectionVariable(dv1);
		geriatricFactorRepository.save(gef2);
		
		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setGefValue(new BigDecimal(5));
		gef3.setTimeInterval(ti3);
		gef3.setUserInRoleId(uir1.getId());
		gef3.setUserInRole(uir1);
		gef3.setDetectionVariableId(dv2.getId());
		gef3.setDetectionVariable(dv2);
		geriatricFactorRepository.save(gef3);
		
		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setGefValue(new BigDecimal(6));
		gef4.setTimeInterval(ti4);
		gef4.setUserInRoleId(uir2.getId());
		gef4.setUserInRole(uir2);
		gef4.setDetectionVariableId(dv1.getId());
		gef4.setDetectionVariable(dv1);
		geriatricFactorRepository.save(gef4);
		
		ti1.getGeriatricFactorValue().add(gef1);
		timeIntervalRepository.save(ti1);
		ti2.getGeriatricFactorValue().add(gef2);
		timeIntervalRepository.save(ti2);
		ti3.getGeriatricFactorValue().add(gef3);
		timeIntervalRepository.save(ti3);
		ti4.getGeriatricFactorValue().add(gef4);
		timeIntervalRepository.save(ti4);

		List<DetectionVariableType.Type> parentFactors = Arrays.asList(DetectionVariableType.Type.valueOf("OVL"), DetectionVariableType.Type.valueOf("GFG"));
		
		logger.info("parentFactorsSize: " + parentFactors.size());
		for (Iterator<DetectionVariableType.Type> i = parentFactors.iterator(); i.hasNext();) {
			logger.info("parentFactor: " + i.next().name());
		}

		List<TimeInterval> result = timeIntervalRepository.getGroups(uir1.getId(), parentFactors);
		
		logger.info("result: " + result);
		
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
				.findByIntervalStartAndTypicalPeriod(Timestamp.valueOf("2016-01-05 10:00:16"), "mon");

		Long returnedId = tif.getId();

		Assert.assertEquals(ti1.getId().longValue(), returnedId.longValue());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void setTimeIntervalNotExistsTest() {
		TimeInterval tif = timeIntervalRepository
				.findByIntervalStartAndTypicalPeriod(Timestamp.valueOf("2016-01-05 10:00:16"), "mon");
		
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-05 10:00:16"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		Long returnedId = ti1.getId();

		Assert.assertNotNull(returnedId.longValue());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetDiagramDataForUserInRoleIdAndParentId() throws Exception {
		
		String zone = "Europe/Athens";
		
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-04-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+3
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);	
		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-01-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+2
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-03-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+2
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-02-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+2
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-05-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+3
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
	
		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		p1.setTimeZone(zone);
		pilotRepository.save(p1);
		
		p1.getTimeZone();
		
		UserInSystem uis1 = new UserInSystem ();
		userInSystemRepository.save(uis1);

		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1.setPilot(p1);
		userInRoleRepository.save(uir1);
		
		DetectionVariableType dvt1 = DetectionVariableType.GEF;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("GEF1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("GES1");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);
		
		DetectionVariable dv3 = new DetectionVariable();
		dv3.setDetectionVariableName("GES2");
		dv3.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv3);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv2);
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setDetectionVariable(dv3);
		pdv2.setDerivedDetectionVariable(dv1);
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv2);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setTimeInterval(ti3);
		gef1.setUserInRoleId(uir1.getId());
		gef1.setUserInRole(uir1);
		gef1.setDetectionVariableId(dv2.getId());
		gef1.setDetectionVariable(dv2);
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setTimeInterval(ti5);
		gef2.setUserInRoleId(uir1.getId());
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariableId(dv3.getId());
		gef2.setDetectionVariable(dv3);
		geriatricFactorRepository.save(gef2);
		
		ti3.getGeriatricFactorValue().add(gef1);
		timeIntervalRepository.save(ti3);
		
		ti5.getGeriatricFactorValue().add(gef2);
		timeIntervalRepository.save(ti5);
		
		dv2.getPilotDetectionVariable().add(pdv1);
		detectionVariableRepository.save(dv2);
		
		dv3.getPilotDetectionVariable().add(pdv2);
		detectionVariableRepository.save(dv3);
		
		List<TimeInterval> result = timeIntervalRepository.getDiagramDataForUserInRoleId(uir1.getId(), dv1.getId());
		
		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(Date.from(LocalDate.parse("2016-03-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(0).getIntervalStart());
		Assert.assertEquals(Date.from(LocalDate.parse("2016-04-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(1).getIntervalStart());
		Assert.assertEquals(Date.from(LocalDate.parse("2016-05-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(2).getIntervalStart());
		
		Assert.assertEquals(1, result.get(0).getGeriatricFactorValue().size());
		Assert.assertEquals(0, result.get(1).getGeriatricFactorValue().size());
		Assert.assertEquals(1, result.get(2).getGeriatricFactorValue().size());
		
		Assert.assertEquals("GES1", result.get(0).getGeriatricFactorValue().iterator().next().getDetectionVariable().getDetectionVariableName());
		Assert.assertEquals("GES2", result.get(2).getGeriatricFactorValue().iterator().next().getDetectionVariable().getDetectionVariableName());
		
		Assert.assertEquals(Pilot.PilotCode.LCC, result.get(0).getGeriatricFactorValue().iterator().next().getDetectionVariable().getPilotDetectionVariable().iterator().next().getPilotCode());		
		Assert.assertEquals(Pilot.PilotCode.LCC, result.get(2).getGeriatricFactorValue().iterator().next().getDetectionVariable().getPilotDetectionVariable().iterator().next().getPilotCode());	
	
	}

}
