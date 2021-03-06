package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.jpa.AssessedGefValuesRepository;
import eu.city4age.dashboard.api.jpa.DerivedMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet.AssessedGefValueSetBuilder;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
import eu.city4age.dashboard.api.service.ComputeService;
import eu.city4age.dashboard.api.service.ExcludeService;
import eu.city4age.dashboard.api.service.MeasuresService;

@Component
public class MeasuresServiceImpl implements MeasuresService {
	
	static protected Logger logger = LogManager.getLogger(MeasuresServiceImpl.class);
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private VariationMeasureValueRepository variationMeasureValueRepository;
	
	@Autowired
	private NUIRepository nuiRepository;
	
	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;
	
	@Autowired
	private DerivedMeasureValueRepository derivedMeasureValueRepository;
	
	@Autowired
	private NativeQueryRepository nativeQueryRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	@Autowired
	private AssessedGefValuesRepository assessmentGFVRepository;
	
	@Autowired
	private ExcludeService excludeService;
	
	@Autowired
	private ComputeService computeService;
	
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeFor1Pilot(Pilot pilot) throws Exception {
		
		PilotCode pilotCode = pilot.getPilotCode();

		logger.info("pilotCode: " + pilotCode.name());

		Date latestDerivedMeasuresComputed = pilot.getLatestVariablesComputed();
		Date newestSubmittedData = pilot.getNewestSubmittedData();

		YearMonth startOfComputationYearMonth;

		if (latestDerivedMeasuresComputed == null) {
			startOfComputationYearMonth = 
					YearMonth.from(variationMeasureValueRepository.findFirstMonthForPilot(pilot.getPilotCode(), pilot.getCompZone()).toInstant().atZone(ZoneId.of(pilot.getCompZone())).toLocalDate());
		} else {
			startOfComputationYearMonth = 
					YearMonth.from(pilotRepository.findNextMonthForPilot(pilot.getPilotCode()).toInstant().atZone(ZoneId.of(pilot.getCompZone())).toLocalDate());
		}

		YearMonth endOfComputationYearMonth = YearMonth.from(YearMonth.from(newestSubmittedData.toInstant().atZone(ZoneId.of(pilot.getCompZone())).toLocalDate()));

		Timestamp startOfMonth;
		Timestamp endOfMonth;

		if (endOfComputationYearMonth.isBefore(YearMonth.now())) {
			endOfComputationYearMonth = endOfComputationYearMonth.plusMonths(1L);
			if (pilot.getLatestVariablesComputed() != null) startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}
		
		List<NumericIndicatorValue> nuiList = new ArrayList<NumericIndicatorValue> ();
		List<DerivedMeasureValue> dmList = new ArrayList<DerivedMeasureValue> ();
		List<GeriatricFactorValue> gfvList = new ArrayList<GeriatricFactorValue> ();

		while(!startOfComputationYearMonth.equals(endOfComputationYearMonth)) {

			startOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atDay(1).atStartOfDay());
			endOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atEndOfMonth().atTime(LocalTime.MAX));

			logger.info(startOfMonth);

