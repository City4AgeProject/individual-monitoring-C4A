package eu.city4age.dashboard.api.math;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.Ts;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorPredictionValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType.Type;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorPredictionValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.PredictionService;

/*
 * author: Marina-Andric
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class PredictionServiceTest {

	static protected Logger logger = LogManager.getLogger(PredictionServiceTest.class);
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Mock
	private GeriatricFactorRepository geriatricFactorRepositoryMock;
	
	@Autowired
	private UserInSystemRepository userInSystemRepository;
	
	@Autowired
	private MeasuresEndpoint measuresEndpoint;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Autowired
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepository;
	
	@Mock
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepositoryMock;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;
	
	@Mock
	private NativeQueryRepository nativeQueryRepositoryMock;
	
	@Autowired 
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }

	@Mock
	private PilotRepository pilotRepositoryMock;


	@Mock
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepositoryMock;

	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;

	@Mock
	private PilotDetectionVariableRepository pilotDetectionVariableRepositoryMock;
	
	@Mock
	private TimeIntervalRepository timeIntervalRepositoryMock;
	
	@Mock
	private ImputeFactorService imputeFactorServiceMock;
    
	@Mock
	private MeasuresEndpoint measuresEndpointMock;
	
	@Spy
	@InjectMocks
	private PredictionService predictionService;

	private int predictionSize = 3;
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void makePredictionTest() {
		
		
//		double[] dataArray  = new double[]{3.0, 2.5, 3.15,  2.75,  2.75,  3.4,   2.75,  4.4,   3.5,   3.65,  3.65,  3.25};

		TimeInterval ti1 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);	
//		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Date.from(LocalDate.parse("2016-05-01").atStartOfDay(ZoneId.of(zone)).toInstant()), //UTC+2
//				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti3 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-06-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti4 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-07-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti5 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-08-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti6 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-09-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);	
		TimeInterval ti7 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-10-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti8 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-11-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti9 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-12-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti11 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-02-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		TimeInterval ti12 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-03-01 00:00:00"), 
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
		
		
		UserInRole uir = new UserInRole();
		uir.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir);
			
		DetectionVariableType dvt = new DetectionVariableType(Type.GEF, "DVType");
		detectionVariableTypeRepository.save(dvt);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("DVName");
		dv1.setDetectionVariableType(dvt);
		detectionVariableRepository.save(dv1);

		GeriatricFactorValue gef1 = new GeriatricFactorValue();
		gef1.setGefValue(new BigDecimal("3.0"));
		gef1.setUserInRole(uir);
		gef1.setTimeInterval(ti1);
		gef1.setUserInRoleId(uir.getId());
		gef1.setDetectionVariable(dv1);
		gef1.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef1);
		
//		GeriatricFactorValue gef2 = new GeriatricFactorValue();
//		gef2.setGefValue(new BigDecimal("2.5"));
//		gef2.setUserInRole(uir);
//		gef2.setTimeInterval(ti2);
//		gef2.setUserInRoleId(uir.getId());
//		gef2.setDetectionVariable(dv1);
//		gef2.setDetectionVariableId(dv1.getId());
//		geriatricFactorRepository.save(gef2);

		GeriatricFactorValue gef3 = new GeriatricFactorValue();
		gef3.setGefValue(new BigDecimal("3.15"));
		gef3.setUserInRole(uir);
		gef3.setTimeInterval(ti3);
		gef3.setUserInRoleId(uir.getId());
		gef3.setDetectionVariable(dv1);
		gef3.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef3);
		
		GeriatricFactorValue gef4 = new GeriatricFactorValue();
		gef4.setGefValue(new BigDecimal("2.75"));
		gef4.setUserInRole(uir);
		gef4.setTimeInterval(ti4);
		gef4.setUserInRoleId(uir.getId());
		gef4.setDetectionVariable(dv1);
		gef4.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef4);
		
		GeriatricFactorValue gef5 = new GeriatricFactorValue();
		gef5.setGefValue(new BigDecimal("2.75"));
		gef5.setUserInRole(uir);
		gef5.setTimeInterval(ti5);
		gef5.setUserInRoleId(uir.getId());
		gef5.setDetectionVariable(dv1);
		gef5.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef5);
		
		GeriatricFactorValue gef6 = new GeriatricFactorValue();
		gef6.setGefValue(new BigDecimal("3.4"));
		gef6.setUserInRole(uir);
		gef6.setTimeInterval(ti6);
		gef6.setUserInRoleId(uir.getId());
		gef6.setDetectionVariable(dv1);
		gef6.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef6);
		
		GeriatricFactorValue gef7 = new GeriatricFactorValue();
		gef7.setGefValue(new BigDecimal("2.75"));
		gef7.setUserInRole(uir);
		gef7.setTimeInterval(ti7);
		gef7.setUserInRoleId(uir.getId());
		gef7.setDetectionVariable(dv1);
		gef7.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef7);

		GeriatricFactorValue gef8 = new GeriatricFactorValue();
		gef8.setGefValue(new BigDecimal("4.4"));
		gef8.setUserInRole(uir);
		gef8.setTimeInterval(ti8);
		gef8.setUserInRoleId(uir.getId());
		gef8.setDetectionVariable(dv1);
		gef8.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef8);

		GeriatricFactorValue gef9 = new GeriatricFactorValue();
		gef9.setGefValue(new BigDecimal("3.5"));
		gef9.setUserInRole(uir);
		gef9.setTimeInterval(ti9);
		gef9.setUserInRoleId(uir.getId());
		gef9.setDetectionVariable(dv1);
		gef9.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef9);

		GeriatricFactorValue gef11 = new GeriatricFactorValue();
		gef11.setGefValue(new BigDecimal("3.65"));
		gef11.setUserInRole(uir);
		gef11.setTimeInterval(ti11);
		gef11.setUserInRoleId(uir.getId());
		gef11.setDetectionVariable(dv1);
		gef11.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef11);

		GeriatricFactorValue gef12 = new GeriatricFactorValue();
		gef12.setGefValue(new BigDecimal("3.25"));
		gef12.setUserInRole(uir);
		gef12.setTimeInterval(ti12);
		gef12.setUserInRoleId(uir.getId());
		gef12.setDetectionVariable(dv1);
		gef12.setDetectionVariableId(dv1.getId());
		geriatricFactorRepository.save(gef12);
		
		ti1.getGeriatricFactorValue().add(gef1);
		timeIntervalRepository.save(ti1);

//		ti2.getGeriatricFactorValue().add(gef2);
//		timeIntervalRepository.save(ti2);

		ti3.getGeriatricFactorValue().add(gef3);
		timeIntervalRepository.save(ti3);
		
		ti4.getGeriatricFactorValue().add(gef4);
		timeIntervalRepository.save(ti4);
		
		ti5.getGeriatricFactorValue().add(gef5);
		timeIntervalRepository.save(ti5);
		
		ti6.getGeriatricFactorValue().add(gef6);
		timeIntervalRepository.save(ti6);
		
		ti7.getGeriatricFactorValue().add(gef7);
		timeIntervalRepository.save(ti7);
		
		ti8.getGeriatricFactorValue().add(gef8);
		timeIntervalRepository.save(ti8);
		
		ti9.getGeriatricFactorValue().add(gef9);
		timeIntervalRepository.save(ti9);
		
//		ti10.getGeriatricFactorValue().add(gef10);
//		timeIntervalRepository.save(ti10);
		
		ti11.getGeriatricFactorValue().add(gef11);
		timeIntervalRepository.save(ti11);
		
		ti12.getGeriatricFactorValue().add(gef12);
		timeIntervalRepository.save(ti12);

		TimeInterval tiInterp2 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);	

		
		GeriatricFactorInterpolationValue gfiv2 = new GeriatricFactorInterpolationValue();
		gfiv2.setGefValue(new BigDecimal("2.5"));
		gfiv2.setId(1L);
		gfiv2.setUserInRole(uir);
		gfiv2.setTimeInterval(tiInterp2);
		gfiv2.setUserInRoleId(uir.getId());
		gfiv2.setDetectionVariable(dv1);
		gfiv2.setDetectionVariableId(dv1.getId());
		geriatricFactorInterpolationValueRepository.save(gfiv2);
		
		TimeInterval tiInterp10 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-01-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		
		GeriatricFactorInterpolationValue gfiv10 = new GeriatricFactorInterpolationValue();
		gfiv10.setGefValue(new BigDecimal("3.65"));
		gfiv10.setUserInRole(uir);
		gfiv10.setTimeInterval(tiInterp10);
		gfiv10.setUserInRoleId(uir.getId());
		gfiv10.setDetectionVariable(dv1);
		gfiv10.setDetectionVariableId(dv1.getId());
		geriatricFactorInterpolationValueRepository.save(gfiv10);

		List<Object[]> data2 = nativeQueryRepository.getJointGefValues(dv1.getId(), uir.getId());		
		Mockito.when(nativeQueryRepositoryMock.getJointGefValues(dv1.getId(), uir.getId())).thenReturn(data2);
		
		TimeInterval endDate = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-03-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH); 
		Mockito.when(
				measuresEndpointMock.getOrCreateTimeInterval(Timestamp.valueOf("2017-03-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).
				thenReturn(endDate);
	
		TimeInterval timePred1 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-04-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH); 
		Mockito.when(
				measuresEndpointMock.getOrCreateTimeInterval(Timestamp.valueOf("2017-04-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).
				thenReturn(timePred1);

		TimeInterval timePred2 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-05-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH); 
		Mockito.when(
				measuresEndpointMock.getOrCreateTimeInterval(Timestamp.valueOf("2017-05-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).
				thenReturn(timePred2);

		TimeInterval timePred3 = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf("2017-06-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH); 
		Mockito.when(
				measuresEndpointMock.getOrCreateTimeInterval(Timestamp.valueOf("2017-06-01 00:00:00.0"),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).
				thenReturn(timePred3);

		
		GeriatricFactorPredictionValue prediction1 = new GeriatricFactorPredictionValue();
		prediction1.setUserInRoleId(uir.getId());
		prediction1.setGefValue(new BigDecimal(3.5223592680743034));
		prediction1.setTimeInterval(timePred1);
		geriatricFactorPredictionValueRepository.save(prediction1);
		
		GeriatricFactorPredictionValue prediction2 = new GeriatricFactorPredictionValue();
		prediction2.setUserInRoleId(uir.getId());
		prediction2.setGefValue(new BigDecimal(3.3369103408093777));
		prediction2.setTimeInterval(timePred2);
		geriatricFactorPredictionValueRepository.save(prediction2);
		
		GeriatricFactorPredictionValue prediction3 = new GeriatricFactorPredictionValue();
		prediction3.setUserInRoleId(uir.getId());
		prediction3.setGefValue(new BigDecimal(3.4631821760469776));
		prediction3.setTimeInterval(timePred3);
		geriatricFactorPredictionValueRepository.save(prediction3);
		
	
		Mockito.when(geriatricFactorPredictionValueRepositoryMock.save(Mockito.any(GeriatricFactorPredictionValue.class))).thenReturn(null);

		double[] dataArray  = new double[]{3.0, 2.5,   3.15,  2.75,  2.75,  3.4,   2.75,  4.4,   3.5,   3.65,  3.65,  3.25};
		TimeSeries timeSeries = Ts.newMonthlySeries(dataArray);
		
		ArimaOrder optimalModelOrder = ArimaOrder.order(1, 1, 0, 0, 0, 0);
		Arima optimalModelFit = Arima.model(timeSeries, optimalModelOrder);
		Forecast forecast = optimalModelFit.forecast(predictionSize);

		predictionService.makePredictions(dv1.getId(), uir.getId(), endDate.getIntervalStart());
		
		Assert.assertEquals(prediction1.getGefValue(), new BigDecimal(forecast.pointEstimates().at(0)));
		Assert.assertEquals(prediction2.getGefValue(), new BigDecimal(forecast.pointEstimates().at(1)));
		Assert.assertEquals(prediction3.getGefValue(), new BigDecimal(forecast.pointEstimates().at(2)));

	}
	
}
