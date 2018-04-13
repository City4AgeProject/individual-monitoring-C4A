package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.AssessmentRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.json.view.View;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;

public class NativeQueryRepositoryTest {
	
	static protected Logger logger = LogManager.getLogger(NativeQueryRepositoryTest.class);
	
	private static DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy MMM")
			.toFormatter(Locale.ENGLISH);
	
	@Autowired
	private TypicalPeriodRepository typicalPeriodRepository;
	
	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private AssessedGefValuesRepository assessedGefValuesRepository;
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Autowired
	private UserInRoleRepository userInRoleRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Autowired
	NUIRepository nuiRepository;
	
	@Autowired
	PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private MeasuresEndpoint measuresService;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllNui1Value() {
		
		
		Long uirId = 1L;
		Long dvId = 1L;
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));
		
		Pilot p = new Pilot();
		p.setPilotCode(Pilot.PilotCode.LCC);
		pilotRepository.save(p);
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		detectionVariableRepository.save(dv1);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1); 
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setUserInRole(uir1);
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		vmv1.setMeasureValue(new BigDecimal(5));
		variationMeasureValueRepository.save(vmv1);
	
		List<Pilot.PilotCode> pilotCodes = Arrays.asList(Pilot.PilotCode.LCC);
		List<String> pilots = new ArrayList<>();
		for (PilotCode pilot : pilotCodes) pilots.add(pilot.getName());
		List<Object[]> result = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, pilots.get(0));

		
		//Assert.assertNotNull(result);
		
		/*Assert.assertEquals(new BigDecimal(5.0), result.getAvg());
		Assert.assertEquals(new BigDecimal(.0), result.getStDev());
		Assert.assertEquals(new BigDecimal(.0), result.getStd());
		Assert.assertEquals(new BigDecimal(5.0), result.getBest25());
		Assert.assertEquals(new BigDecimal(1.0), result.getBest());
		Assert.assertEquals(new BigDecimal(0.0), result.getDelta());*/
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllNui2Values() {
		
		
		Long uirId = 1L;
		Long dvId = 1L;
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));
		
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(uirId);
		userInRoleRepository.save(uir1);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(dvId);
		detectionVariableRepository.save(dv1);
		
		TimeInterval ti1 = new TimeInterval();
		ti1.setId(1L);
		ti1.setIntervalStart(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1.setIntervalEnd(Timestamp.valueOf("2017-05-03 00:00:00"));
		ti1 = timeIntervalRepository.save(ti1); 
		
		VariationMeasureValue vmv1 = new VariationMeasureValue();
		vmv1.setId(1L);
		vmv1.setUserInRole(uir1);
		vmv1.setDetectionVariable(dv1);
		vmv1.setTimeInterval(ti1);
		vmv1.setMeasureValue(new BigDecimal(5));
		variationMeasureValueRepository.save(vmv1);
		
		VariationMeasureValue vmv2 = new VariationMeasureValue();
		vmv2.setId(2L);
		vmv2.setUserInRole(uir1);
		vmv2.setDetectionVariable(dv1);
		vmv2.setTimeInterval(ti1);
		vmv2.setMeasureValue(new BigDecimal(4));
		variationMeasureValueRepository.save(vmv2);

		List<Object[]> result = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, null);

		
		//Assert.assertNotNull(result);
		
		/*Assert.assertEquals(new BigDecimal(4.5), result.getAvg());
		Assert.assertEquals(new BigDecimal(.70710678118654757).setScale(2, RoundingMode.HALF_UP), result.getStDev().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(.15713484026367724).setScale(2, RoundingMode.HALF_UP), result.getStd().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(4.75), result.getBest25());
		Assert.assertEquals(new BigDecimal(1.0555555555555556).setScale(2, RoundingMode.HALF_UP), result.getBest().setScale(2, RoundingMode.HALF_UP));
		Assert.assertEquals(new BigDecimal(0.05555555555555555).setScale(2, RoundingMode.HALF_UP), result.getDelta().setScale(2, RoundingMode.HALF_UP));*/
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllGess() {
		
		Timestamp startOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atDay(1).atStartOfDay());
		Timestamp endOfMonth = Timestamp.valueOf(YearMonth.parse("2017 MAY", formatter).atEndOfMonth().atTime(LocalTime.MAX));
		
		Timestamp intervalStart1 = Timestamp.valueOf(YearMonth.of(2017, 1).atDay(1).atStartOfDay());

		TimeInterval ti1 = measuresService.getOrCreateTimeInterval(intervalStart1, TypicalPeriod.MONTH);

		Timestamp intervalStart2 = Timestamp.valueOf(YearMonth.of(2017, 2).atDay(1).atStartOfDay());

		TimeInterval ti2 = measuresService.getOrCreateTimeInterval(intervalStart2, TypicalPeriod.MONTH);
		
		UserInRole uir1 = new UserInRole();
		uir1.setId(1L);
		uir1.setPilotCode(Pilot.PilotCode.LCC);
		userInRoleRepository.save(uir1);
		
		UserInRole uir2 = new UserInRole ();
		uir2.setId(2L);
		uir2.setPilotCode(Pilot.PilotCode.LCC);
		uir2 = userInRoleRepository.save(uir2);
		
		UserInRole uir3 = new UserInRole ();
		uir3.setId(3L);
		uir3.setPilotCode(Pilot.PilotCode.ATH);
		uir3 = userInRoleRepository.save(uir3);
		
		DetectionVariableType dvt1 = DetectionVariableType.GES;
		dvt1 = detectionVariableTypeRepository.save(dvt1);

		DetectionVariableType dvt2 = DetectionVariableType.NUI;
		dvt2 = detectionVariableTypeRepository.save(dvt2);
		
		DetectionVariable dv1 = new DetectionVariable();
		dv1.setId(1L);
		dv1.setDetectionVariableName("Ges");
		dv1.setDetectionVariableType(dvt1);
		dv1 = detectionVariableRepository.save(dv1);

		DetectionVariable dv2 = new DetectionVariable();
		dv2.setId(2L);
		dv2.setDetectionVariableName("Nui");
		dv2.setDetectionVariableType(dvt2);
		dv2 = detectionVariableRepository.save(dv2);
		
		NumericIndicatorValue nui1 = new NumericIndicatorValue();
		nui1.setId(1L);
		nui1.setNuiValue(new Double(0));
		nui1.setDetectionVariable(dv2);
		nui1.setUserInRole(uir1);
		nui1.setTimeInterval(ti1);
		nui1 = nuiRepository.save(nui1);

		NumericIndicatorValue nui2 = new NumericIndicatorValue();
		nui2.setId(2L);
		nui2.setNuiValue(new Double(1));
		nui2.setDetectionVariable(dv2);
		nui2.setUserInRole(uir1);
		nui2.setTimeInterval(ti2);
		nui2 = nuiRepository.save(nui2);
		
		NumericIndicatorValue nui3 = new NumericIndicatorValue();
		nui3.setId(3L);
		nui3.setNuiValue(new Double(2));
		nui3.setDetectionVariable(dv2);
		nui3.setUserInRole(uir2);
		nui3.setTimeInterval(ti1);
		nui3 = nuiRepository.save(nui3);
		
		NumericIndicatorValue nui4 = new NumericIndicatorValue();
		nui4.setId(4L);
		nui4.setNuiValue(new Double(3));
		nui4.setDetectionVariable(dv2);
		nui4.setUserInRole(uir3);
		nui4.setTimeInterval(ti2);
		nui4 = nuiRepository.save(nui4);
		
		NumericIndicatorValue nui5 = new NumericIndicatorValue();
		nui5.setId(5L);
		nui5.setNuiValue(new Double(4));
		nui5.setDetectionVariable(dv1);
		nui5.setUserInRole(uir1);
		nui5.setTimeInterval(ti2);
		nui5 = nuiRepository.save(nui5);
				
		PilotDetectionVariable pdv1 = new PilotDetectionVariable();
		pdv1.setId(1L);
		pdv1.setDetectionVariable(dv2);
		pdv1.setDerivedDetectionVariable(dv1);
		pdv1 = pilotDetectionVariableRepository.save(pdv1);
		
		PilotDetectionVariable pdv2 = new PilotDetectionVariable();
		pdv2.setId(2L);
		pdv2.setDetectionVariable(dv1);
		pdv2.setDerivedDetectionVariable(dv2);
		pdv2 = pilotDetectionVariableRepository.save(pdv2);
		
		//List<Gfvs> result = nuiRepository.doAllGess(startOfMonth, endOfMonth);
		
		//Assert.assertNotNull(result);
		
		//Assert.assertEquals(5, result.size());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDoAllGfvs() {
		
		Timestamp startOfMonth = Timestamp.valueOf("2017-08-01 00:00:00");
		Timestamp endOfMonth = Timestamp.valueOf("2017-09-01 23:59:59.999999999");
		
		DetectionVariableType derivedDetectionVariableType = DetectionVariableType.GEF;
		//detectionVariableTypeRepository.save(derivedDetectionVariableType);

		List<Object[]> result = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, derivedDetectionVariableType, null);
		
		//Assert.assertNotNull(result);
		
		//Assert.assertEquals(0, result.size());
		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetLast5AssessmentsForDiagramTimeline() throws Exception {

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

		List<Object[]> list = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(uir1.getId(), ddv1.getId(), start, end); // added
																										// 1L
																										// for
																										// parentDetectionVariableId

		Assert.assertNotNull(list);

		System.out.println(list.size() + "::WWW");
		int i = 1;
		for (Iterator <Object[]> l5a = list.iterator(); l5a.hasNext();) {
			Object[] curr = l5a.next();
			logger.info(i + " intervalID: " + curr[0]);
			logger.info(i + " intervalStart: " + curr[1]);
			logger.info(i + " gefID: " + curr[2]);	
			logger.info(i + " gefValue: " + curr[3]);
			logger.info(i + " assessmentID: " + curr[4]);
			logger.info(i + " assessmentComment: " + curr[5]);
			logger.info(i + " riskStatus: " + curr[6]);
			logger.info(i + " validity: " + curr[7]);
			logger.info(i + " created: " + curr[8]);
			i++;
		}
		Assert.assertEquals(6, list.size());

		Assert.assertEquals(Character.valueOf('A'), list.get(0)[6]);
		Assert.assertEquals("2016-01-01 00:00:00.0", list.get(0)[1]); // ti

		Assert.assertEquals("2017-05-22 12:00:00", list.get(0)[8]);
		
		list = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(uir2.getId(), ddv1.getId(), start, end);
		
		i = 1;
		for (Iterator <Object[]> l5a = list.iterator(); l5a.hasNext();) {
			Object[] curr = l5a.next();
			logger.info(i + " intervalID: " + curr[0]);
			logger.info(i + " intervalStart: " + curr[1]);
			logger.info(i + " gefID: " + curr[2]);	
			logger.info(i + " gefValue: " + curr[3]);
			logger.info(i + " assessmentID: " + curr[4]);
			logger.info(i + " assessmentComment: " + curr[5]);
			logger.info(i + " riskStatus: " + curr[6]);
			logger.info(i + " validity: " + curr[7]);
			logger.info(i + " created: " + curr[8]);
			i++;
		}
		
		Assert.assertEquals(6, list.size());
		
		start = Timestamp.valueOf("2017-01-01 00:00:00");
		end = Timestamp.valueOf("2018-01-01 00:00:00");
		
		list = nativeQueryRepository.getLast5AssessmentsForDiagramTimeline(uir2.getId(), ddv1.getId(), start, end);
		
		logger.info("LISTSIZE: " + list.size());

		ObjectMapper objectMapper = new ObjectMapper();

		Hibernate5Module hbm = new Hibernate5Module();

		objectMapper.registerModule(hbm);

		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		String result = objectMapper.writerWithView(View.TimeIntervalView.class).writeValueAsString(list);

		Assert.assertNotNull(result);

		logger.info("end of testFindLastFiveAssessmentsForDiagram");

	}

}
