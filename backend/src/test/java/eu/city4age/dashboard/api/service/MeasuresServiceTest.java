package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.FrailtyStatusTimelineRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorPredictionValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.jpa.ValueEvidenceNoticeRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class MeasuresServiceTest {

	static protected Logger logger = LogManager.getLogger(MeasuresServiceTest.class);

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
	private VariationMeasureValueRepository variationMeasureValueRepository;

	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private ValueEvidenceNoticeRepository valueEvidenceNoticeRepository;

	@Autowired
	private NUIRepository nuiRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

	@Autowired
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepository;

	@Autowired
	private ImputeFactorService imputeFactorService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	private VariationMeasureValueRepository variationMeasureValueRepositoryMock;

	@Mock
	private NUIRepository nuiRepositoryMock;

	@Mock
	private MeasuresEndpoint measuresServiceMock;
	
	@Mock
	private PilotRepository pilotRepositoryMock;

	@Mock
	private NativeQueryRepository nativeQueryRepositoryMock;

	@Mock
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepositoryMock;

	@Mock
	private UserInRoleRepository userInRoleRepositoryMock;

	@Mock
	private GeriatricFactorRepository geriatricFactorRepositoryMock;

	@Mock
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepositoryMock;

	@Mock
	private PilotDetectionVariableRepository pilotDetectionVariableRepositoryMock;
	
	
	@Mock
	private TimeIntervalRepository timeIntervalRepositoryMock;
	
	@Mock
	private ImputeFactorService imputeFactorServiceMock;
	
	@Mock
	private PredictionService predictionServiceMock;
	
	@Spy
	@InjectMocks
	MeasuresEndpoint measuresEndpoint;

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	@Transactional
	@Rollback(true)
	public void getDailyMeasuresTest() throws Exception {

		/*
		 * Test scenario
		 * 5 time intervals
		 * 2 detection variables: GES1 and GES2 (parent factor GEF1)
		 * 1 value for each detection variable
		 */

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-02-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-03-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.DAY);

		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.DAY);

		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);

		UserInSystem uis1 = new UserInSystem();
		userInSystemRepository.save(uis1);

		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir1);

		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("MEA1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("MEA2");
		dv2.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setDetectionVariableName("GES1");
		dv3.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv3);

		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv3);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv1);

		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setDetectionVariable(dv2);
		pdv2.setDerivedDetectionVariable(dv3);
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv2);

		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti3);
		vmv1.setUserInRole(uir1);
		vmv1.setMeasureValue(BigDecimal.valueOf(1.8));
		variationMeasureValueRepository.save(vmv1);

		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setDetectionVariable(dv1);
		vmv2.setTimeInterval(ti5);
		vmv2.setUserInRole(uir1);
		vmv2.setMeasureValue(BigDecimal.valueOf(2.8));
		variationMeasureValueRepository.save(vmv2);

		ti3.getVariationMeasureValues().add(vmv1);
		timeIntervalRepository.save(ti3);

		ti5.getVariationMeasureValues().add(vmv2);
		timeIntervalRepository.save(ti5);

		dv1.getPilotDetectionVariable().add(pdv1);
		detectionVariableRepository.save(dv1);

		dv2.getPilotDetectionVariable().add(pdv2);
		detectionVariableRepository.save(dv2);

		List<VariationMeasureValue> result = variationMeasureValueRepository.findByUserAndGes(uir1.getId(), dv3.getId());

		Mockito.when(variationMeasureValueRepositoryMock.findByUserAndGes(uir1.getId(), dv3.getId())).thenReturn(result);

		Response response = measuresServiceMock.getDailyMeasures(uir1.getId(), dv3.getId());

		String output = (String) response.getEntity();

		String json = objectMapper.writeValueAsString(output);

		Assert.assertEquals(objectMapper.writeValueAsString("[{\"id\":" + vmv1.getId() + ",\"detectionVariable\":{\"id\":" + dv1.getId() + ",\"detectionVariableName\":\"MEA1\"},\"timeInterval\":{\"id\":" + ti3.getId() + ",\"intervalStart\":1456790400000},\"userInRole\":{\"id\":" + uir1.getId() + "},\"measureValue\":1.8,\"valueEvidenceNotice\":null},{\"id\":" + vmv2.getId() + ",\"detectionVariable\":{\"id\":" + dv1.getId() + ",\"detectionVariableName\":\"MEA1\"},\"timeInterval\":{\"id\":" + ti5.getId() + ",\"intervalStart\":1462060800000},\"userInRole\":{\"id\":" + uir1.getId() + "},\"measureValue\":2.8,\"valueEvidenceNotice\":null}]"), json);

	}


	@Test
	@Transactional
	@Rollback(true)
	public void getNuiValuesTest() throws Exception {

		/*
		 * Test scenario
		 * 5 time intervals
		 * 2 detection variables: GES1 and GES2 (parent factor GEF1)
		 * 1 value for each detection variable
		 */

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-01-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-02-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti3 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-03-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.DAY);

		TimeInterval ti4 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-04-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		TimeInterval ti5 = measuresService.getOrCreateTimeInterval(Timestamp.valueOf("2016-05-01 00:00:00"),
				eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.DAY);

		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);

		UserInSystem uis1 = new UserInSystem();
		userInSystemRepository.save(uis1);

		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir1);

		DetectionVariableType dvt1 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("NUI1");
		dv1.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("NUI2");
		dv2.setDetectionVariableType(dvt1);
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setDetectionVariableName("GES1");
		dv3.setDetectionVariableType(dvt2);
		detectionVariableRepository.save(dv3);

		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv3);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv1);

		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setDetectionVariable(dv2);
		pdv2.setDerivedDetectionVariable(dv3);
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pilotDetectionVariableRepository.save(pdv2);

		NumericIndicatorValue nui1 = new NumericIndicatorValue();
		nui1.setDetectionVariable(dv1);
		nui1.setDetectionVariableId(Short.valueOf(dv1.getId().toString()));
		nui1.setTimeInterval(ti3);
		nui1.setUserInRole(uir1);
		nui1.setUserInRoleId(Integer.valueOf(uir1.getId().toString()));
		nui1.setNuiValue(1.8);
		nuiRepository.save(nui1);

		NumericIndicatorValue nui2 = new NumericIndicatorValue();
		nui2.setDetectionVariable(dv1);
		nui2.setDetectionVariableId(Short.valueOf(dv1.getId().toString()));
		nui2.setTimeInterval(ti5);
		nui2.setUserInRole(uir1);
		nui2.setUserInRoleId(Integer.valueOf(uir1.getId().toString()));
		nui2.setNuiValue(2.8);
		nuiRepository.save(nui2);

		ti3.getNumericIndicatorValues().add(nui1);
		timeIntervalRepository.save(ti3);

		ti5.getNumericIndicatorValues().add(nui2);
		timeIntervalRepository.save(ti5);

		dv1.getPilotDetectionVariable().add(pdv1);
		detectionVariableRepository.save(dv1);

		dv2.getPilotDetectionVariable().add(pdv2);
		detectionVariableRepository.save(dv2);

		List<NumericIndicatorValue> result = nuiRepository.getNuisForSelectedGes(uir1.getId(), dv3.getId());

		Mockito.when(nuiRepositoryMock.getNuisForSelectedGes(uir1.getId(), dv3.getId())).thenReturn(result);

		Response response = measuresServiceMock.getNuiValues(uir1.getId(), dv3.getId());

		String output = (String) response.getEntity();

		String json = objectMapper.writeValueAsString(output);

		Assert.assertEquals(objectMapper.writeValueAsString("[{\"id\":" +nui1.getId() + ",\"detectionVariable\":{\"id\":" + dv1.getId() + ",\"detectionVariableName\":\"NUI1\"},\"timeInterval\":{\"id\":" + ti3.getId() + ",\"intervalStart\":1456790400000},\"userInRole\":{\"id\":" + uir1.getId() + "},\"nuiValue\":1.8},{\"id\":" + nui2.getId() + ",\"detectionVariable\":{\"id\":" + dv1.getId() + ",\"detectionVariableName\":\"NUI1\"},\"timeInterval\":{\"id\":" + ti5.getId() + ",\"intervalStart\":1462060800000},\"userInRole\":{\"id\":" + uir1.getId() + "},\"nuiValue\":2.8}]"), json);

	}


	@Test
	@Transactional
	@Rollback(true)
	public void computeFromMeasuresTest() throws Exception {

		/*
		 * Test scenario
		 * 5 time intervals
		 * 2 detection variables: GES1 and GES2 (parent factor GEF1)
		 * 1 value for each detection variable
		 */

		TimeInterval ti1 = new TimeInterval();
		ti1.setIntervalStart(Timestamp.valueOf("2016-01-01 01:00:00"));
		ti1.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti1);

		TimeInterval ti2 = new TimeInterval();
		ti2.setIntervalStart(Timestamp.valueOf("2016-01-01 02:00:00"));
		ti2.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti2);

		TimeInterval ti3 = new TimeInterval();
		ti3.setIntervalStart(Timestamp.valueOf("2016-01-01 03:00:00"));
		ti3.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti3);

		TimeInterval ti4 = new TimeInterval();
		ti4.setIntervalStart(Timestamp.valueOf("2016-01-01 04:00:00"));
		ti4.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti4);

		TimeInterval ti5 = new TimeInterval();
		ti5.setIntervalStart(Timestamp.valueOf("2016-02-01 05:00:00"));
		ti5.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti5);

		TimeInterval ti6 = new TimeInterval();
		ti6.setIntervalStart(Timestamp.valueOf("2016-02-01 06:00:00"));
		ti6.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti6);

		TimeInterval ti7 = new TimeInterval();
		ti7.setIntervalStart(Timestamp.valueOf("2016-02-01 07:00:00"));
		ti7.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti7);

		TimeInterval ti8 = new TimeInterval();
		ti8.setIntervalStart(Timestamp.valueOf("2016-02-01 08:00:00"));
		ti8.setTypicalPeriod("DAY");
		timeIntervalRepository.save(ti8);

		TimeInterval ti9 = new TimeInterval();
		ti9.setIntervalStart(Timestamp.valueOf("2016-01-01 00:00:00"));
		ti9.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti9);

		TimeInterval ti10 = new TimeInterval();
		ti10.setIntervalStart(Timestamp.valueOf("2016-02-01 00:00:00"));
		ti10.setTypicalPeriod("MON");
		timeIntervalRepository.save(ti10);

		Pilot p1 = new Pilot();
		p1.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p1);

		UserInSystem uis1 = new UserInSystem();
		userInSystemRepository.save(uis1);

		UserInRole uir1 = new UserInRole();
		uir1.setUserInSystem(uis1);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir1);

		DetectionVariableType dvt1 = DetectionVariableType.MEA;
		detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		detectionVariableTypeRepository.save(dvt2);

		DetectionVariableType dvt3 = DetectionVariableType.GES;
		detectionVariableTypeRepository.save(dvt3);

		DetectionVariableType dvt4 = DetectionVariableType.GEF;
		detectionVariableTypeRepository.save(dvt4);

		DetectionVariableType dvt5 = DetectionVariableType.GFG;
		detectionVariableTypeRepository.save(dvt5);

		DetectionVariableType dvt6 = DetectionVariableType.OVL;
		detectionVariableTypeRepository.save(dvt6);

		//MEAs
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setDetectionVariableName("MEA1");
		dv1.setDetectionVariableType(dvt1);
		dv1.setDefaultTypicalPeriod("DAY");
		detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setDetectionVariableName("MEA2");
		dv2.setDetectionVariableType(dvt1);
		dv2.setDefaultTypicalPeriod("DAY");
		detectionVariableRepository.save(dv2);

		DetectionVariable dv3 = new DetectionVariable();
		dv3.setDetectionVariableName("MEA3");
		dv3.setDetectionVariableType(dvt1);
		dv3.setDefaultTypicalPeriod("DAY");
		detectionVariableRepository.save(dv3);

		DetectionVariable dv4 = new DetectionVariable();
		dv4.setDetectionVariableName("MEA4");
		dv4.setDetectionVariableType(dvt1);
		dv4.setDefaultTypicalPeriod("DAY");
		detectionVariableRepository.save(dv4);

		DetectionVariable dv5 = new DetectionVariable();
		dv5.setDetectionVariableName("MEA5");
		dv5.setDetectionVariableType(dvt1);
		dv5.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv5);

		//NUIs
		DetectionVariable dv6 = new DetectionVariable();
		dv6.setDetectionVariableName("NUI1");
		dv6.setDetectionVariableType(dvt2);
		dv6.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv6);

		DetectionVariable dv7 = new DetectionVariable();
		dv7.setDetectionVariableName("NUI2");
		dv7.setDetectionVariableType(dvt2);
		dv7.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv7);

		DetectionVariable dv8 = new DetectionVariable();
		dv8.setDetectionVariableName("NUI3");
		dv8.setDetectionVariableType(dvt2);
		dv8.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv8);

		DetectionVariable dv9 = new DetectionVariable();
		dv9.setDetectionVariableName("NUI4");
		dv9.setDetectionVariableType(dvt2);
		dv9.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv9);

		DetectionVariable dv10 = new DetectionVariable();
		dv10.setDetectionVariableName("NUI5");
		dv10.setDetectionVariableType(dvt2);
		dv10.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv10);

		DetectionVariable dv11 = new DetectionVariable();
		dv11.setDetectionVariableName("NUI6");
		dv11.setDetectionVariableType(dvt2);
		dv11.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv11);

		DetectionVariable dv12 = new DetectionVariable();
		dv12.setDetectionVariableName("NUI7");
		dv12.setDetectionVariableType(dvt2);
		dv12.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv12);

		DetectionVariable dv13 = new DetectionVariable();
		dv13.setDetectionVariableName("NUI8");
		dv13.setDetectionVariableType(dvt2);
		dv13.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv13);

		//GESs
		DetectionVariable dv14 = new DetectionVariable();
		dv14.setDetectionVariableName("GES1");
		dv14.setDetectionVariableType(dvt3);
		dv14.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv14);

		DetectionVariable dv15 = new DetectionVariable();
		dv15.setDetectionVariableName("GES2");
		dv15.setDetectionVariableType(dvt3);
		dv15.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv15);

		//GEFs
		DetectionVariable dv16 = new DetectionVariable();
		dv16.setDetectionVariableName("GEF1");
		dv16.setDetectionVariableType(dvt4);
		dv16.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv16);

		//GFGs
		DetectionVariable dv17 = new DetectionVariable();
		dv17.setDetectionVariableName("GFG1");
		dv17.setDetectionVariableType(dvt5);
		dv17.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv17);

		//OVLs
		DetectionVariable dv18 = new DetectionVariable();
		dv18.setDetectionVariableName("OVL1");
		dv18.setDetectionVariableType(dvt6);
		dv18.setDefaultTypicalPeriod("MON");
		detectionVariableRepository.save(dv18);


		//MEA-NUI
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setDetectionVariable(dv1);
		pdv1.setDerivedDetectionVariable(dv6);
		pdv1.setPilotCode(Pilot.PilotCode.LCC);
		pdv1.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv1.setFormula("avg");
		pilotDetectionVariableRepository.save(pdv1);

		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setDetectionVariable(dv1);
		pdv2.setDerivedDetectionVariable(dv7);
		pdv2.setPilotCode(Pilot.PilotCode.LCC);
		pdv2.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv2.setFormula("std");
		pilotDetectionVariableRepository.save(pdv2);

		PilotDetectionVariable pdv3 = new PilotDetectionVariable();
		pdv3.setDetectionVariable(dv2);
		pdv3.setDerivedDetectionVariable(dv8);
		pdv3.setPilotCode(Pilot.PilotCode.LCC);
		pdv3.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv3.setFormula("avg");
		pilotDetectionVariableRepository.save(pdv3);

		PilotDetectionVariable pdv4 = new PilotDetectionVariable();
		pdv4.setDetectionVariable(dv2);
		pdv4.setDerivedDetectionVariable(dv9);
		pdv4.setPilotCode(Pilot.PilotCode.LCC);
		pdv4.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv4.setFormula("std");
		pilotDetectionVariableRepository.save(pdv4);

		PilotDetectionVariable pdv5 = new PilotDetectionVariable();
		pdv5.setDetectionVariable(dv3);
		pdv5.setDerivedDetectionVariable(dv10);
		pdv5.setPilotCode(Pilot.PilotCode.LCC);
		pdv5.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv5.setFormula("avg");
		pilotDetectionVariableRepository.save(pdv5);

		PilotDetectionVariable pdv6 = new PilotDetectionVariable();
		pdv6.setDetectionVariable(dv3);
		pdv6.setDerivedDetectionVariable(dv11);
		pdv6.setPilotCode(Pilot.PilotCode.LCC);
		pdv6.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv6.setFormula("std");
		pilotDetectionVariableRepository.save(pdv6);

		PilotDetectionVariable pdv7 = new PilotDetectionVariable();
		pdv7.setDetectionVariable(dv4);
		pdv7.setDerivedDetectionVariable(dv12);
		pdv7.setPilotCode(Pilot.PilotCode.LCC);
		pdv7.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv7.setFormula("avg");
		pilotDetectionVariableRepository.save(pdv7);

		PilotDetectionVariable pdv8 = new PilotDetectionVariable();
		pdv8.setDetectionVariable(dv4);
		pdv8.setDerivedDetectionVariable(dv13);
		pdv8.setPilotCode(Pilot.PilotCode.LCC);
		pdv8.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv8.setFormula("std");
		pilotDetectionVariableRepository.save(pdv8);

		//MEA-GES
		PilotDetectionVariable pdv9 = new PilotDetectionVariable();
		pdv9.setDetectionVariable(dv1);
		pdv9.setDerivedDetectionVariable(dv14);
		pdv9.setPilotCode(Pilot.PilotCode.LCC);
		pdv9.setDerivationWeight(BigDecimal.valueOf(0,4));
		pdv9.setFormula("");
		pilotDetectionVariableRepository.save(pdv9);

		PilotDetectionVariable pdv10 = new PilotDetectionVariable();
		pdv10.setDetectionVariable(dv2);
		pdv10.setDerivedDetectionVariable(dv14);
		pdv10.setPilotCode(Pilot.PilotCode.LCC);
		pdv10.setDerivationWeight(BigDecimal.valueOf(0,6));
		pdv10.setFormula("");
		pilotDetectionVariableRepository.save(pdv10);

		PilotDetectionVariable pdv11 = new PilotDetectionVariable();
		pdv11.setDetectionVariable(dv3);
		pdv11.setDerivedDetectionVariable(dv15);
		pdv11.setPilotCode(Pilot.PilotCode.LCC);
		pdv11.setDerivationWeight(BigDecimal.valueOf(0,3));
		pdv11.setFormula("");
		pilotDetectionVariableRepository.save(pdv11);

		PilotDetectionVariable pdv12 = new PilotDetectionVariable();
		pdv12.setDetectionVariable(dv4);
		pdv12.setDerivedDetectionVariable(dv15);
		pdv12.setPilotCode(Pilot.PilotCode.LCC);
		pdv12.setDerivationWeight(BigDecimal.valueOf(0,2));
		pdv12.setFormula("");
		pilotDetectionVariableRepository.save(pdv12);

		PilotDetectionVariable pdv13 = new PilotDetectionVariable();
		pdv13.setDetectionVariable(dv5);
		pdv13.setDerivedDetectionVariable(dv15);
		pdv13.setPilotCode(Pilot.PilotCode.LCC);
		pdv13.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv13.setFormula("");
		pilotDetectionVariableRepository.save(pdv13);

		//NUI-GES
		PilotDetectionVariable pdv14 = new PilotDetectionVariable();
		pdv14.setDetectionVariable(dv6);
		pdv14.setDerivedDetectionVariable(dv14);
		pdv14.setPilotCode(Pilot.PilotCode.LCC);
		pdv14.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv14.setFormula("");
		pilotDetectionVariableRepository.save(pdv14);

		PilotDetectionVariable pdv15 = new PilotDetectionVariable();
		pdv15.setDetectionVariable(dv7);
		pdv15.setDerivedDetectionVariable(dv14);
		pdv15.setPilotCode(Pilot.PilotCode.LCC);
		pdv15.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv15.setFormula("");
		pilotDetectionVariableRepository.save(pdv15);

		PilotDetectionVariable pdv16 = new PilotDetectionVariable();
		pdv16.setDetectionVariable(dv8);
		pdv16.setDerivedDetectionVariable(dv14);
		pdv16.setPilotCode(Pilot.PilotCode.LCC);
		pdv16.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv16.setFormula("");
		pilotDetectionVariableRepository.save(pdv16);

		PilotDetectionVariable pdv17 = new PilotDetectionVariable();
		pdv17.setDetectionVariable(dv9);
		pdv17.setDerivedDetectionVariable(dv14);
		pdv17.setPilotCode(Pilot.PilotCode.LCC);
		pdv17.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv17.setFormula("");
		pilotDetectionVariableRepository.save(pdv17);

		PilotDetectionVariable pdv18 = new PilotDetectionVariable();
		pdv18.setDetectionVariable(dv10);
		pdv18.setDerivedDetectionVariable(dv15);
		pdv18.setPilotCode(Pilot.PilotCode.LCC);
		pdv18.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv18.setFormula("");
		pilotDetectionVariableRepository.save(pdv18);

		PilotDetectionVariable pdv19 = new PilotDetectionVariable();
		pdv19.setDetectionVariable(dv11);
		pdv19.setDerivedDetectionVariable(dv15);
		pdv19.setPilotCode(Pilot.PilotCode.LCC);
		pdv19.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv19.setFormula("");
		pilotDetectionVariableRepository.save(pdv19);

		PilotDetectionVariable pdv20 = new PilotDetectionVariable();
		pdv20.setDetectionVariable(dv12);
		pdv20.setDerivedDetectionVariable(dv15);
		pdv20.setPilotCode(Pilot.PilotCode.LCC);
		pdv20.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv20.setFormula("");
		pilotDetectionVariableRepository.save(pdv20);

		PilotDetectionVariable pdv21 = new PilotDetectionVariable();
		pdv21.setDetectionVariable(dv13);
		pdv21.setDerivedDetectionVariable(dv15);
		pdv21.setPilotCode(Pilot.PilotCode.LCC);
		pdv21.setDerivationWeight(BigDecimal.valueOf(0,5));
		pdv21.setFormula("");
		pilotDetectionVariableRepository.save(pdv21);

		//GES-GEF
		PilotDetectionVariable pdv22 = new PilotDetectionVariable();
		pdv22.setDetectionVariable(dv14);
		pdv22.setDerivedDetectionVariable(dv16);
		pdv22.setPilotCode(Pilot.PilotCode.LCC);
		pdv22.setDerivationWeight(BigDecimal.valueOf(0,3));
		pdv22.setFormula("");
		pilotDetectionVariableRepository.save(pdv22);

		PilotDetectionVariable pdv23 = new PilotDetectionVariable();
		pdv23.setDetectionVariable(dv15);
		pdv23.setDerivedDetectionVariable(dv16);
		pdv23.setPilotCode(Pilot.PilotCode.LCC);
		pdv23.setDerivationWeight(BigDecimal.valueOf(0,7));
		pdv23.setFormula("");
		pilotDetectionVariableRepository.save(pdv23);

		//GEF-GFG
		PilotDetectionVariable pdv24 = new PilotDetectionVariable();
		pdv24.setDetectionVariable(dv16);
		pdv24.setDerivedDetectionVariable(dv17);
		pdv24.setPilotCode(Pilot.PilotCode.LCC);
		pdv24.setDerivationWeight(BigDecimal.valueOf(1));
		pdv24.setFormula("");
		pilotDetectionVariableRepository.save(pdv24);

		//GFG-OVL
		PilotDetectionVariable pdv25 = new PilotDetectionVariable();
		pdv25.setDetectionVariable(dv17);
		pdv25.setDerivedDetectionVariable(dv18);
		pdv25.setPilotCode(Pilot.PilotCode.LCC);
		pdv25.setDerivationWeight(BigDecimal.valueOf(1));
		pdv25.setFormula("");
		pilotDetectionVariableRepository.save(pdv25);

		//VariationMeasureValue
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		vmv1.setUserInRole(uir1);
		vmv1.setMeasureValue(BigDecimal.valueOf(100));
		variationMeasureValueRepository.save(vmv1);

		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setDetectionVariable(dv2);
		vmv2.setTimeInterval(ti2);
		vmv2.setUserInRole(uir1);
		vmv2.setMeasureValue(BigDecimal.valueOf(200));
		variationMeasureValueRepository.save(vmv2);

		VariationMeasureValue vmv3 = new VariationMeasureValue();
		vmv3.setDetectionVariable(dv3);
		vmv3.setTimeInterval(ti3);
		vmv3.setUserInRole(uir1);
		vmv3.setMeasureValue(BigDecimal.valueOf(300));
		variationMeasureValueRepository.save(vmv3);

		VariationMeasureValue vmv4 = new VariationMeasureValue();
		vmv4.setDetectionVariable(dv4);
		vmv4.setTimeInterval(ti4);
		vmv4.setUserInRole(uir1);
		vmv4.setMeasureValue(BigDecimal.valueOf(400));
		variationMeasureValueRepository.save(vmv4);

		VariationMeasureValue vmv5 = new VariationMeasureValue();
		vmv5.setDetectionVariable(dv1);
		vmv5.setTimeInterval(ti5);
		vmv5.setUserInRole(uir1);
		vmv5.setMeasureValue(BigDecimal.valueOf(500));
		variationMeasureValueRepository.save(vmv5);

		VariationMeasureValue vmv6 = new VariationMeasureValue();
		vmv6.setDetectionVariable(dv2);
		vmv6.setTimeInterval(ti6);
		vmv6.setUserInRole(uir1);
		vmv6.setMeasureValue(BigDecimal.valueOf(100));
		variationMeasureValueRepository.save(vmv6);

		VariationMeasureValue vmv7 = new VariationMeasureValue();
		vmv7.setDetectionVariable(dv3);
		vmv7.setTimeInterval(ti7);
		vmv7.setUserInRole(uir1);
		vmv7.setMeasureValue(BigDecimal.valueOf(200));
		variationMeasureValueRepository.save(vmv7);

		VariationMeasureValue vmv8 = new VariationMeasureValue();
		vmv8.setDetectionVariable(dv4);
		vmv8.setTimeInterval(ti8);
		vmv8.setUserInRole(uir1);
		vmv8.setMeasureValue(BigDecimal.valueOf(300));
		variationMeasureValueRepository.save(vmv8);

		VariationMeasureValue vmv9 = new VariationMeasureValue();
		vmv9.setDetectionVariable(dv5);
		vmv9.setTimeInterval(ti9);
		vmv9.setUserInRole(uir1);
		vmv9.setMeasureValue(BigDecimal.valueOf(400));
		variationMeasureValueRepository.save(vmv9);

		VariationMeasureValue vmv10 = new VariationMeasureValue();
		vmv10.setDetectionVariable(dv5);
		vmv10.setTimeInterval(ti10);
		vmv10.setUserInRole(uir1);
		vmv10.setMeasureValue(BigDecimal.valueOf(500));
		variationMeasureValueRepository.save(vmv10);


		//time interval fkeys
		ti1.getVariationMeasureValues().add(vmv1);
		timeIntervalRepository.save(ti1);

		ti2.getVariationMeasureValues().add(vmv2);
		timeIntervalRepository.save(ti2);

		ti3.getVariationMeasureValues().add(vmv3);
		timeIntervalRepository.save(ti3);

		ti4.getVariationMeasureValues().add(vmv4);
		timeIntervalRepository.save(ti4);

		ti5.getVariationMeasureValues().add(vmv5);
		timeIntervalRepository.save(ti5);

		ti6.getVariationMeasureValues().add(vmv6);
		timeIntervalRepository.save(ti6);

		ti7.getVariationMeasureValues().add(vmv7);
		timeIntervalRepository.save(ti7);

		ti8.getVariationMeasureValues().add(vmv8);
		timeIntervalRepository.save(ti8);

		ti9.getVariationMeasureValues().add(vmv9);
		timeIntervalRepository.save(ti9);

		ti10.getVariationMeasureValues().add(vmv10);
		timeIntervalRepository.save(ti10);


		// detection variables fkeys
		/*dv1.getPilotDetectionVariable().add(pdv1);
		detectionVariableRepository.save(dv1);

		dv2.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv2);

		dv3.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv3);

		dv4.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv4);

		dv5.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv5);

		dv6.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv6);

		dv7.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv7);

		dv8.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv8);

		dv9.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv9);

		dv10.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv10);

		dv11.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv11);

		dv12.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv12);

		dv13.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv13);

		dv14.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv14);

		dv15.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv15);

		dv16.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv16);

		dv17.getPilotDetectionVariable().add(pdv7);
		detectionVariableRepository.save(dv17);

		dv18.getPilotDetectionVariable().add(pdv25);
		detectionVariableRepository.save(dv18);*/

		List<Pilot> nevers = pilotRepository.findAllNeverComputed();
		Mockito.when(pilotRepositoryMock.findAllNeverComputed()).thenReturn(nevers);

		List<Pilot> comps = pilotRepository.findAllComputed();
		Mockito.when(pilotRepositoryMock.findAllComputed()).thenReturn(comps);

		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository.findAllForMonthByPilotCodeNui(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC);
		Mockito.when(variationMeasureValueRepositoryMock.findAllForMonthByPilotCodeNui(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC)).thenReturn(vmsMonthly);

		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC.getName());
		Mockito.when(nativeQueryRepositoryMock.computeAllNuis(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC.getName())).thenReturn(nuisList);

		List<Object[]> gess = nativeQueryRepository.computeAllGess(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC.getName());
		Mockito.when(nativeQueryRepositoryMock.computeAllGess(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), Pilot.PilotCode.LCC.getName())).thenReturn(gess);

		List<Object[]> listGef = nativeQueryRepository.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.GEF ,Pilot.PilotCode.LCC.getName());
		Mockito.when(nativeQueryRepositoryMock.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.GEF ,Pilot.PilotCode.LCC.getName())).thenReturn(listGef);

		List<Object[]> listGfg = nativeQueryRepository.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.GFG ,Pilot.PilotCode.LCC.getName());
		Mockito.when(nativeQueryRepositoryMock.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.GFG ,Pilot.PilotCode.LCC.getName())).thenReturn(listGfg);

		List<Object[]> listOvl = nativeQueryRepository.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.OVL ,Pilot.PilotCode.LCC.getName());
		Mockito.when(nativeQueryRepositoryMock.computeAllGfvs(Timestamp.valueOf("2016-01-01 00:00:00"), Timestamp.valueOf("2016-01-31 23:59:59.99999"), DetectionVariableType.OVL ,Pilot.PilotCode.LCC.getName())).thenReturn(listOvl);

		Response response = measuresServiceMock.computeFromMeasures();

		String output = (String) response.getEntity();

		logger.info("output: " + output);

		Assert.assertEquals("success", output);

	}

}