			try {
				computeService.computeAllFor1Month (startOfMonth, endOfMonth, pilotCode, nuiList, dmList, gfvList);
			}
			catch (Exception e) {
				e.printStackTrace();
				break;
			}
			startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}

		Timestamp endOfComputation = Timestamp.valueOf(startOfComputationYearMonth.minusMonths(1L).atDay(1).atStartOfDay());
		setVariablesComputedForAllPilots(pilot, endOfComputation, newestSubmittedData);
	}	

	public void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<NumericIndicatorValue> nuiList) {

		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth, pilotCode);
		
		//logger.info("vms size: " + vmsMonthly.size());

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			excludeService.excludeMeasures(vmsMonthly);
			nuiList = createAllNuis(startOfMonth, endOfMonth, pilotCode, nuiList);
			nuiRepository.bulkSave(nuiList);
			nuiRepository.flush();
			nuiList.clear();
			//nuiRepository.flush();
		}
		vmsMonthly.clear();
	}
	
	public void computeDmsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<DerivedMeasureValue> dmList) {
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> derivedMeasures = nativeQueryRepository.computeAllDerivedMeasures(startOfMonth, endOfMonth, stringPilotCode);
		
		if (derivedMeasures != null && !derivedMeasures.isEmpty()) {
			dmList = createAllDms(derivedMeasures, startOfMonth, endOfMonth, pilotCode, dmList);
			derivedMeasureValueRepository.bulkSave(dmList);
			derivedMeasureValueRepository.flush();
			dmList.clear();
		}
		derivedMeasures.clear();
	}

	public void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, List<GeriatricFactorValue> gesList) throws Exception {

		String stringPilotCode = pilotCode.name().toLowerCase();
		
		//ROLLBACK SCENARIO!!!
		//if (startOfMonth.equals(Timestamp.valueOf("2017-06-01 00:00:00.0")) && pilotCode == Pilot.PilotCode.ATH) throw new Exception();
		
		List<Object[]> gess = nativeQueryRepository.computeAllGess(startOfMonth, endOfMonth, stringPilotCode);
		//gess.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GES));
		
		if(gess != null && gess.size() > 0) {
			gesList = createAllGFVs(gess, startOfMonth, endOfMonth, pilotCode, gesList);
			geriatricFactorRepository.bulkSave(gesList);
			geriatricFactorRepository.flush();
			gesList.clear();
			//nuiRepository.flush();
		}
		gess.clear();

	}

	public void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode, List<GeriatricFactorValue> gfvList) throws Exception {
		
		/*if(factor.equals(DetectionVariableType.OVL) && pilotCode.equals(PilotCode.MAD) && startOfMonth.after(Timestamp.valueOf("2017-09-01 00:00:00.0"))) {
			logger.info("Desilo se.");
			throw new Exception("Desilo se.");
		}*/
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> list = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, factor, stringPilotCode);

		/*if(factor.equals(DetectionVariableType.GEF))
		list.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GEF));*/

		if (list != null && list.size() > 0) {
			gfvList = createAllGFVs(list, startOfMonth, endOfMonth, pilotCode, gfvList);
			geriatricFactorRepository.bulkSave(gfvList);
			geriatricFactorRepository.flush();
			gfvList.clear();
			//nuiRepository.flush();
		}
		list.clear();
	}
	
	public void setVariablesComputedForAllPilots(Pilot pilot, Timestamp endOfComputation, Date newestSubmittedData) {
		
		pilot.setLatestVariablesComputed(endOfComputation);
		pilot.setTimeOfComputation(new Date());
		pilot.setNewestSubmittedData(newestSubmittedData);
		pilotRepository.save(pilot);
	}
	
	public List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode, 
			List<NumericIndicatorValue> nuiList) {
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, stringPilotCode);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {

			for (Object[] nui:nuisList) {

				NumericIndicatorValue create1Nui = create1Nui((Integer) nui[0],  (Integer) nui[1], (Double) nui[2], startOfMonth, pilotCode);

				nuiList.add(create1Nui);

			}

		}
		nuisList.clear();
		
		return nuiList;

	}
	
	private List<DerivedMeasureValue> createAllDms(List<Object[]> derivedMeasures, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode, List<DerivedMeasureValue> dmList) {
		
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode);
		
		for (Object[] derivedMeasure : derivedMeasures) {
			DerivedMeasureValue dmv = new DerivedMeasureValue();
			dmv.setUserInRoleId(((Integer) derivedMeasure[0]).longValue());
			dmv.setDetectionVariableId(((Integer) derivedMeasure[1]).longValue());
			dmv.setDerivedDetectionVariableId(((Integer) derivedMeasure[2]).longValue());
			dmv.setTimeInterval(ti);
			dmv.setDmValue((BigDecimal) derivedMeasure[5]);
			dmList.add(dmv);
		}
		
		return dmList;
	}
	
	public List<GeriatricFactorValue> createAllGFVs(List<Object[]> list, Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode,
			List<GeriatricFactorValue> gfvList) {
				
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode);
		
		for(Object[] arr : list) {
			GeriatricFactorValue ges = new GeriatricFactorValue();
			ges.setGefValue((BigDecimal) arr[2]);
			ges.setTimeInterval(ti);
			ges.setDetectionVariableId(((Integer) arr[1]).longValue());
			ges.setUserInRoleId(((Integer) arr[0]).longValue());
			ges.setDerivationWeight(arr[3] == null ? new BigDecimal(1) : (BigDecimal) arr[3]);
			gfvList.add(ges);
		}
		return gfvList;
	}
	
	public NumericIndicatorValue create1Nui(Integer userId, Integer nuiDvId, Double nuiValue, Timestamp startOfMonth, PilotCode pilotCode) {
		
		NumericIndicatorValue nui = new NumericIndicatorValue();
		if (nuiValue != null) {
			nui.setNuiValue(nuiValue);
		} else {
			nui.setNuiValue(new Double(0));
		}
		nui.setUserInRoleId(userId);
		nui.setDetectionVariableId(nuiDvId);
		nui.setTimeInterval(getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode));

		return nui;
	}
	
	public TimeInterval getOrCreateTimeIntervalPilotTimeZone(Date intervalStart, TypicalPeriod typicalPeriod, PilotCode pilotCode) {
		
		String computationTimeZone = pilotRepository.findByPilotCode(pilotCode).getCompZone();
		//System.err.println("trt: " + computationTimeZone);
		TimeZone.setDefault(TimeZone.getTimeZone(computationTimeZone));
		TimeInterval ti = timeIntervalRepository.findByIntervalStartAndTypicalPeriod(intervalStart,
				typicalPeriod.getDbName());
		if (ti == null) {
			//System.err.println("null");
			ti = new TimeInterval();
			ti.setIntervalStart(intervalStart);
			ti.setTypicalPeriod(typicalPeriod.getDbName());
			ti.setCreated(new Timestamp((new Date().getTime())));
			timeIntervalRepository.save(ti);
			//timeIntervalRepository.flush();
		} else {
			//System.err.println("not null");
		}
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		return ti;
	} 
	
	public TimeInterval getOrCreateTimeInterval(Date intervalStart, TypicalPeriod typicalPeriod) {
		
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		TimeInterval ti = timeIntervalRepository.findByIntervalStartAndTypicalPeriod(intervalStart,
				typicalPeriod.getDbName());
		if (ti == null) {
			ti = new TimeInterval();
			ti.setIntervalStart(intervalStart);
			ti.setTypicalPeriod(typicalPeriod.getDbName());
			ti.setCreated(new Timestamp((new Date().getTime())));
			timeIntervalRepository.save(ti);
			//timeIntervalRepository.flush();
		}
		return ti;
	}
	
	public int determineTimeInterval (long start, long end, long differentiator) {
		
		if ((differentiator - start) >= (end - differentiator)) return 0;
		else return 1;
		
	}

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeFor1User(UserInRole uir, Timestamp firstMonth) {
		
		YearMonth startOfComputationYearMonth = YearMonth.from (firstMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		YearMonth endOfComputationYearMonth;
		if (uir.getPilot().getLatestVariablesComputed() != null ) endOfComputationYearMonth = YearMonth.from(YearMonth.from(uir.getPilot().getLatestVariablesComputed().toInstant().atZone(ZoneId.of("Z")).toLocalDate()));
		else return;
		
		endOfComputationYearMonth = endOfComputationYearMonth.plusMonths(1l);
		
		logger.info("firstMonth: " + firstMonth);
		
		logger.info("ZoneId.systemDefault(): " + ZoneId.systemDefault());
		
		logger.info("yearMonth start: " + startOfComputationYearMonth);
		
		logger.info("yearMonth end: " + endOfComputationYearMonth);
		
		Timestamp startOfMonth;
		Timestamp endOfMonth;
		
		while(!startOfComputationYearMonth.equals(endOfComputationYearMonth)) {
			
			startOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atDay(1).atStartOfDay());
			endOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atEndOfMonth().atTime(LocalTime.MAX));
			
			try {
				computeService.computeAllFor1UserFor1Month (startOfMonth, endOfMonth, uir);
			}
			catch (Exception e) {
				//startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
				break;
			}
			
			startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}
		
	}

	public void computeFor1MonthFor1User(DetectionVariableType gef, Timestamp startOfMonth, Timestamp endOfMonth,
			UserInRole uir) {
		
		
		List<GeriatricFactorValue> gfvs = createGFVsFor1User(startOfMonth, endOfMonth, gef, uir);
		
		List<GeriatricFactorValue> existingGFVs = geriatricFactorRepository.findGFVForUserAndStartAndEndAndGFVType (startOfMonth, endOfMonth, uir, gef);
		
		Map<GeriatricFactorValue, GeriatricFactorValue> gfvMap = new HashMap<GeriatricFactorValue, GeriatricFactorValue> ();
		Map<GeriatricFactorValue, List<Long>> gfvAssessmentMap = new HashMap<GeriatricFactorValue, List<Long>> ();
		
		for (GeriatricFactorValue gfv : existingGFVs) {
			//logger.info("existingUser: " + gfv.getUserInRoleId() + " existingGesType: " + gfv.getDetectionVariableId() + " existingTimeIntervalID: " + gfv.getTimeInterval().getId() + " existingTimeInterval: " + gfv.getTimeInterval().getStart() +
				//	" existingValue: " + gfv.getGefValue() + " existingWeight: " + gfv.getDerivationWeight());
			for (GeriatricFactorValue newGfv : gfvs) {
				if (gfv.getDetectionVariableId().equals(newGfv.getDetectionVariableId()) &&
						gfv.getUserInRoleId().equals(newGfv.getUserInRoleId()) &&
						gfv.getTimeInterval().getId().equals(newGfv.getTimeInterval().getId())) {
					gfvMap.put(gfv, newGfv);
					break;
				}
			}
			List<AssessedGefValueSet> list = assessmentGFVRepository.findByGefValueId (gfv.getId().intValue());
			//logger.info("list.size: " + list.size());
			if (list != null && !list.isEmpty()) {
				List<Long> assessmentIDs = new ArrayList<Long> ();
				for ( AssessedGefValueSet l : list)
					assessmentIDs.add(l.getAssessmentId().longValue());
				gfvAssessmentMap.put(gfv, assessmentIDs);
			}
		}
		
		geriatricFactorRepository.delete(existingGFVs);
		geriatricFactorRepository.flush();
		geriatricFactorRepository.save(gfvs);
		
		for (GeriatricFactorValue gfv : gfvAssessmentMap.keySet()) {
			logger.info("old value: " + gfv.getGefValue());
			logger.info("new value: " + gfvMap.get(gfv).getGefValue());
			for (Long assessments: gfvAssessmentMap.get(gfv)) {
				logger.info("assessmentId: " + assessments);
				assessmentGFVRepository.save(new AssessedGefValueSet(new AssessedGefValueSetBuilder ().assessmentId(assessments.intValue())
						.gefValueId(gfvMap.get(gfv).getId().intValue())));
			}
		}
		
		/*gfvs.clear();
		existingGFVs.clear();
		gfvMap.clear();
		gfvAssessmentMap.clear();*/
	}

	private List<GeriatricFactorValue> createGFVsFor1User(Timestamp startOfMonth, Timestamp endOfMonth,
			DetectionVariableType gef, UserInRole uir) {
		
		final List<GeriatricFactorValue> gfvs = new ArrayList<GeriatricFactorValue>();
		
		List<Object[]> gfvsList = nativeQueryRepository.computeGfvsFor1User(startOfMonth, endOfMonth, uir.getId(), gef);
		
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, uir.getPilotCode());
		
		if (gfvsList != null) logger.info ("gessList.size: " + gfvsList.size());
		
		if(!gfvsList.isEmpty()) {
			
			for (Object[] gfv : gfvsList) {
				
				GeriatricFactorValue create1Ges = new GeriatricFactorValue();
				
				create1Ges.setDerivationWeight(gfv[3] == null ? new BigDecimal(1) : (BigDecimal) gfv [3]);
				create1Ges.setDetectionVariableId(((Integer) gfv[1]).longValue());
				create1Ges.setGefValue((BigDecimal) gfv[2]);
				create1Ges.setTimeInterval(ti);
				create1Ges.setUserInRoleId(((Integer) gfv[0]).longValue());
				
				logger.info("uir: " + gfv[0] + " ges_type: " + gfv[1] + " value: " + gfv[2] + " weight: " + gfv[3]);
				
				gfvs.add(create1Ges);
			}
		}
		return gfvs;
	}

	public void computeGessFor1User(Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir) {
		
		List<GeriatricFactorValue> gess = createGessFor1User(startOfMonth, endOfMonth, uir);
		
		List<GeriatricFactorValue> existingGess = geriatricFactorRepository.findGFVForUserAndStartAndEndAndGFVType (startOfMonth, endOfMonth, uir, DetectionVariableType.GES);
		
		/*List<Integer> updatedGFVsIDs = new ArrayList<Integer> ();
		List<String> updatedGFVValues = new ArrayList<String> ();
		List<Integer> removedGFVsIDs = new ArrayList<Integer> ();
		List<String> removedGFVValues = new ArrayList<String> ();
		List<GeriatricFactorValue> gfvsForDelete = new ArrayList <GeriatricFactorValue> ();*/
		
		logger.info("existingGess.size: " + existingGess.size()); 
		
		//int gessCounter = 0;
		
		/*for (GeriatricFactorValue ges : existingGess) {
			logger.info("existingUser: " + ges.getUserInRoleId() + " existingGesType: " + ges.getDetectionVariableId() + " existingTimeIntervalID: " + ges.getTimeInterval().getId() + " existingTimeInterval: " + ges.getTimeInterval().getStart() +
					" existingValue: " + ges.getGefValue() + " existingWeight: " + ges.getDerivationWeight());
		}
		*/
		/*logger.info("gess.size: " + gess.size());
		for (GeriatricFactorValue ges : gess) {
			logger.info("User: " + ges.getUserInRoleId() + " GesType: " + ges.getDetectionVariableId() + " TimeIntervalID: " + ges.getTimeInterval().getId() + " TimeInterval: " + ges.getTimeInterval().getStart() +
					" Value: " + ges.getGefValue() + " Weight: " + ges.getDerivationWeight());
		} */
		
		Map<GeriatricFactorValue, GeriatricFactorValue> gfvMap = new HashMap<GeriatricFactorValue, GeriatricFactorValue> ();
		Map<GeriatricFactorValue, List<Long>> gfvAssessmentMap = new HashMap<GeriatricFactorValue, List<Long>> ();
		
		for (GeriatricFactorValue gfv : existingGess) {
			//logger.info("existingUser: " + gfv.getUserInRoleId() + " existingGesType: " + gfv.getDetectionVariableId() + " existingTimeIntervalID: " + gfv.getTimeInterval().getId() + " existingTimeInterval: " + gfv.getTimeInterval().getStart() +
				//	" existingValue: " + gfv.getGefValue() + " existingWeight: " + gfv.getDerivationWeight());
			for (GeriatricFactorValue newGfv : gess) {
				if (gfv.getDetectionVariableId().equals(newGfv.getDetectionVariableId()) &&
						gfv.getUserInRoleId().equals(newGfv.getUserInRoleId()) &&
						gfv.getTimeInterval().getId().equals(newGfv.getTimeInterval().getId())) {
					gfvMap.put(gfv, newGfv);
					break;
				}
			}
			List<AssessedGefValueSet> list = assessmentGFVRepository.findByGefValueId (gfv.getId().intValue());
			//logger.info("list.size: " + list.size());
			if (list != null && !list.isEmpty()) {
				List<Long> assessmentIDs = new ArrayList<Long> ();
				for ( AssessedGefValueSet l : list)
					assessmentIDs.add(l.getAssessmentId().longValue());
				gfvAssessmentMap.put(gfv, assessmentIDs);
			}
		}
		
		logger.info("geriatricFactorRepository.size: " + geriatricFactorRepository.findAll().size());
		geriatricFactorRepository.delete(existingGess);
		geriatricFactorRepository.flush();
		logger.info("geriatricFactorRepository.size: " + geriatricFactorRepository.findAll().size());
		geriatricFactorRepository.save(gess);
		
		for (GeriatricFactorValue gfv : gfvAssessmentMap.keySet()) {
			logger.info("old value: " + gfv.getGefValue());
			logger.info("new value: " + gfvMap.get(gfv).getGefValue());
			for (Long assessments: gfvAssessmentMap.get(gfv)) {
				logger.info("assessmentId: " + assessments);
				assessmentGFVRepository.save(new AssessedGefValueSet(new AssessedGefValueSetBuilder ().assessmentId(assessments.intValue())
						.gefValueId(gfvMap.get(gfv).getId().intValue())));
			}
		}
		
		/*gess.clear();
		existingGess.clear();
		gfvMap.clear();
		gfvAssessmentMap.clear();*/
		
		//logger.info("gfvMap.size: " + gfvMap.size());
		//logger.info("gfvAssessmentMap.size: " + gfvAssessmentMap.size());
		
		/*for (GeriatricFactorValue gfv : gfvMap.keySet()) {
			logger.info("existingUser: " + gfv.getUserInRoleId() + " existingGesType: " + gfv.getDetectionVariableId() + " existingTimeIntervalID: " + gfv.getTimeInterval().getId() + " existingTimeInterval: " + gfv.getTimeInterval().getStart() +
					" existingValue: " + gfv.getGefValue() + " existingWeight: " + gfv.getDerivationWeight());
			logger.info("corespondingUser: " + gfvMap.get(gfv).getUserInRoleId() + " coresspondingGesType: " + gfvMap.get(gfv).getDetectionVariableId() + " coresspondingTimeIntervalID: " + gfvMap.get(gfv).getTimeInterval().getId() + " coresspondingTimeInterval: " + gfvMap.get(gfv).getTimeInterval().getStart() +
					" coresspondingValue: " + gfvMap.get(gfv).getGefValue() + " coresspondingWeight: " + gfvMap.get(gfv).getDerivationWeight());
		}*/
		
		/*for (GeriatricFactorValue gfv : gfvAssessmentMap.keySet()) {
			logger.info("gfv.id: " + gfv.getId() + " old value: " + gfv.getGefValue());
			logger.info("new value: " + gfvMap.get(gfv).getGefValue());
			for (Long assessments: gfvAssessmentMap.get(gfv))
				logger.info("assessmentId: " + assessments);
		}*/
		
		/*for (int i = 0; i < existingGess.size(); i++) {
			if (existingGess.get(i).getDetectionVariableId().equals(gess.get(gessCounter).getDetectionVariableId())) {
				
				logger.info("MATCH BY ID");
				
				if (existingGess.get(i).getGefValue().compareTo(gess.get(gessCounter).getGefValue().setScale(2, RoundingMode.HALF_UP)) != 0) {
					logger.info("DIFFERENT GES VALUES:  previous: " + existingGess.get(i).getGefValue() + " new: " + gess.get(gessCounter).getGefValue().setScale(2, RoundingMode.HALF_UP));
					updatedGFVsIDs.add(existingGess.get(i).getId().intValue());
					updatedGFVValues.add("\r\n" + "GeriatricSubfactor: " + existingGess.get(i).getDetectionVariable().getDetectionVariableName() + 
							" for time period: " + LocalDate.parse(existingGess.get(i).getTimeInterval().getStart().substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)) + " changed; previous value: " +
							existingGess.get(i).getGefValue() + ", new value: " + gess.get(gessCounter).getGefValue().setScale(2, RoundingMode.HALF_UP) + "\n");
					existingGess.get(i).setGefValue(gess.get(gessCounter).getGefValue().setScale(2, RoundingMode.HALF_UP));
				}
				
				else {
					logger.info("SAME GES VALUES:  previous: " + existingGess.get(i).getGefValue() + " new: " + gess.get(gessCounter).getGefValue().setScale(2, RoundingMode.HALF_UP));
				}
				
				gessCounter++;
			}
			else {
				
				logger.info("NO MATCH BY ID");
				
				removedGFVsIDs.add(existingGess.get(i).getId().intValue());
				removedGFVValues.add("\r\n" + "GeriatricSubfactor: " + existingGess.get(i).getDetectionVariable().getDetectionVariableName() + 
						" for time period: " + LocalDate.parse(existingGess.get(i).getTimeInterval().getStart().substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)) + " removed; previous value: " +
						existingGess.get(i).getGefValue());
				gfvsForDelete.add(existingGess.get(i));
			}
		}
		
		int assessmentCounter = 0;
		
		List<AssessedGefValueSet> list1 = assessmentGFVRepository.findByGefIDs (updatedGFVsIDs);
		
		logger.info("size: " + list1.size());
		
		for (Integer gfvID : updatedGFVsIDs) {
			List<AssessedGefValueSet> list = assessmentGFVRepository.findByGefValueId (gfvID);
			
			logger.info("gfvID: " + gfvID);
			
			for (AssessedGefValueSet l : list) {
				logger.info("assessment : " + l.getAssessmentId());
				logger.info("gfv : " + l.getGefValueId());
				
				Assessment assessmentForUpdate = assessmentRepository.findOne(l.getAssessmentId().longValue());
				assessmentForUpdate.setAssessmentComment(assessmentForUpdate.getAssessmentComment() + updatedGFVValues.get(assessmentCounter));
				assessmentRepository.save(assessmentForUpdate);
			}
			
			assessmentCounter++;
		} */
		//List<Integer> updatedAssessmentList = assessmentGFVRepository.findAssessmentsByGeriatricFactors (updatedGFVsIDs);
		//List<Integer> removedAssessmentList = assessmentGFVRepository.findAssessmentsByGeriatricFactors (removedGFVsIDs);
		
		//logger.info("updated assessments.size: " + updatedAssessmentList.size());
		//logger.info("removedAssesments.size: " + removedAssessmentList.size());
		
		/*List<AssessedGefValueSet> list = assessmentGFVRepository.findAll();
		logger.info("assessmentGFVRepository.size: " + list.size());
		
		for (AssessedGefValueSet l : list) {
			logger.info("assessment : " + l.getAssessmentId());
			logger.info("gfv : " + l.getGefValueId());
		}*/
		
		/*logger.info("GFVs for delete: " + gfvsForDelete.size());
		
		geriatricFactorRepository.delete(gfvsForDelete);
		geriatricFactorRepository.flush();
		geriatricFactorRepository.save(existingGess);*/
		
	}

	private List<GeriatricFactorValue> createGessFor1User(Timestamp startOfMonth, Timestamp endOfMonth,
			UserInRole uir) {
		
		final List<GeriatricFactorValue> gess = new ArrayList<GeriatricFactorValue>();
		
		List<Object[]> gessList = nativeQueryRepository.computeGessForUser(startOfMonth, endOfMonth, uir.getId());
		
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, uir.getPilotCode());
		
		if (gessList != null) logger.info ("gessList.size: " + gessList.size());
		
		if(!gessList.isEmpty()) {
			
			for (Object[] ges : gessList) {
				
				GeriatricFactorValue create1Ges = new GeriatricFactorValue();
				
				create1Ges.setDerivationWeight(ges[3] == null ? new BigDecimal(1) : (BigDecimal) ges [3]);
				create1Ges.setDetectionVariableId(((Integer) ges[1]).longValue());
				create1Ges.setGefValue((BigDecimal) ges[2]);
				create1Ges.setTimeInterval(ti);
				create1Ges.setUserInRoleId(((Integer) ges[0]).longValue());
				
				logger.info("uir: " + ges[0] + " ges_type: " + ges[1] + " value: " + ges[2] + " weight: " + ges[3]);
				
				gess.add(create1Ges);
			}
		}
		return gess;
	}

	public void computeNuisFor1User(Timestamp startOfMonth, Timestamp endOfMonth, UserInRole uir) {
		
		List<NumericIndicatorValue> nuis = createNuisFor1User(startOfMonth, endOfMonth, uir);
		
		List<NumericIndicatorValue> existingNuis = nuiRepository.findNuisForUserAndStartAndEnd (startOfMonth, endOfMonth, uir);
		
		logger.info("existingNuis.size: " + existingNuis.size());
		logger.info("nuisuis.size: " + nuis.size());
		
		nuiRepository.delete(existingNuis);
		nuiRepository.flush();
		nuiRepository.save(nuis);				
		
	}

	private List<NumericIndicatorValue> createNuisFor1User(Timestamp startOfMonth, Timestamp endOfMonth,
			UserInRole uir) {
		
		final List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();
		
		List<Object[]> nuisList = nativeQueryRepository.computeNuisForUser(startOfMonth, endOfMonth, uir.getPilotCode().name().toLowerCase(), uir.getId());
		
		TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, uir.getPilotCode());
		logger.info("createNuisFor1User: ");
		logger.info("ti.id: " + ti.getId() + " ti.start: " + ti.getStart());
		logger.info("startOfMonth: " + startOfMonth);
		
		if (nuisList != null) logger.info ("nuisList.size: " + nuisList.size());
		
		if(!nuisList.isEmpty()) {

			for (Object[] nui:nuisList) {

				NumericIndicatorValue create1Nui = create1Nui((Integer) nui[0],  (Integer) nui[1], (Double) nui[2], startOfMonth, uir.getPilotCode());
				
				//logger.info("user: " + (Integer) nui[0] + " nuiId: " + (Integer) nui[1] + " value: " + (Double) nui[2]);
				
				nuis.add(create1Nui);

			}

		}
		
		return nuis;
		
	}

}
