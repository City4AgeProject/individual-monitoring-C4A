package eu.city4age.dashboard.api.math;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.MeasuresService;

/*
* author: Vladimir Aleksic
*/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ImputeFactorServiceTest {

               @Spy
               @InjectMocks
               private ImputeFactorService imputeFactorService=new ImputeFactorService();

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
               private MeasuresEndpoint measuresEndpoint;
               
               @Mock
               private MeasuresEndpoint measuresEndpointMock;
               
               @Autowired
               private MeasuresService measuresService;
               
               @Autowired
               private UserInSystemRepository userInSystemRepository;
               
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
               
               static protected Logger logger = LogManager.getLogger(ImputeFactorServiceTest.class);
               
               private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
               
//            private TimeInterval ti;
               
               @Test
               @Transactional
               @Rollback(true)
               public void imputeMissingValuesTest() throws Exception {
                              
                              GeriatricFactorValue gef;
                              TimeInterval ti;
                              
                              List<DetectionVariable> list = detectionVariableRepository.findAll();
                              System.out.println("size: " + list.size());

                              Pilot pilot=new Pilot();
                              pilot.setLatestVariablesComputed(new Date(Timestamp.valueOf("2017-04-30 00:00:00").getTime()));
                              pilot.setPilotCode(Pilot.PilotCode.LCC);
                              pilot.setName("lecce");
                              pilot=pilotRepository.save(pilot);
                              
                              UserInSystem uis = new UserInSystem();
                              uis.setUsername("username");
                              uis.setPassword("Password");
                              userInSystemRepository.save(uis);
                              
                              UserInRole userInRole = new UserInRole();
                              userInRole.setPilotCode(Pilot.PilotCode.LCC);
                              userInRole.setUserInSystem(uis);
                              userInRole = userInRoleRepository.save(userInRole);
                              System.out.println("UIR: "+userInRole.getId());
                              
                              DetectionVariableType dvt1 = DetectionVariableType.GES;
                              detectionVariableTypeRepository.save(dvt1);

                              DetectionVariableType dvt2 = DetectionVariableType.GEF;
                              detectionVariableTypeRepository.save(dvt2);
                              
                              DetectionVariableType dvt3 = DetectionVariableType.GFG;
                              detectionVariableTypeRepository.save(dvt3);
                              
                              DetectionVariable dv1 = new DetectionVariable();
                              dv1.setDetectionVariableName("DV1");
                              dv1.setDetectionVariableType(dvt1);
                              dv1=detectionVariableRepository.save(dv1);
                              System.out.println("DV1: "+dv1.getId());
                                             
                              DetectionVariable dv2 = new DetectionVariable();
                              dv2.setDetectionVariableType(dvt2);
                              dv2.setDetectionVariableName("DV2");
                              dv2=detectionVariableRepository.save(dv2);
                              System.out.println("DV2: "+dv2.getId());
                              
                              DetectionVariable dv3 = new DetectionVariable();
                              dv3.setDetectionVariableType(dvt3);
                              dv3.setDetectionVariableName("DV3");
                              dv3=detectionVariableRepository.save(dv3);
                              System.out.println("DV3: "+dv3.getId());
                              
                              PilotDetectionVariable pdv1 = new PilotDetectionVariable();
                              pdv1.setPilotCode(Pilot.PilotCode.LCC);
                              pdv1.setDetectionVariable(dv1);
                              pdv1.setDerivedDetectionVariable(dv2);
                              pilotDetectionVariableRepository.save(pdv1);
                              
                              PilotDetectionVariable pdv2 = new PilotDetectionVariable ();
                              pdv2.setPilotCode(Pilot.PilotCode.LCC);
                              pdv2.setDetectionVariable(dv2);
                              pdv2.setDerivedDetectionVariable(dv3);
                              pilotDetectionVariableRepository.save(pdv2);

                              String[] timeIntervals= { "2016-01-01 00:00:00",
                                                       "2016-03-01 00:00:00",
                                                       "2016-04-01 00:00:00",
                                                       "2016-05-01 00:00:00",
                                                       "2016-08-01 00:00:00",
                                                       "2016-09-01 00:00:00",
                                                       "2016-11-01 00:00:00",
                                                       "2016-12-01 00:00:00"};
                              
                              String[] allIntervals=        {              "2016-01-01 00:00:00",
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
                                                                           "2017-02-01 00:00:00"};

                              double [] gfValues = {                    3.00,
                                                                       2.50,
                                                                       3.15,
                                                                       2.75,
                                                                       2.60,
                                                                       2.60,
                                                                       2.60,
                                                                       2.60};
  
                              TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                              
                              for(int i=0; i<timeIntervals.length; i++) {
                                             ti = measuresService.getOrCreateTimeInterval(Timestamp.valueOf(timeIntervals[i]),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
                                             
                                             gef = new GeriatricFactorValue();
                                             gef.setDerivationWeight(new BigDecimal(1));
                                             gef.setUserInRole(userInRole);
                                             gef.setUserInRoleId(userInRole.getId());
                                             gef.setDetectionVariable(dv3);
                                             gef.setDetectionVariableId(dv3.getId());
                                             gef.setTimeInterval(ti);
                                             gef.setGefValue(new BigDecimal (gfValues[i]));
                                             geriatricFactorRepository.save(gef);

                              }
                              geriatricFactorRepository.flush();
                              
                              for(String t: allIntervals) {
                                             TimeInterval ts1=measuresService.getOrCreateTimeInterval(Timestamp.valueOf(t), eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
                                             Mockito.when(measuresServiceMock.getOrCreateTimeInterval(Timestamp.valueOf(t), eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH)).thenReturn(ts1);
                              }

                              Long dvId=dv3.getId();
                              Long uId=userInRole.getId();        
                              Date endDatePilot = Timestamp.valueOf("2017-02-01 00:00:00");
                              
                              List<GeriatricFactorValue> gfr = geriatricFactorRepository.findByDetectionVariableId(dvId, uId);
                              Mockito.when(geriatricFactorRepositoryMock.findByDetectionVariableId(dvId, uId)).thenReturn(gfr);
                              
                              List<ViewGefCalculatedInterpolatedPredictedValues> geriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uId);
               Mockito.when(viewGefCalculatedInterpolatedPredictedValuesRepositoryMock.findByDetectionVariableIdNoPredicted(dvId, uId)).thenReturn(geriatricFactorValue);
                              
                              int imputiranih=imputeFactorService.imputeMissingValues(dv3, userInRole, endDatePilot);
                              Assert.assertEquals(1+3+2, imputiranih);
                              
               }
}
