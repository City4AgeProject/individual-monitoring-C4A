package eu.city4age.dashboard.api.math;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import eu.city4age.dashboard.api.interpolation.InterpolateMissingValuesSpline;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.rest.MeasuresService;

/*
 * author: Vladimir Aleksic
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class InterpolateMissingValuesSplineTest {


	@Spy
	@InjectMocks
	InterpolateMissingValuesSpline imv=new InterpolateMissingValuesSpline();


	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Mock
	private GeriatricFactorRepository geriatricFactorRepositoryMock;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;
	@Mock
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepositoryMock;

	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;
	@Mock
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepositoryMock;

	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private MeasuresService measuresService;
	
	@Mock
	private MeasuresService measuresServiceMock;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Mock
	private PilotRepository pilotRepositoryMock;
	
	/*@Autowired
	private GeriatricFactorInterpolationValue geriatricFactorInterpolationValue;*/
	
	static protected Logger logger = LogManager.getLogger(InterpolateMissingValuesSplineTest.class);
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Test
	@Transactional
	@Rollback(false)
	public void interpolateMissingValuesSplineTest() throws Exception {
		
		Pilot pilot=new Pilot();
		pilot.setLatestVariablesComputed(new Date(Timestamp.valueOf("2017-04-30 00:00:00").getTime()));
		pilot.setPilotCode(Pilot.PilotCode.LCC);
		pilot=pilotRepository.save(pilot);
		
		UserInRole userInRole = new UserInRole();
		userInRole.setPilotCode(Pilot.PilotCode.LCC);
		userInRole = userInRoleRepository.save(userInRole);
		
		DetectionVariableType dvt = DetectionVariableType.GEF;
		dvt = detectionVariableTypeRepository.save(dvt);
		
		DetectionVariableType dvt1 = DetectionVariableType.GES;
		dvt1 = detectionVariableTypeRepository.save(dvt1);
		
		DetectionVariable dv1 = new DetectionVariable();
		//dv1.setId(1L);
		dv1.setDetectionVariableName("DV1");
		dv1.setDetectionVariableType(dvt);
		dv1 = detectionVariableRepository.save(dv1);
			
		DetectionVariable dv2 = new DetectionVariable();
		//dv2.setId(2L);
		dv2.setDetectionVariableType(dvt1);
		dv2.setDetectionVariableName("DV2");
		dv2 = detectionVariableRepository.save(dv2);
		
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(dv1.getId());
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
		//pdv2.setId(dv2.getId());
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pdv2.setDetectionVariable(dv2);
		pdv2.setDerivedDetectionVariable(dv2);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);

		String[] timeIntervals= {	"2016-01-01 00:00:00",
									"2016-03-01 00:00:00",
									"2016-04-01 00:00:00",
									"2016-05-01 00:00:00",
									"2016-06-01 00:00:00",
									"2016-10-01 00:00:00",
									"2016-11-01 00:00:00",
									"2016-12-01 00:00:00",
									"2017-01-01 00:00:00",
									"2017-03-01 00:00:00",
									"2017-04-01 00:00:00",
									"2017-05-01 00:00:00"};
		String[] allIntervals=	{	"2016-01-01 00:00:00",
									"2016-02-01 00:00:00",
									"2016-03-01 00:00:00",
									"2016-04-01 00:00:00",
									"2016-05-01 00:00:00",
									"2016-06-01 00:00:00",
									"2016-07-01 00:00:00",
									"2016-08-01 00:00:00",
									"2016-09-01 00:00:00",
									"2016-10-01 00:00:00",
									"2016-11-01 00:00:00",
									"2016-12-01 00:00:00",
									"2017-01-01 00:00:00",
									"2017-02-01 00:00:00",
									"2017-03-01 00:00:00",
									"2017-04-01 00:00:00",
									"2017-05-01 00:00:00",
									"2017-06-01 00:00:00",
									"2017-07-01 00:00:00",
									"2017-08-01 00:00:00"};
		double [] gfValues = {		3.00,
									2.50,
									3.15,
									2.75,
									2.75,
									3.40,
									2.75,
									4.40,
									3.50,
									3.65,
									3.65,
									3.25};

		GeriatricFactorValue gf0 = null;
		for(int i=0; i<timeIntervals.length; i++) {
			TimeInterval ti = measuresService
					.getOrCreateTimeInterval(Timestamp.valueOf(timeIntervals[i]),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
			

			GeriatricFactorValue gef = new GeriatricFactorValue();
			gef.setUserInRole(userInRole);
			gef.setUserInRoleId(userInRole.getId());
			gef.setDetectionVariable(dv1);
			gef.setDetectionVariableId(dv1.getId());
			gef.setTimeInterval(ti);
			gef.setGefValue(new BigDecimal (gfValues[i]));
			gf0 = geriatricFactorRepository.save(gef);
			//logger.info("gf0: "+gf0);
		}
		geriatricFactorRepository.flush();
		Long dvId=dv1.getId();
		Long uId=userInRole.getId();
		Calendar calendar=Calendar.getInstance();
		//logger.info("dvId: "+dvId);
		//logger.info("uId: "+uId);

		GeriatricFactorInterpolationValue gefi = new GeriatricFactorInterpolationValue();
		gefi.setUserInRole(gf0.getUserInRole());
		gefi.setUserInRoleId(gf0.getUserInRoleId());
		gefi.setDetectionVariable(gf0.getDetectionVariable());
		gefi.setDetectionVariableId(gf0.getDetectionVariableId());
		gefi.setTimeInterval(gf0.getTimeInterval());
		gefi.setGefValue(gf0.getGefValue());
		
		List<GeriatricFactorValue> gfr = geriatricFactorRepository.findByDetectionVariableId(dvId, uId);
		//logger.info("gfrLen: "+gfr.size());
		Mockito.when(geriatricFactorRepositoryMock.findByDetectionVariableId(dvId, uId)).thenReturn(gfr);
		List<ViewGefCalculatedInterpolatedPredictedValues> geriatricFactorValue=viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uId);
		logger.info("wgfrLen: "+geriatricFactorValue.size());
		Mockito.when(viewGefCalculatedInterpolatedPredictedValuesRepositoryMock.findByDetectionVariableIdNoPredicted(dvId, uId)).thenReturn(geriatricFactorValue);
		for(String t: allIntervals) {
			Mockito.when(
					measuresServiceMock.getOrCreateTimeInterval(
							Timestamp.valueOf(t),
							eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).thenReturn(
									measuresService.getOrCreateTimeInterval(
											Timestamp.valueOf(t),
											eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH));
		}
		
		Mockito.when(userInRoleRepositoryMock.findByUirId(uId)).thenReturn(userInRole);
		Mockito.when(pilotRepositoryMock.findByPilotCode(userInRole.getPilotCode())).thenReturn(pilot);
		
		Date endDatePilot=Timestamp.valueOf("2017-07-01 00:00:00");
		
		int imputiranih=imv.getData(dvId, uId, endDatePilot);
		Assert.assertEquals(5, imputiranih);
	}
}

