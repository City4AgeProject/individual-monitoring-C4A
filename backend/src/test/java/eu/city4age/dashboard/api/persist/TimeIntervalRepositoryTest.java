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

	@Test
	@Transactional
	@Rollback(true)
	public void testFindLastFiveAssessmentsForDiagram() throws Exception {
		
		logger.info("start of testFindLastFiveAssessmentsForDiagram");

		TypicalPeriod tp = new TypicalPeriod();
		tp.setTypicalPeriod("MON");
		tp.setPeriodDescription("Month");
		typicalPeriodRepository.save(tp);

		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti1.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti1);

		TimeInterval ti2 = new TimeInterval();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti2.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti2);

		TimeInterval ti3 = new TimeInterval();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti3.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti3);

		TimeInterval ti4 = new TimeInterval();
		ti4.setId(4L);
		ti4.setIntervalStart(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti4.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti4);

		TimeInterval ti5 = new TimeInterval();
		ti5.setId(5L);
		ti5.setIntervalStart(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));
		ti5.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti5);

		TimeInterval ti6 = new TimeInterval();
		ti6.setId(6L);
		ti6.setIntervalStart(Timestamp.valueOf("2016-06-01 00:00:00"));
		ti6.setIntervalEnd(Timestamp.valueOf("2016-07-01 00:00:00"));
		ti6.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti6);

		TimeInterval ti7 = new TimeInterval();
		ti7.setId(7L);
		ti7.setIntervalStart(Timestamp.valueOf("2016-07-01 00:00:00"));
		ti7.setIntervalEnd(Timestamp.valueOf("2016-08-01 00:00:00"));
		ti7.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti7);

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
		gef1.setCdDetectionVariable(dv1);
		gef1.setUserInRole(uir);
		geriatricFactorRepository.save(gef1);


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
		
		geriatricFactorRepository.save(gef1);
		
		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		assessedGefValuesRepository.save(ag1);

		assessmentRepository.flush();

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");

		List<Last5Assessment> list = timeIntervalRepository.getLastFiveForDiagram(1L, 4L, start, end); //added 1L for parentDetectionVariableId

		Assert.assertNotNull(list);
		
		System.out.println(list.size()+"::WWW");
		Assert.assertEquals(7, list.size());
		Assert.assertEquals(Long.valueOf(1), list.get(0).getTimeIntervalId()); 

		Assert.assertEquals(Character.valueOf('A'), list.get(0).getRiskStatus());
		Assert.assertEquals("2016-01-01 00:00:00.0", list.get(0).getIntervalStart()); //ti
		
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

		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti1.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti1);

		TimeInterval ti2 = new TimeInterval();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti2.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti2);

		TimeInterval ti3 = new TimeInterval();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti3.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti3);

		TimeInterval ti4 = new TimeInterval();
		ti4.setId(4L);
		ti4.setIntervalStart(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti4.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti4);

		TimeInterval ti5 = new TimeInterval();
		ti5.setId(5L);
		ti5.setIntervalStart(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));
		ti5.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti5);

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

		DetectionVariableType dvt1 = new DetectionVariableType();
		dvt1.setDetectionVariableType("DT1");
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = new DetectionVariableType();
		dvt2.setDetectionVariableType("DT2");
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

		GeriatricFactorValue gef = new GeriatricFactorValue();
		gef.setId(1L);
		gef.setGefValue(new BigDecimal(3));
		gef.setTimeInterval(ti1);
		gef.setUserInRole(uir);
		gef.setCdDetectionVariable(dv1);
		geriatricFactorRepository.save(gef);

		List<String> parentFactors = Arrays.asList("DT1", "DT2");

		List<TimeInterval> result = timeIntervalRepository.getGroups(1L, parentFactors);

		Assert.assertNotNull(result);

		Assert.assertEquals(1, result.size());
		
		logger.info("end of testGetGroups");
	}

}
