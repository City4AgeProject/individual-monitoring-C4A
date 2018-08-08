package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.PathParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.jpa.NUIRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TimeIntervalRepository;
import eu.city4age.dashboard.api.jpa.VariationMeasureValueRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.enu.TypicalPeriod;
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
	private NativeQueryRepository nativeQueryRepository;
	
	@Autowired
	private TimeIntervalRepository timeIntervalRepository;
	
	
	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void computeFor1Pilot(@PathParam("pilotCode") String pilotCodeString, @PathParam("newestSubmittedDate") String newestSubmittedDataString) throws Exception {
		
		Pilot pilot = pilotRepository.findByPilotCode(Pilot.PilotCode.valueOf(pilotCodeString.toUpperCase()));
		PilotCode pilotCode = pilot.getPilotCode();

		logger.info("pilotCode: " + pilotCode.name());

		Date latestDerivedMeasuresComputed = pilot.getLatestVariablesComputed();
		Timestamp newestSubmittedTimestamp = Timestamp.valueOf(newestSubmittedDataString);
		Date newestSubmittedData = Date.from(newestSubmittedTimestamp.toInstant());

		YearMonth startOfComputationYearMonth;

		if (latestDerivedMeasuresComputed == null) {
			startOfComputationYearMonth = 
					YearMonth.from(variationMeasureValueRepository.findFirstMonthForPilot(pilot.getPilotCode()).toInstant().atZone(ZoneOffset.UTC).toLocalDate());
		} else {
			startOfComputationYearMonth = 
					YearMonth.from(pilotRepository.findNextMonthForPilot(pilot.getPilotCode()).toInstant().atZone(ZoneOffset.UTC).toLocalDate());
		}

		YearMonth endOfComputationYearMonth = YearMonth.from(YearMonth.from(newestSubmittedData.toInstant().atZone(ZoneOffset.UTC).toLocalDate()));

		Timestamp startOfMonth;
		Timestamp endOfMonth;

		if (endOfComputationYearMonth.isBefore(YearMonth.now())) {
			endOfComputationYearMonth = endOfComputationYearMonth.plusMonths(1L);
			if (pilot.getLatestVariablesComputed() != null) startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}

		while(!startOfComputationYearMonth.equals(endOfComputationYearMonth)) {

			startOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atDay(1).atStartOfDay());
			endOfMonth = Timestamp.valueOf(startOfComputationYearMonth.atEndOfMonth().atTime(LocalTime.MAX));

			logger.info(startOfMonth);

			computeNuisFor1Month(startOfMonth, endOfMonth, pilotCode);
			computeGESsFor1Month(startOfMonth, endOfMonth, pilotCode);
			computeFor1Month(DetectionVariableType.GEF, startOfMonth, endOfMonth, pilotCode);
			computeFor1Month(DetectionVariableType.GFG, startOfMonth, endOfMonth, pilotCode);
			computeFor1Month(DetectionVariableType.OVL, startOfMonth, endOfMonth, pilotCode);
			startOfComputationYearMonth = startOfComputationYearMonth.plusMonths(1L);
		}

		Timestamp endOfComputation = Timestamp.valueOf(endOfComputationYearMonth.minusMonths(1L).atDay(1).atStartOfDay());
		setVariablesComputedForAllPilots(pilot, endOfComputation, newestSubmittedData);
	}

	public void computeNuisFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {

		List<VariationMeasureValue> vmsMonthly = variationMeasureValueRepository
				.findAllForMonthByPilotCodeNui(startOfMonth, endOfMonth, pilotCode);

		if (vmsMonthly != null && vmsMonthly.size() > 0) {
			List<NumericIndicatorValue> nuis = createAllNuis(startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(nuis);
			nuis.clear();
			//nuiRepository.flush();
		}
		vmsMonthly.clear();
	}

	public void computeGESsFor1Month(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) throws Exception {

		String stringPilotCode = pilotCode.name().toLowerCase();
		
		//ROLLBACK SCENARIO!!!
		//if (startOfMonth.equals(Timestamp.valueOf("2017-06-01 00:00:00.0")) && pilotCode == Pilot.PilotCode.ATH) throw new Exception();
		
		List<Object[]> gess = nativeQueryRepository.computeAllGess(startOfMonth, endOfMonth, stringPilotCode);
		//gess.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GES));
		
		if(gess != null && gess.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(gess, startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(gfvs);
			gfvs.clear();
			//nuiRepository.flush();
		}
		gess.clear();

	}

	public void computeFor1Month(DetectionVariableType factor, Timestamp startOfMonth,
			Timestamp endOfMonth, PilotCode pilotCode) throws Exception {
		
		/*if(factor.equals(DetectionVariableType.OVL) && pilotCode.equals(PilotCode.MAD) && startOfMonth.after(Timestamp.valueOf("2017-09-01 00:00:00.0"))) {
			logger.info("Desilo se.");
			throw new Exception("Desilo se.");
		}*/
		
		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> list = nativeQueryRepository.computeAllGfvs(startOfMonth, endOfMonth, factor, stringPilotCode);

		/*if(factor.equals(DetectionVariableType.GEF))
		list.addAll(variationMeasureValueRepository.computeAllDirect(startOfMonth, endOfMonth, pilotCodes, DetectionVariableType.GEF));*/

		if (list != null && list.size() > 0) {
			List<GeriatricFactorValue> gfvs = createAllGFVs(list, startOfMonth, endOfMonth, pilotCode);
			nuiRepository.bulkSave(gfvs);
			gfvs.clear();
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
	
	public List<NumericIndicatorValue> createAllNuis(Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {
		
		final List<NumericIndicatorValue> nuis = new ArrayList<NumericIndicatorValue>();

		String stringPilotCode = pilotCode.name().toLowerCase();
		
		List<Object[]> nuisList = nativeQueryRepository.computeAllNuis(startOfMonth, endOfMonth, stringPilotCode);

		if(!nuisList.isEmpty() && nuisList.size() != 0) {

			for (Object[] nui:nuisList) {

				NumericIndicatorValue create1Nui = create1Nui((Integer) nui[0],  (Integer) nui[1], (Double) nui[2], startOfMonth, pilotCode);

				nuis.add(create1Nui);

			}

		}
		return nuis;

	}
	
	public List<GeriatricFactorValue> createAllGFVs(List<Object[]> list, Timestamp startOfMonth, Timestamp endOfMonth, PilotCode pilotCode) {
		
		final List<GeriatricFactorValue> gfvs = new ArrayList<GeriatricFactorValue>();
		final TimeInterval ti = getOrCreateTimeIntervalPilotTimeZone(startOfMonth, TypicalPeriod.MONTH, pilotCode);
		for(Object[] arr : list) {
			GeriatricFactorValue ges = new GeriatricFactorValue();
			ges.setGefValue((BigDecimal) arr[2]);
			ges.setTimeInterval(ti);
			ges.setDetectionVariableId(((Integer) arr[1]).longValue());
			ges.setUserInRoleId(((Integer) arr[0]).longValue());
			ges.setDerivationWeight(arr[3] == null ? new BigDecimal(1) : (BigDecimal) arr[3]);
			gfvs.add(ges);
		}
		return gfvs;
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
		
		//String computationTimeZone = pilotRepository.findByPilotCode(pilotCode).getCompZone();
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
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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

}
