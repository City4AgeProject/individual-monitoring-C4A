package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import eu.city4age.dashboard.api.pojo.dto.LastFiveAssessment;
import eu.city4age.dashboard.api.pojo.json.view.View;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class TimeIntervalRepositoryTest {

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
	public void testFindByPeriod() {

		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti1.setTypicalPeriod("1");
		timeIntervalRepository.save(ti1);

		TimeInterval ti2 = new TimeInterval();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti2.setTypicalPeriod("1");
		timeIntervalRepository.save(ti2);

		TimeInterval ti3 = new TimeInterval();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti3.setTypicalPeriod("1");
		timeIntervalRepository.save(ti3);

		TimeInterval ti4 = new TimeInterval();
		ti4.setId(4L);
		ti4.setIntervalStart(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti4.setTypicalPeriod("1");
		timeIntervalRepository.save(ti4);

		TimeInterval ti5 = new TimeInterval();
		ti5.setId(5L);
		ti5.setIntervalStart(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));
		ti5.setTypicalPeriod("1");
		timeIntervalRepository.save(ti5);

		Timestamp start = Timestamp.valueOf("2016-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2016-04-01 00:00:00");

		List<TimeInterval> result = timeIntervalRepository.findByPeriod(start, end);

		Assert.assertNotNull(result);

		Assert.assertEquals(3, result.size());

		Assert.assertEquals("2016-01-01 00:00:00.0", result.get(0).getStart());

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindByUserInRoleId() throws Exception {

		UserInSystem uis = new UserInSystem();
		uis.setId(1L);
		userInSystemRepository.save(uis);

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		uir.setUserInSystem(uis);
		userInRoleRepository.save(uir);

		DetectionVariableType dvt1 = new DetectionVariableType();
		dvt1.setDetectionVariableType("GES");
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("Walking");
		dv1.setDetectionVariableType(dvt1);
		dv1.setDerivedDetectionVariable(null);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("Climbing stairs");
		dv2.setDetectionVariableType(dvt1);
		dv2.setDerivedDetectionVariable(dv1);
		detectionVariableRepository.save(dv2);

		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti1.setTypicalPeriod("1");
		timeIntervalRepository.save(ti1);

		TimeInterval ti2 = new TimeInterval();
		ti2.setId(2L);
		ti2.setIntervalStart(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti2.setIntervalEnd(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti2.setTypicalPeriod("1");
		timeIntervalRepository.save(ti2);

		TimeInterval ti3 = new TimeInterval();
		ti3.setId(3L);
		ti3.setIntervalStart(Timestamp.valueOf("2016-03-01 00:00:00"));
		ti3.setIntervalEnd(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti3.setTypicalPeriod("1");
		timeIntervalRepository.save(ti3);

		TimeInterval ti4 = new TimeInterval();
		ti4.setId(4L);
		ti4.setIntervalStart(Timestamp.valueOf("2016-04-01 00:00:00"));
		ti4.setIntervalEnd(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti4.setTypicalPeriod("4");
		timeIntervalRepository.save(ti4);

		TimeInterval ti5 = new TimeInterval();
		ti5.setId(5L);
		ti5.setIntervalStart(Timestamp.valueOf("2016-05-01 00:00:00"));
		ti5.setIntervalEnd(Timestamp.valueOf("2016-06-01 00:00:00"));
		ti5.setTypicalPeriod("1");
		timeIntervalRepository.save(ti5);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setTimeInterval(ti1);
		gef1.setGefValue(new BigDecimal("5"));
		gef1.setCdDetectionVariable(dv2);
		gef1.setUserInRole(uir);
		geriatricFactorRepository.save(gef1);

		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setTimeInterval(ti2);
		gef2.setGefValue(new BigDecimal("3.9"));
		gef2.setCdDetectionVariable(dv2);
		gef2.setUserInRole(uir);
		geriatricFactorRepository.save(gef2);

		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setId(3L);
		gef3.setTimeInterval(ti3);
		gef3.setGefValue(new BigDecimal("3.3"));
		gef3.setCdDetectionVariable(dv2);
		gef3.setUserInRole(uir);
		geriatricFactorRepository.save(gef3);

		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setId(4L);
		gef4.setTimeInterval(ti4);
		gef4.setGefValue(new BigDecimal("2.5"));
		gef4.setCdDetectionVariable(dv2);
		gef4.setUserInRole(uir);
		geriatricFactorRepository.save(gef4);

		GeriatricFactorValue gef5 = new GeriatricFactorValue();
		gef5.setId(5L);
		gef5.setTimeInterval(ti5);
		gef5.setGefValue(new BigDecimal("3.9"));
		gef5.setCdDetectionVariable(dv2);
		gef5.setUserInRole(uir);
		geriatricFactorRepository.save(gef5);

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2018-01-01 00:00:00");

		List<Object[]> result = timeIntervalRepository.findByUserInRoleId(1L, 1L, start, end);

		Assert.assertNotNull(result);

		Assert.assertEquals(5, result.size());

		Assert.assertEquals("Climbing stairs",
				((GeriatricFactorValue) result.get(0)[1]).getGefTypeId().getDetectionVariableName());

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindLastFiveAssessmentsForDiagram() throws Exception {

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

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		detectionVariableRepository.save(dv1);

		UserInRole uir = new UserInRole();
		uir.setId(1L);
		userInRoleRepository.save(uir);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setTimeInterval(ti1);
		gef1.setCdDetectionVariable(dv1);
		gef1.setUserInRole(uir);
		geriatricFactorRepository.save(gef1);

		Assessment aa1 = new Assessment();
		aa1.setId(1L);
		aa1.setGeriatricFactorValue(gef1);
		aa1.setCreated(new Date());
		assessmentRepository.save(aa1);
		
		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		assessedGefValuesRepository.save(ag1);

		assessmentRepository.flush();

		// ti1.getGeriatricFactorValue().add(gef1);
		// timeIntervalRepository.save(ti1);

		List<Assessment> ass = assessmentRepository.findAll();
		Assert.assertNotNull(ass);
		Assert.assertEquals(1, ass.size());

		List<AssessedGefValueSet> agvs = assessedGefValuesRepository.findAll();
		Assert.assertNotNull(agvs);
		Assert.assertEquals(1, agvs.size());
		Assert.assertEquals(Long.valueOf(1), agvs.get(0).getGefValueId());

		List<GeriatricFactorValue> gefs = geriatricFactorRepository.findAll();
		Assert.assertNotNull(gefs);
		Assert.assertEquals(1, gefs.size());
		Assert.assertEquals(Long.valueOf(1), gefs.get(0).getId());
		Assert.assertEquals(Long.valueOf(1), gefs.get(0).getTimeInterval().getId());
		// Assert.assertEquals(Long.valueOf(1),
		// gefs.get(0).getTimeInterval().getGeriatricFactorValue().iterator().next().getId());
		// // !!!

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");

		List<LastFiveAssessment> list = timeIntervalRepository.findLastFiveForDiagram(1L, start, end);

		Assert.assertNotNull(list);

		Assert.assertEquals(7, list.size());
		Assert.assertEquals(Long.valueOf(1), list.get(0).getTimeIntervalId());
		// Assert.assertEquals(Long.valueOf(1),
		// list.get(0).getGeriatricFactorValue().iterator().next().getId()); //
		// !!!

		ObjectMapper objectMapper = new ObjectMapper();

		Hibernate5Module hbm = new Hibernate5Module();

		objectMapper.registerModule(hbm);

		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		String result = objectMapper.writerWithView(View.TimeIntervalView.class).writeValueAsString(list);

		Assert.assertNotNull(result);

		Assert.assertEquals(64, result.length());

		/*
		Assert.assertEquals(
				"[{\"id\":1,\"typicalPeriod\":\"MON\"},{\"id\":2,\"typicalPeriod\":\"MON\"},{\"id\":3,\"typicalPeriod\":\"MON\"},{\"id\":4,\"typicalPeriod\":\"MON\"},{\"id\":5,\"typicalPeriod\":\"MON\"},{\"id\":6,\"typicalPeriod\":\"MON\"},{\"id\":7,\"typicalPeriod\":\"MON\"}]",
				result);*/

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testGetGroups() throws Exception {

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
	}

}
