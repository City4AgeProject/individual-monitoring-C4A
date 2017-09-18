package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.dto.Last5Assessment;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.rest.MeasuresService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
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
	private UserInSystemRepository userInSystemRepository;

	@Autowired
	private DetectionVariableRepository detectionVariableRepository;

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
	public void testFindLastFiveAssessmentsForDiagram() throws Exception {

		logger.info("start of testFindLastFiveAssessmentsForDiagram");

		TypicalPeriod tp = new TypicalPeriod();
		tp.setTypicalPeriod("MON");
		tp.setPeriodDescription("Month");
		typicalPeriodRepository.save(tp);

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00")
				,eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
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

		TimeInterval ti6 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-06-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti6.setIntervalEnd(Timestamp.valueOf("2016-07-01 00:00:00"));

		TimeInterval ti7 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-07-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		ti7.setIntervalEnd(Timestamp.valueOf("2016-08-01 00:00:00"));

		DetectionVariable ddv1 = new DetectionVariable();
		ddv1.setId(4L);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDerivedDetectionVariable(ddv1);
		detectionVariableRepository.save(dv1);

		//
		UserInSystem uis = new UserInSystem();
		uis.setId(1L);
		userInSystemRepository.save(uis);
		//

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		uir.setUserInSystem(uis);
		userInRoleRepository.save(uir);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setTimeInterval(ti1);
		gef1.setDetectionVariable(dv1);
		gef1.setUserInRole(uir);

		Assessment aa1 = new Assessment();
		aa1.setGeriatricFactorValue(gef1);
		String inputString = "2017-05-22 12:00:00";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date inputDate = dateFormat.parse(inputString);
		aa1.setCreated(inputDate);
		aa1.setRiskStatus('A');
		aa1.setAssessmentComment("my comment");
		aa1.setId(1L);
		assessmentRepository.save(aa1);
		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		assessedGefValuesRepository.save(ag1);

		assessmentRepository.flush();

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");

		List<Last5Assessment> list = timeIntervalRepository.getLastFiveForDiagram(1L, 4L, start, end); // added
																										// 1L
																										// for
																										// parentDetectionVariableId

		Assert.assertNotNull(list);

		System.out.println(list.size() + "::WWW");
		Assert.assertEquals(7, list.size());

		Assert.assertEquals(Character.valueOf('A'), list.get(0).getRiskStatus());
		Assert.assertEquals("2016-01-01 00:00:00.0", list.get(0).getIntervalStart()); // ti

		Assert.assertEquals("2017-05-22 12:00:00", list.get(0).getDateAndTime());

		ObjectMapper objectMapper = new ObjectMapper();

		Hibernate5Module hbm = new Hibernate5Module();

		objectMapper.registerModule(hbm);

		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		String result = objectMapper.writerWithView(View.TimeIntervalView.class).writeValueAsString(list);

		Assert.assertNotNull(result);

		logger.info("end of testFindLastFiveAssessmentsForDiagram");

	}

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
		

		UserInSystem uis = new UserInSystem();
		uis.setId(1L);
		uis.setUsername("alfa");
		uis.setPassword("omega");
		userInSystemRepository.save(uis);

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		uir.setUserInSystem(uis);
		userInRoleRepository.save(uir);

		FrailtyStatusTimeline fst = new FrailtyStatusTimeline();
		fst.setTimeIntervalId(1L);
		fst.setUserInRoleId(1L);
		fst.setFrailtyStatus("test");
		fst.setChanged(new Date());
		fst.setChangedBy(uir);
		frailtyStatusTimelineRepository.save(fst);
		
		DetectionVariableType dvt1 = DetectionVariableType.GEF;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("DV1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("DV2");
		dv2.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv2);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setGefValue(new BigDecimal(3));
		gef1.setTimeInterval(ti1);
		gef1.setUserInRole(uir);
		gef1.setDetectionVariable(dv1);
		geriatricFactorRepository.save(gef1);

		List<DetectionVariableType.Type> parentFactors = Arrays.asList(DetectionVariableType.Type.valueOf("GEF"), DetectionVariableType.Type.valueOf("GES"));

		List<TimeInterval> result = timeIntervalRepository.getGroups(1L, parentFactors);

		Assert.assertNotNull(result);

		Assert.assertEquals(1, result.size());

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
