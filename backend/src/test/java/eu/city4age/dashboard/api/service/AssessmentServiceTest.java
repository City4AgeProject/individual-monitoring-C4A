package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.AudienceRolesRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepositoryTest;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.dto.oj.DataSet;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.rest.AssessmentsService;
import eu.city4age.dashboard.api.rest.MeasuresService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AssessmentServiceTest {

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
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AudienceRolesRepository audienceRolesRepository;
	
    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }
    
	@Spy
	@InjectMocks
	AssessmentsService assessmentService;
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	@Transactional
	@Rollback(false)
	public void getLast5ForDiagramTest() throws Exception {
		
		/*
		 * Test scenario
		 * 2 users in role
		 * 7 time intervals
		 * 2 geriatric factor values
		 * 2 assessments
		 */

		logger.info("start of testFindLastFiveAssessmentsForDiagram");

		eu.city4age.dashboard.api.pojo.domain.TypicalPeriod tp = new eu.city4age.dashboard.api.pojo.domain.TypicalPeriod();
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
		ddv1 = detectionVariableRepository.save(ddv1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDerivedDetectionVariable(ddv1);		
		dv1 = detectionVariableRepository.save(dv1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable ();
		pdv1.setId(1L);
		pdv1.setDerivedDetectionVariable(ddv1);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(ddv1);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setId(2L);
		pdv2.setDerivedDetectionVariable(ddv1);
		pdv2.setPilotCode(Pilot.PilotCode.ATH);
		pdv2.setDetectionVariable(dv1);
		pdv2.setDerivedDetectionVariable(ddv1);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);
		
		UserInSystem uis = new UserInSystem ();
		uis.setId(1L);
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1.setUserInSystem(uis);
		uir1 = userInRoleRepository.save(uir1);
		UserInRole uir2 = new UserInRole();
		uir2.setId(2L);
		uir2.setPilotCode(Pilot.PilotCode.ATH);
		uir2.setUserInSystem(uis);
		uir2 = userInRoleRepository.save(uir2);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setId(1L);
		gef1.setTimeInterval(ti1);
		gef1.setDetectionVariable(dv1);
		gef1.setUserInRole(uir1);
		gef1.setGefValue(new BigDecimal (1));
		gef1 = geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setId(2L);
		gef2.setTimeInterval(ti2);
		gef2.setDetectionVariable(dv1);
		gef2.setUserInRole(uir2);
		gef2.setGefValue(new BigDecimal (2));
		gef2 = geriatricFactorRepository.save(gef2);

		Assessment aa1 = new Assessment();
		aa1.setGeriatricFactorValue(gef1);
		String inputString = "2017-05-22 12:00:00";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date inputDate = dateFormat.parse(inputString);
		aa1.setCreated(inputDate);
		aa1.setRiskStatus('A');
		aa1.setAssessmentComment("my comment");
		aa1.setUserInRole(uir1);
		aa1.setId(1L);
		aa1 = assessmentRepository.save(aa1);
		
		AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(gef1.getId().intValue());
		ag1.setAssessmentId(aa1.getId().intValue());
		ag1 = assessedGefValuesRepository.save(ag1);
		
		Assessment aa2 = new Assessment();
		aa2.setGeriatricFactorValue(gef2);
		aa2.setCreated(inputDate);
		aa2.setRiskStatus('A');
		aa2.setAssessmentComment("my comment2");
		aa2.setUserInRole(uir2);
		aa2.setId(2L);
		aa2 = assessmentRepository.save(aa2);
		
		AssessedGefValueSet ag2 = new AssessedGefValueSet();
		ag2.setGefValueId(gef2.getId().intValue());
		ag2.setAssessmentId(aa2.getId().intValue());
		assessedGefValuesRepository.save(ag2);		

		assessmentRepository.flush();

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");
		
		List<Object[]> result = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(uir1.getId(), ddv1.getId(), start, end);
		
		Mockito.doReturn(result).when(assessmentService).getLast5AssessmentsForDiagramTimeline(uir1.getId(), ddv1.getId(), start, end);
		
		Response response = assessmentService.getLast5ForDiagram(uir1.getId(), ddv1.getId(), "2015-01-01", "2017-01-01");
		
		String output = (String) response.getEntity();

		Assert.assertEquals("{\"series\":[{\"name\":\"Only\",\"imgSize\":\"20px\",\"markerSize\":32,\"markerDisplayed\":\"on\",\"lineType\":\"none\"}]}", output);

	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void getDiagramDataTest() throws Exception {
		
		/*
		 * Test scenario
		 * 5 time intervals
		 * 2 detection variables: GES1 and GES2 (parent factor GEF1)
		 * 1 value for each detection variable
		 */
		
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
		

		SimpleDateFormat isoFormat = new SimpleDateFormat("MM/yyyy");
		isoFormat.setTimeZone(TimeZone.getTimeZone(p1.getTimeZone()));
		String dateInTZ = isoFormat.format(ti1.getIntervalStart());
		
		UserInSystem uis1 = new UserInSystem();
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
		gef1.setGefValue(new BigDecimal(4.0));
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setTimeInterval(ti5);
		gef2.setUserInRoleId(uir1.getId());
		gef2.setUserInRole(uir1);
		gef2.setDetectionVariableId(dv3.getId());
		gef2.setDetectionVariable(dv3);
		gef2.setGefValue(new BigDecimal(1.8).setScale(1, RoundingMode.HALF_UP));
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
		
		Mockito.doReturn(result).when(assessmentService).getDiagramDataForUserInRoleId(uir1.getId(), dv1.getId());
		
		Response response = assessmentService.getDiagramData(uir1.getId(), dv1.getId());
		
		DataSet output = (DataSet) response.getEntity();
		
		String json = objectMapper.writeValueAsString(output);
		
		Assert.assertEquals("{\"groups\":[\"03/2016\",\"04/2016\",\"05/2016\"],\"series\":[{\"name\":\"GES1\",\"items\":[4,null,null]},{\"name\":\"GES2\",\"items\":[null,null,1.8]}]}", json);
		
	}

}
