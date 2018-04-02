package eu.city4age.dashboard.api.interpolation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.rest.MeasuresService;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/*
 * author: Vladimir Aleksic
 */

public class InterpolateMissingValuesSpline {
	
	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;
	
	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

	//private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	static protected Logger logger = LogManager.getLogger(InterpolateMissingValuesSpline.class);

	//private Long dvId, uId;
	private ArrayList<BigDecimal>gfValues;
	private ArrayList<Integer>intIntervalsExisting;
	private ArrayList<Integer>intIntervalsMissing;
	private ArrayList<TimeInterval>tiIntervaslMissingInterpolation;
	private List<ViewGefCalculatedInterpolatedPredictedValues> viewGeriatricFactorValue;
	private List<GeriatricFactorValue> gf;
	private GeriatricFactorValue gf0;
	private int month, year, monthCounter, gfIndex, i;
	private String strDate;
	private TimeInterval ti;
	private Calendar prevDate=Calendar.getInstance(), endDate=Calendar.getInstance(), currentDate=Calendar.getInstance();
	private SplineInterpolator splineInterpolator=new SplineInterpolator();
	private PolynomialSplineFunction polynomialSplineFunction;
	private double[] x, y;
	private double rawX, interpolatedY, extrapolatedMeanValue, sumOfValues=0.0;
	private GeriatricFactorInterpolationValue gefi;
	public int getData(Long dvId, Long uId, Date endDatePilot) {
		intIntervalsExisting=new ArrayList<Integer>();
		intIntervalsMissing=new ArrayList<Integer>();
		tiIntervaslMissingInterpolation=new ArrayList<TimeInterval>();
		gfValues=new ArrayList<BigDecimal>();
		prevDate=Calendar.getInstance();
		monthCounter=0;
		gfIndex=0;

		//latestVariablesComputed=pilotRepository.findByPilotCode(userInRoleRepository.findByUirId(uId).getPilotCode()).getLatestVariablesComputed();
		viewGeriatricFactorValue=viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uId);
		if(viewGeriatricFactorValue.size()<4) {
			return -1; //polzSpline throws exception with less than 4 input values
		}
		
/*		if(latestVariablesComputed==null) {
		this.endDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1)
							.getIntervalStart());
		}else {
			this.endDate.setTime(latestVariablesComputed);
		}*/
		
		prevDate.setTime(viewGeriatricFactorValue.get(0).getIntervalStart());
		endDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1)
				.getIntervalStart());
		logger.info("viewGeriatricFactorValue: "+viewGeriatricFactorValue.size());
		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
			currentDate.setTime(viewGeriatricFactorValue.get(gfIndex).getIntervalStart());
			month=prevDate.get(Calendar.MONTH);
			year=prevDate.get(Calendar.YEAR);
			while(prevDate.getTimeInMillis() < currentDate.getTimeInMillis()) {
				tiIntervaslMissingInterpolation.add(ti);
				intIntervalsMissing.add(monthCounter);
				monthCounter++;
				month++;
				if(month==12) {
					month=0;
					year++;
				}
				strDate=String.format("%4d-%02d-01 00:00:00", year, month+1);
				ti = measuresService
						.getOrCreateTimeInterval(Timestamp.valueOf(strDate),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
				prevDate.setTime(ti.getIntervalStart());
			}
			intIntervalsExisting.add(monthCounter);
			gfValues.add(viewGeriatricFactorValue.get(gfIndex).getGefValue());
			gfIndex++;
			monthCounter++;
			month++;
			if(month==12) {
				month=0;
				year++;
			}
			strDate=String.format("%4d-%02d-01 00:00:00", year, month+1);
			ti = measuresService
					.getOrCreateTimeInterval(Timestamp.valueOf(strDate),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
			prevDate.setTime(ti.getIntervalStart());
		}
		logger.info("intIntervalsMissing: "+intIntervalsMissing);
		
		if(intIntervalsMissing.size()==0) {
			return 0;
		}
		x=new double[gfIndex];
		y=new double[gfIndex];
		for(int i=0;i<gfIndex;i++) {
			x[i]=intIntervalsExisting.get(i).doubleValue();
			y[i]=gfValues.get(i).doubleValue();
			sumOfValues+=y[i];
		}
		polynomialSplineFunction=splineInterpolator.interpolate(x, y);
		gf=geriatricFactorRepository.findByDetectionVariableId(dvId, uId);
		gf0=gf.get(0);
		
		for(i=0;i<intIntervalsMissing.size();i++) {
			
			rawX=intIntervalsMissing.get(i).doubleValue();
			interpolatedY=polynomialSplineFunction.value(rawX);
			sumOfValues+=interpolatedY;
			monthCounter++;
			
			gefi = new GeriatricFactorInterpolationValue();
			gefi.setUserInRole(gf0.getUserInRole());
			gefi.setUserInRoleId(gf0.getUserInRoleId());
			gefi.setDetectionVariable(gf0.getDetectionVariable());
			gefi.setDetectionVariableId(gf0.getDetectionVariableId());
			gefi.setTimeInterval(tiIntervaslMissingInterpolation.get(i));
			gefi.setGefValue(BigDecimal.valueOf(interpolatedY));
			logger.info("TII: "+gefi.getTimeInterval().getStart());
			gefi = geriatricFactorInterpolationValueRepository.save(gefi);

		}
		extrapolatedMeanValue=sumOfValues/monthCounter;
		endDate.setTime(endDatePilot);
		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
			
			
			gefi = new GeriatricFactorInterpolationValue();
			gefi.setUserInRole(gf0.getUserInRole());
			gefi.setUserInRoleId(gf0.getUserInRoleId());
			gefi.setDetectionVariable(gf0.getDetectionVariable());
			gefi.setDetectionVariableId(gf0.getDetectionVariableId());
			gefi.setTimeInterval(ti);
			gefi.setGefValue(BigDecimal.valueOf(extrapolatedMeanValue));
			logger.info("TIE: "+gefi.getTimeInterval().getStart());
			gefi = geriatricFactorInterpolationValueRepository.save(gefi);

			monthCounter++;
			month++;
			if(month==12) {
				month=0;
				year++;
			}
			strDate=String.format("%4d-%02d-01 00:00:00", year, month+1);
			ti = measuresService
					.getOrCreateTimeInterval(Timestamp.valueOf(strDate),eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);
			prevDate.setTime(ti.getIntervalStart());			
		}
		
		return intIntervalsMissing.size();
	}
	
}
