package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

//import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.rest.MeasuresEndpoint;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/*
 * author: Vladimir Aleksic
 */

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
public class ImputeFactorService {

	@Autowired
	private MeasuresEndpoint measuresEndpoint;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

	//private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	static protected Logger logger = LogManager.getLogger(ImputeFactorService.class);

	//private Long dvId, uId;
	private ArrayList<BigDecimal> gfValues;
	private ArrayList<Integer> intIntervalsExisting;
	private ArrayList<Integer> intIntervalsMissing;
	private ArrayList<TimeInterval> missingTimeIntervals;
	private List<ViewGefCalculatedInterpolatedPredictedValues> viewGeriatricFactorValue;
	private List<GeriatricFactorValue> gefList;
	private int monthCounter, gfIndex, i;
	private TimeInterval ti;
	private Calendar prevDate, endDate, currentDate;
	private SplineInterpolator splineInterpolator;
	private PolynomialSplineFunction polynomialSplineFunction;
	private double[] x, y;
	private double rawX, interpolatedY, extrapolatedMeanValue, sumOfValues;
	private Long uirId;
	private UserInRole uir;
	private Long detectionVariableId;
	private DetectionVariable detectionVariable;

	private int trashholdPoint;

	public ImputeFactorService() {
		
		this.trashholdPoint = 3;
		this.intIntervalsExisting = new ArrayList<Integer>();
		this.intIntervalsMissing = new ArrayList<Integer>();
		this.missingTimeIntervals = new ArrayList<TimeInterval>();
		this.gfValues = new ArrayList<BigDecimal>();
		this.prevDate = Calendar.getInstance();
		this.monthCounter = 0;
		this.gfIndex = 0;
		this.sumOfValues = 0.0;
		this.splineInterpolator = new SplineInterpolator();
		this.prevDate = Calendar.getInstance();
		this.endDate = Calendar.getInstance();
		this.currentDate = Calendar.getInstance();
	}

	public int imputeMissingValues(Long dvId, Long uId, Date endDatePilot) {

		uirId = uId;
		detectionVariableId = dvId;

		gefList = geriatricFactorRepository.findByDetectionVariableId(dvId, uId);		
		
		uir = gefList.get(0).getUserInRole();
		detectionVariable = gefList.get(0).getDetectionVariable();
		
		viewGeriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uId);

		if(viewGeriatricFactorValue.size() > trashholdPoint) {

			return interpolateMissingValuesSpline() + extrapolateMissingValues(endDatePilot);

		} else {
			return -1;
		}
	}

	private int interpolateMissingValuesSpline() {

		prevDate.setTime(viewGeriatricFactorValue.get(0).getIntervalStart());
		endDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1).getIntervalStart());
		//		logger.info("viewGeriatricFactorValue: " + viewGeriatricFactorValue.size());

		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {

			currentDate.setTime(viewGeriatricFactorValue.get(gfIndex).getIntervalStart());

			while(prevDate.getTimeInMillis() < currentDate.getTimeInMillis()) {

				ti = getFollowingTimeInterval(prevDate);
				prevDate.setTime(ti.getIntervalStart());
				
				missingTimeIntervals.add(ti);
				intIntervalsMissing.add(monthCounter);
				monthCounter++;

			}

			intIntervalsExisting.add(monthCounter);
			gfValues.add(viewGeriatricFactorValue.get(gfIndex).getGefValue());

			gfIndex++;
			monthCounter++;

			ti = getFollowingTimeInterval(prevDate);
			logger.info("TI "+ti.getIntervalStart());
			prevDate.setTime(ti.getIntervalStart());
		}

		logger.info("intIntervalsMissing: " + intIntervalsMissing);

		if(intIntervalsMissing.size() == 0) {
			return 0;
		}

		x = new double[gfIndex];
		y = new double[gfIndex];
		
		for(int i = 0; i < gfIndex; i++) {
			
			x[i] = intIntervalsExisting.get(i).doubleValue();
			y[i] = gfValues.get(i).doubleValue();
			sumOfValues += y[i];
		}

		polynomialSplineFunction = splineInterpolator.interpolate(x, y);

		for(i = 0; i < intIntervalsMissing.size(); i++) {

			rawX = intIntervalsMissing.get(i).doubleValue();
			logger.info("X: " + rawX);
			interpolatedY = polynomialSplineFunction.value(rawX);

			sumOfValues += interpolatedY;
			monthCounter++;

			saveImputedValues(missingTimeIntervals.get(i), BigDecimal.valueOf(interpolatedY));

		}
		
		return intIntervalsMissing.size();
	}

	private int extrapolateMissingValues(Date endDatePilot) {

		extrapolatedMeanValue = sumOfValues/monthCounter;
		endDate.setTime(endDatePilot);
		
		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {	

			saveImputedValues(ti, BigDecimal.valueOf(extrapolatedMeanValue));
			monthCounter++;

			ti = getFollowingTimeInterval(prevDate);
			prevDate.setTime(ti.getIntervalStart());			
		}

		return 0;

	}

	private void saveImputedValues(TimeInterval ti, BigDecimal value) {

		GeriatricFactorInterpolationValue gef = new GeriatricFactorInterpolationValue();
		gef.setUserInRole(uir);
		gef.setUserInRoleId(uirId);
		gef.setDetectionVariable(detectionVariable);
		gef.setDetectionVariableId(detectionVariableId);
		gef.setTimeInterval(ti);
		gef.setGefValue(value);
		logger.info("TIE: " + gef.getTimeInterval().getStart());
		gef = geriatricFactorInterpolationValueRepository.save(gef);
		
	}

	private TimeInterval getFollowingTimeInterval(Calendar date) {

		int monthOffset = 1;

		date.add(Calendar.MONTH, monthOffset);

		String format = "yyyy-MM-01 00:00:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String formatted = simpleDateFormat.format(date.getTime());

		TimeInterval timeInterval = measuresEndpoint.getOrCreateTimeInterval(Timestamp.valueOf(formatted), eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		return timeInterval;

	}

}
