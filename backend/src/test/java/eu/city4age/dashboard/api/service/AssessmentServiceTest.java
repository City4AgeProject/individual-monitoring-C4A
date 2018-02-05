package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepositoryTest;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.dto.oj.DataSet;
import eu.city4age.dashboard.api.rest.AssessmentsService;
import eu.city4age.dashboard.api.rest.MeasuresService;
import org.mockito.runners.MockitoJUnitRunner;



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
	private MeasuresService measuresService;
	
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
	@Rollback(true)
	public void getDiagramDataTest() throws Exception {
		
		/*
		 * Test scenario
		 * 5 time intervals
		 * 2 detection variables: GES1 and GES2 (parent factor GEF1)
		 * 1 value for each detection variable
		 */
		
		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
				
		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-03-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
				
		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-02-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);
		
		UserInSystem uis1 = new UserInSystem();
		userInSystemRepository.save(uis1);

		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
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
		gef1.setDetectionVariableId(dv2.getId());
		gef1.setDetectionVariable(dv2);
		gef1.setGefValue(new BigDecimal(4.0));
		geriatricFactorRepository.save(gef1);
		
		GeriatricFactorValue gef2 = new GeriatricFactorValue();
		gef2.setTimeInterval(ti5);
		gef2.setUserInRoleId(uir1.getId());
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
		
		Assert.assertEquals("{\"groups\":[\"2016-01-01 00:00:00.0\",\"2016-02-01 00:00:00.0\",\"2016-03-01 00:00:00.0\",\"2016-04-01 00:00:00.0\",\"2016-05-01 00:00:00.0\"],\"series\":[{\"name\":\"GES1\",\"items\":[null,null,4,null,null]},{\"name\":\"GES2\",\"items\":[null,null,null,null,1.8]}]}", json);
		
	}

}
