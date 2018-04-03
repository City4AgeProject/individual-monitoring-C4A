package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import eu.city4age.dashboard.api.pojo.enu.DataValidity;
import eu.city4age.dashboard.api.pojo.json.AddAssessmentDeserializer;
import eu.city4age.dashboard.api.pojo.persist.Filter;
import eu.city4age.dashboard.api.pojo.ws.C4ALoginResponse;
import eu.city4age.dashboard.api.rest.AssessmentsEndpoint;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;
import eu.city4age.dashboard.api.rest.UserEndpoint;

/*
 * authors: marina.andric
 * 		    milos.holclajtner
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
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
	
	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	private UserInSystemRepository userInSystemRepository;

	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;
	
	@Mock
	private TimeIntervalRepository timeIntervalRepositoryMock;

	@Autowired
	private AssessmentRepository assessmentRepository;
	
	@Mock
	private AssessmentRepository assessmentRepositoryMock;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;
	
	@Mock
	private AssessedGefValuesRepository assessedGefValuesRepositoryMock;

	@Autowired
	private FrailtyStatusTimelineRepository frailtyStatusTimelineRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Mock
	private PilotRepository pilotRepositoryMock;
	
	@Mock
	private NativeQueryRepository nativeQueryRepositoryMock;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private MeasuresEndpoint measuresService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AudienceRolesRepository audienceRolesRepository;

	@Mock
	private AudienceRolesRepository audienceRolesRepositoryMock;
	
	@Autowired
	private UserEndpoint userService;
	
    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }
    
	@Spy
	@InjectMocks
	AssessmentsEndpoint assessmentService;
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	@Transactional
	@Rollback(true)
	public void addForSelectedDataSetTest() throws Exception {
		/*
		 * Note: For the purpose of creating this test we modified Pilot.java (and consequently MeasureService.java) such that "computedStartDate" attribute is removed from 
		 *  the json produced by addForSelectedDataSet method in AssessmentService
		 */
		
		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);
		
		UserInSystem uis = new UserInSystem ();
		uis.setUsername("user");
		uis.setPassword("pass");
		userInSystemRepository.save(uis);
		
		Role r1 = new Role();
		roleRepository.save(r1);
		
		UserInRole uir = new UserInRole();
		uir.setPilot(p1);
		uir.setPilotCode(p1.getPilotCode());
		uir.setUserInSystem(uis);
		uir.setRoleId(Short.valueOf(r1.getId().toString()));
		userInRoleRepository.save(uir);
		
		GeriatricFactorValue gef = new GeriatricFactorValue();
		gef.setGefValue(new BigDecimal("5"));
		gef.setUserInRole(uir);
		geriatricFactorRepository.save(gef);		
		
		UserInRole response = userInRoleRepository.findBySystemUsernameAndPassword("user", "pass");		
		Mockito.when(userInRoleRepositoryMock.findBySystemUsernameAndPassword("user", "pass")).thenReturn(response);
		
		Pilot userPilot = pilotRepository.findOne(p1.getPilotCode());		
		Mockito.when(pilotRepositoryMock.findOne(p1.getPilotCode())).thenReturn(userPilot);
		
		C4ALoginResponse responseLogin = (C4ALoginResponse) userService.login("user", "pass").getEntity();
		
		String jwt = responseLogin.getJwToken();
		
		AddAssessmentDeserializer pojo = new AddAssessmentDeserializer();
		pojo.setDataValidity(DataValidity.FAULTY_DATA);
		pojo.setRiskStatus('A');
		pojo.setComment("Comment");
		pojo.setAudienceIds(Arrays.asList(r1.getId()));
		pojo.setGeriatricFactorValueIds(Arrays.asList(gef.getId()));
		pojo.setJwt(jwt);
		
		String json = objectMapper.writeValueAsString(pojo);
		
		UserInRole userInRole = userInRoleRepository.findBySystemUsername("user");
		Mockito.when(userInRoleRepositoryMock.findBySystemUsername("user")).thenReturn(userInRole);
		
		Assessment assessment = new Assessment();
		assessment.setAssessmentComment(pojo.getComment());
		assessment.setRiskStatus(pojo.getRiskStatus());
		assessment.setGeriatricFactorValue(gef);
		assessment.setDataValidity(pojo.getDataValidity().toChar());
		assessment.setUserInRole(uir);
		assessmentRepository.save(assessment);
		
		List<AssessmentAudienceRole> assessmentAudienceRoles = new ArrayList<AssessmentAudienceRole>();
		List<AssessedGefValueSet> assessedGefValueSets = new ArrayList<AssessedGefValueSet>();

			for (Long audienceId : pojo.getAudienceIds())
				assessmentAudienceRoles.add(new AssessmentAudienceRole.AssessmentAudienceRoleBuilder()
						.assessmentId(assessment.getId().intValue()).userInRoleId(audienceId.intValue()).build());

			for (Long gefId : pojo.getGeriatricFactorValueIds())
				assessedGefValueSets.add(new AssessedGefValueSet.AssessedGefValueSetBuilder()
						.assessmentId(assessment.getId().intValue()).gefValueId(gefId.intValue()).build());

		Assessment ass;
		
		Mockito.when(assessmentRepositoryMock.saveAndFlush(Mockito.any(Assessment.class))).thenReturn(ass = assessmentRepository.saveAndFlush(assessment));
		
		Response result = assessmentService.addForSelectedDataSet(jwt, json);
		
		Assert.assertTrue(result.getEntity().toString().contains("{\"id\":"+ass.getId()+",\"userInRole\":"));
		Assert.assertTrue(result.getEntity().toString().contains("\"userInRole\":{\"id\":"+userInRole.getId()+""));
		Assert.assertTrue(result.getEntity().toString().contains("\"pilotCode\":\"LCC\",\"pilot\":{\"pilotCode\":\"LCC\""));
		Assert.assertTrue(result.getEntity().toString().contains("\"userInSystem\":{\"id\":"+uis.getId()+",\"username\":\"user\",\"password\":\"pass\"},\"roleId\":"+r1.getId()+"}"));
		Assert.assertTrue(result.getEntity().toString().contains("\"assessmentComment\":\"Comment\",\"riskStatus\":\"A\",\"dataValidity\":\"F\""));
		Assert.assertTrue(result.getEntity().toString().contains("\"pilotCode\":\"LCC\",\"pilot\":{\"pilotCode\":\"LCC\""));
		Assert.assertTrue(result.getEntity().toString().contains("\"dataValidityDesc\":\"Faulty data\""));
		Assert.assertTrue(result.getEntity().toString().contains("\"riskStatusDesc\":\"Alert\""));
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void findForSelectedDataSetTest() throws Exception {
		
		/*
		 * 2 users in role
		 * 2 geriatric factor values
		 * 2 roles
		 * 7 assessments
		 */
		
		UserInRole uir1 = new UserInRole();
		uir1 = userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole();
		uir2 = userInRoleRepository.save(uir2);
		
		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setGefValue(new BigDecimal("5"));
		gef1.setUserInRole(uir1);
		gef1 = geriatricFactorRepository.save(gef1);		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setGefValue(new BigDecimal("6"));
		gef2.setUserInRole(uir2);
		gef2 = geriatricFactorRepository.save(gef2);

		Role r1 = new Role();
		r1 = roleRepository.save(r1);
		
		Role r2 = new Role();
		r2 = roleRepository.save(r2);

		/*
		 * these seem to be added automaticaly, no need for manual addition
		 * AssessedGefValueSet ag1 = new AssessedGefValueSet();
		ag1.setGefValueId(1);
		ag1.setAssessmentId(1);
		ag1 = assessedGefValuesRepository.save(ag1);		
		AssessedGefValueSet ag2 = new AssessedGefValueSet();
		ag2.setGefValueId(2);
		ag2.setAssessmentId(2);
		ag2 = assessedGefValuesRepository.save(ag2);
		
		AssessmentAudienceRole ar1 = new AssessmentAudienceRole();
		ar1.setAssessmentId(1);
		ar1.setRoleId(1);
		ar1.setAssigned(new Timestamp(System.currentTimeMillis()));
		ar1 = audienceRolesRepository.save(ar1);		
		AssessmentAudienceRole ar2 = new AssessmentAudienceRole();
		ar2.setAssessmentId(2);
		ar2.setRoleId(2);
		ar2.setAssigned(new Timestamp(System.currentTimeMillis()));
		ar2 = audienceRolesRepository.save(ar2); */
		//data for second assessment and joined tables:


		Assessment aa1 = new Assessment();
		aa1.setUserInRole(uir1);
		aa1.setGeriatricFactorValue(gef1);
		aa1.getRoles().add(r1);
		aa1.setDataValidity('F');//F,Q
		aa1.setRiskStatus('W');//W,A
		aa1 = assessmentRepository.save(aa1);
	
		Assessment aa2 = new Assessment();
		aa2.setUserInRole(uir2);//
		aa2.setGeriatricFactorValue(gef1);
		aa2.getRoles().add(r2);
		aa2.setDataValidity('F');//F,Q
		aa2.setRiskStatus('W');//W,A
		aa2 = assessmentRepository.save(aa2);

		Assessment aa3 = new Assessment();
		aa3.setUserInRole(uir1);
		aa3.setGeriatricFactorValue(gef1);
		aa3.getRoles().add(r1);
		aa3.setDataValidity('Q');//F,Q
		aa3.setRiskStatus('W');//W,A
		aa3 = assessmentRepository.save(aa3);
		
		Assessment aa4 = new Assessment();
		aa4.setUserInRole(uir1);
		aa4.setGeriatricFactorValue(gef1);
		aa4.getRoles().add(r1);
		aa4.setDataValidity('F');//F,Q
		aa4.setRiskStatus('A');//W,A
		aa4 = assessmentRepository.save(aa4);
		
		Assessment aa5 = new Assessment();
		aa5.setUserInRole(uir1);
		aa5.setGeriatricFactorValue(gef2);
		aa5.getRoles().add(r1);
		aa5.setDataValidity('F');//F,Q
		aa5.setRiskStatus('W');//W,A
		aa5 = assessmentRepository.save(aa5);
		
		Assessment aa6 = new Assessment();
		aa6.setGeriatricFactorValue(gef2);
		aa6 = assessmentRepository.save(aa6);
		
		Assessment aa7 = new Assessment();
		aa7 = assessmentRepository.save(aa7);
		
		List<Filter> filters = new ArrayList<Filter>();
		List<Object> inParams = new ArrayList<Object>();
		
		//riskStatus filter (W,A):
		Filter riskStatus = new Filter();
		riskStatus.setName("riskStatus");
		
		inParams = new ArrayList<Object>();
		inParams.add('W');
		
		riskStatus.getInParams().put("riskStatus", inParams);
		filters.add(riskStatus);
		
		//dataValidity filter (F,Q)
		Filter dataValidity = new Filter();
		dataValidity.setName("dataValidity");
		
		inParams = new ArrayList<Object>();
		inParams.add('F');

		dataValidity.getInParams().put("dataValidity", inParams);
		filters.add(dataValidity);
		
		//userInRoleId filter (r1):
		Filter roleId = new Filter();
		roleId.setName("roleId");
		
		inParams = new ArrayList<Object>();
		inParams.add(r1.getId());
		
		roleId.getInParams().put("roleId", inParams);
		filters.add(roleId);
				
		List<Long> gefIds = new ArrayList<Long>();
		gefIds.add(gef1.getId());
		gefIds.add(gef2.getId());
		

		HashMap<String, Object> inQueryParams = new HashMap<String, Object>();
		inQueryParams.put("geriatricFactorIds", gefIds);

		List<Filter> noFilters = new ArrayList<Filter>();
		
		// Number of assessments in repository before filtering
		Assert.assertEquals(7, assessmentRepository.findAll().size());
				
		List<Assessment> resultWithoutFilters = assessmentRepository.doQueryWithFilter(noFilters , "findForSelectedDataSet",
				inQueryParams);
		
		// Number of assessments in repository with no filters
		Assert.assertEquals(7, assessmentRepository.findAll().size());
		Assert.assertNotNull(resultWithoutFilters);
		Assert.assertEquals(5, resultWithoutFilters.size());
	

		List<Assessment> resultWithFilters = assessmentRepository.doQueryWithFilter(filters, "findForSelectedDataSet",
				inQueryParams);		

		Assert.assertNotNull(resultWithFilters);
		Assert.assertEquals(3, assessmentRepository.findAll().size());
		Assert.assertEquals(2, resultWithFilters.size());
		
	}

	@Test
	@Transactional
	@Rollback(true)
	public void getLast5ForDiagramTest() throws Exception {
		
		/*
		 * Test scenario
		 * 2 users in role
		 * 7 time intervals
		 * 2 geriatric factor values
		 * 2 assessments
		 */

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
		ddv1 = detectionVariableRepository.save(ddv1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDerivedDetectionVariable(ddv1);		
		dv1 = detectionVariableRepository.save(dv1);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable ();
		pdv1.setDerivedDetectionVariable(ddv1);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDetectionVariable(dv1);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		pdv2.setDerivedDetectionVariable(ddv1);
		pdv2.setPilotCode(Pilot.PilotCode.ATH);
		pdv2.setDetectionVariable(dv1);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);
		
		UserInSystem uis = new UserInSystem ();
		userInSystemRepository.save(uis);
		
		UserInRole uir1 = new UserInRole();
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		uir1.setUserInSystem(uis);
		uir1 = userInRoleRepository.save(uir1);
		UserInRole uir2 = new UserInRole();
		uir2.setPilotCode(Pilot.PilotCode.ATH);
		uir2.setUserInSystem(uis);
		uir2 = userInRoleRepository.save(uir2);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setTimeInterval(ti1);
		gef1.setDetectionVariable(dv1);
		gef1.setUserInRole(uir1);
		gef1.setGefValue(new BigDecimal (1));
		gef1 = geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
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
		aa2 = assessmentRepository.save(aa2);
		
		AssessedGefValueSet ag2 = new AssessedGefValueSet();
		ag2.setGefValueId(gef2.getId().intValue());
		ag2.setAssessmentId(aa2.getId().intValue());
		assessedGefValuesRepository.save(ag2);		

		assessmentRepository.flush();

		Timestamp start = Timestamp.valueOf("2015-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2017-01-01 00:00:00");
		
		List<Object[]> result = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(uir1.getId(), ddv1.getId(), start, end);
		
		Mockito.when(nativeQueryRepositoryMock.getLast5AssessmentsForDiagramTimeline(uir1.getId(), ddv1.getId(), start, end)).thenReturn(result);
		
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
		
		String timeZone = timeIntervalRepository.testHql(ti3.getIntervalStart());
		logger.info("zone: " + timeZone);

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
		
		Assert.assertNotNull(result);
		
		logger.info("result: " + result);
		
		Assert.assertEquals(3, result.size());
		
		Assert.assertEquals(Date.from(LocalDate.parse("2016-03-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(0).getIntervalStart());
		Assert.assertEquals(Date.from(LocalDate.parse("2016-04-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(1).getIntervalStart());
		Assert.assertEquals(Date.from(LocalDate.parse("2016-05-01").atStartOfDay(ZoneId.of(zone)).toInstant()), result.get(2).getIntervalStart());
		
		Assert.assertEquals(1, result.get(0).getGeriatricFactorValue().size());
		Assert.assertEquals(0, result.get(1).getGeriatricFactorValue().size());
		Assert.assertEquals(1, result.get(2).getGeriatricFactorValue().size());
		
		Mockito.when(timeIntervalRepositoryMock.getDiagramDataForUserInRoleId(uir1.getId(), dv1.getId())).thenReturn(result);
		
		Response response = assessmentService.getDiagramData(uir1.getId(), dv1.getId());
		
		DataSet output = (DataSet) response.getEntity();
		
		String json = objectMapper.writeValueAsString(output);
		
		Assert.assertTrue(json.contains("\"groups\":[\"03/2016\",\"04/2016\",\"05/2016\"]"));
		Assert.assertTrue(json.contains("{\"name\":\"GES1\",\"items\":[4,null,null]}"));
		Assert.assertTrue(json.contains("{\"name\":\"GES2\",\"items\":[null,null,1.8]}"));
		
	}

}
