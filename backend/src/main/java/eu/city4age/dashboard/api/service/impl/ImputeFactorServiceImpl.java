package eu.city4age.dashboard.api.service.impl;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.Ts;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

import eu.city4age.dashboard.api.jpa.GeriatricFactorInterpolationValueRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.MeasuresService;

/*
 * author: Vladimir Aleksic
 */

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
public class ImputeFactorServiceImpl implements ImputeFactorService {
	
	@Autowired
	private MeasuresService measuresService;

	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private GeriatricFactorInterpolationValueRepository geriatricFactorInterpolationValueRepository;

	//private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	static protected Logger logger = LogManager.getLogger(ImputeFactorServiceImpl.class);

	//private Long dvId, uId;
	/*private List<BigDecimal> gfValues;
	private List<Integer> intIntervalsExisting;
	private List<Integer> intIntervalsMissing;
	private List<TimeInterval> missingTimeIntervals;*/
	private List<ViewGefCalculatedInterpolatedPredictedValues> viewGeriatricFactorValue;
	//private int monthCounter, gfIndex;
	private int i;
	private TimeInterval ti;
	private Calendar leftDate, endDate, rightDate;
	/*private SplineInterpolator splineInterpolator;
	private PolynomialSplineFunction polynomialSplineFunction;
	private double[] x, y;
	private double rawX, interpolatedY;*/
	private UserInRole userInRole;
	private DetectionVariable detectionVariable;

	private int treshholdPoint;

	public ImputeFactorServiceImpl() {

		this.treshholdPoint = 3;
		this.leftDate = Calendar.getInstance();
		//this.splineInterpolator = new SplineInterpolator();
		this.endDate = Calendar.getInstance();
		this.rightDate = Calendar.getInstance();
	}

	public int imputeMissingValues(DetectionVariable dv, UserInRole uir, Date endDate) {

		this.userInRole = uir;
		this.detectionVariable = dv;
		Long dvId = dv.getId();
		Long uirId = uir.getId();
		
		logger.debug("- Imputing missing values for userId: " + uirId + " and factorId: " + dvId);
		
		/*this.intIntervalsExisting = new ArrayList<Integer>();
		this.intIntervalsMissing = new ArrayList<Integer>();
		this.missingTimeIntervals = new ArrayList<TimeInterval>();
		this.gfValues = new ArrayList<BigDecimal>();*/

		/*this.monthCounter = 0;
		this.gfIndex = 0;*/
		//		this.sumOfValues = 0.0;

		viewGeriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uirId);
		logger.debug("- viewGeriatricFactorValue size before imputing: " + viewGeriatricFactorValue.size());

		if (viewGeriatricFactorValue.size() > treshholdPoint) {

			//return interpolateMissingValuesSpline() + extrapolateMissingValuesMean(endDatePilot);
			int numImputedValues = interpolateMissingValuesMAVG2();
			logger.debug("- Number of interpolated values: " + numImputedValues);

			if (numImputedValues > 0) {
				viewGeriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableIdNoPredicted(dvId, uirId);
				logger.debug("- viewGeriatricFactorValue size after interpolation: " + viewGeriatricFactorValue.size());
			}

			numImputedValues += extrapolateMissingValuesArima(endDate);
			logger.debug("- Total number of imputed values: " + numImputedValues);

			return numImputedValues;

		} else {
			return -1;
		}
	}

	/*private int interpolateMissingValuesMAVG() {

		
		 * average of previous and following value weighted by 2/3 and 1/3 respectively
		 
		int imputed = 0;
		int startIndex = 0;
		int rightIndex = 0;
		
		leftDate.setTime(viewGeriatricFactorValue.get(startIndex).getIntervalStart());
		rightDate.setTime(viewGeriatricFactorValue.get(rightIndex).getIntervalStart());
		endDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1).getIntervalStart());

		BigDecimal leftGefValue = viewGeriatricFactorValue.get(startIndex).getGefValue();
		BigDecimal rightGefValue = viewGeriatricFactorValue.get(rightIndex).getGefValue();
		
		while(rightDate.getTimeInMillis() < endDate.getTimeInMillis()) {
			
			ti = getFollowingTimeInterval(leftDate);
			leftDate.setTime(ti.getIntervalStart());

			rightIndex++;
			rightDate.setTime(viewGeriatricFactorValue.get(rightIndex).getIntervalStart());
			rightGefValue = viewGeriatricFactorValue.get(rightIndex).getGefValue();
			
			while(leftDate.getTimeInMillis() < rightDate.getTimeInMillis()) {
				
				logger.debug("- Missing date: "+ ti.getIntervalStart());
				BigDecimal weightedValue = new BigDecimal(leftGefValue.doubleValue()*2/3 + rightGefValue.doubleValue()*1/3);
				leftGefValue = weightedValue;
				
				saveImputedValues(ti, weightedValue);
				imputed++;
				
				ti = getFollowingTimeInterval(leftDate);
				leftDate.setTime(ti.getIntervalStart());
				
			}

			leftGefValue = rightGefValue;
		}
		
		return imputed;
	}*/
	
	private int interpolateMissingValuesMAVG2() {
		
		/*
		 * average of N (currently 2) previous and follofing values, weighted by 1/(2^distance) (1/2, 1/4, 1/8)
		 */
		
		int countImputedValues=0;
		int leftIndex=0;
		int rightIndex=0;
		int weightPosition;
		int countGefValues=viewGeriatricFactorValue.size();
		double[] weights= {0.5, 0.25};
		double sumOfWeights;
		double weightedValue;
		
		leftDate.setTime(viewGeriatricFactorValue.get(leftIndex).getIntervalStart());
		rightDate.setTime(viewGeriatricFactorValue.get(rightIndex).getIntervalStart());
		endDate.setTime(viewGeriatricFactorValue.get(countGefValues-1).getIntervalStart());
		
		List<Double> leftValues=new ArrayList<Double>();

//		BigDecimal rightGefValue = viewGeriatricFactorValue.get(rightIndex).getGefValue();
		
		while(rightDate.getTimeInMillis() < endDate.getTimeInMillis()) {
					
			ti = getFollowingTimeInterval(leftDate);
			leftDate.setTime(ti.getIntervalStart());

			leftValues.add(viewGeriatricFactorValue.get(rightIndex).getGefValue().doubleValue());
			rightIndex++;
			rightDate.setTime(viewGeriatricFactorValue.get(rightIndex).getIntervalStart());
			
			while(leftDate.getTimeInMillis() < rightDate.getTimeInMillis()) {
				
				logger.debug("leftValues: "+leftValues);
				weightPosition=1;
				weightedValue=0;
				sumOfWeights=0;
				for(double weight : weights) {
					//logger.debug("WTI: "+weightPosition); //, weightedValue, sumOfWeights, leftValues.size(), rightIndex, countGefValues);
					//logger.debug("weight: "+weight);
					if(leftValues.size()-weightPosition>=0) {
						weightedValue=weightedValue+(leftValues.get(leftValues.size()-weightPosition))*weight;
						sumOfWeights=sumOfWeights+weight;
//						logger.debug("WTI: -"+weightPosition);
//						logger.debug(leftValues.get(leftValues.size()-weightPosition));
					}
					if(rightIndex+weightPosition<=countGefValues) {
						weightedValue=weightedValue+(viewGeriatricFactorValue.get(rightIndex+weightPosition-1).getGefValue().doubleValue())*weight;
						sumOfWeights=sumOfWeights+weight;
//						logger.debug("WTI: +"+weightPosition);
//						logger.debug(viewGeriatricFactorValue.get(rightIndex+weightPosition-1).getGefValue().doubleValue());
					}
					weightPosition++;
				}
//				logger.debug("weightedValue "+weightedValue);
//				logger.debug("sumOfWeights "+sumOfWeights);
				weightedValue/=sumOfWeights;
				leftValues.add(weightedValue);
				logger.debug("- Missing date: "+ ti.getIntervalStart()+" Imputed: "+weightedValue);
				
				saveImputedValues(ti, new BigDecimal(weightedValue));
				countImputedValues++;
				
				ti = getFollowingTimeInterval(leftDate);
				leftDate.setTime(ti.getIntervalStart());
				
			}
		}
		
		return countImputedValues;
	}

	/*private int interpolateMissingValuesSpline() {

		//		if (viewGeriatricFactorValue != null && !viewGeriatricFactorValue.isEmpty()) {
		leftDate.setTime(viewGeriatricFactorValue.get(0).getIntervalStart());
		endDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1).getIntervalStart());
		//		}

		while(leftDate.getTimeInMillis() <= endDate.getTimeInMillis()) {

			rightDate.setTime(viewGeriatricFactorValue.get(gfIndex).getIntervalStart());

			while(leftDate.getTimeInMillis() < rightDate.getTimeInMillis()) {
				//ti=next time interval
				missingTimeIntervals.add(ti);
				intIntervalsMissing.add(monthCounter);
				monthCounter++;

				ti = getFollowingTimeInterval(leftDate);
				leftDate.setTime(ti.getIntervalStart());
				
			}

			intIntervalsExisting.add(monthCounter);
			gfValues.add(viewGeriatricFactorValue.get(gfIndex).getGefValue());

			gfIndex++;
			monthCounter++;

			ti = getFollowingTimeInterval(leftDate);
			logger.debug("TI "+ti.getIntervalStart());
			leftDate.setTime(ti.getIntervalStart());
		}

		logger.debug("intIntervalsMissing: " + intIntervalsMissing);

		if(intIntervalsMissing.size() == 0) {
			return 0;
		}

		x = new double[gfIndex];
		y = new double[gfIndex];

		for(int i = 0; i < gfIndex; i++) {

			x[i] = intIntervalsExisting.get(i).doubleValue();
			y[i] = gfValues.get(i).doubleValue();
			//			sumOfValues += y[i];
		}

		polynomialSplineFunction = splineInterpolator.interpolate(x, y);

		for(i = 0; i < intIntervalsMissing.size(); i++) {

			rawX = intIntervalsMissing.get(i).doubleValue();
			logger.debug("X: " + rawX);
			interpolatedY = polynomialSplineFunction.value(rawX);

			//			sumOfValues += interpolatedY;
			monthCounter++;

			saveImputedValues(missingTimeIntervals.get(i), BigDecimal.valueOf(interpolatedY));

		}

		return intIntervalsMissing.size();
	}*/

	//	private int extrapolateMissingValuesMean(Date endDatePilot) {
	//
	//		extrapolatedMeanValue = sumOfValues/monthCounter;
	//		endDate.setTime(endDatePilot);
	//		int counter=0;
	//
	//		while(prevDate.getTimeInMillis() <= endDate.getTimeInMillis()) {	
	//			//ti=next time interval
	//			saveImputedValues(ti, BigDecimal.valueOf(extrapolatedMeanValue));
	//			//monthCounter++;
	//			counter++;
	//
	//			ti = getFollowingTimeInterval(prevDate);
	//			prevDate.setTime(ti.getIntervalStart());			
	//		}
	//
	//		return counter;
	//	}

	private int extrapolateMissingValuesArima(Date endDateUser) {
		//Re-load viewGeriatricFactorValue with interpolated values

		int vs = viewGeriatricFactorValue.size();
		double[] dataArray = new double[vs];
		for(int i = 0; i < vs; i++) {
			dataArray[i] = viewGeriatricFactorValue.get(i).getGefValue().doubleValue();
		}

		ArrayList<TimeInterval> tis = new ArrayList<TimeInterval>();
		leftDate.setTime(viewGeriatricFactorValue.get(viewGeriatricFactorValue.size()-1).getIntervalStart());
		endDate.setTime(endDateUser);

		while(leftDate.getTimeInMillis() < endDate.getTimeInMillis()) {
			//ti = current time interval
			ti = getFollowingTimeInterval(leftDate);
			logger.debug("- Missing date: "+ ti.getIntervalStart());
			tis.add(ti);
			leftDate.setTime(ti.getIntervalStart());
		}

		if (tis.size() == 0) 
			return 0;

		TimeSeries timeSeries = Ts.newMonthlySeries(dataArray);

		ArrayList<ArimaOrder> models = new ArrayList<ArimaOrder>();
		models.add(ArimaOrder.order(0, 1, 1, 0, 0, 0, Arima.Drift.EXCLUDE));	// simple exponential smoothing 
		models.add(ArimaOrder.order(0, 1, 1, Arima.Drift.INCLUDE));				// simple exponential smoothing with growth
		models.add(ArimaOrder.order(1, 1, 0, 0, 0, 0)); 						// differenced first-order autoregressive model
		models.add(ArimaOrder.order(1, 1, 2, 0, 0, 0)); 						// damped-trend linear exponential smoothing

		ArrayList<Arima> fmodels = new ArrayList<Arima>();
		for (int i = 0; i < models.size(); i++) {
			fmodels.add(Arima.model(timeSeries, models.get(i)));
		}

		// Finds optimal ARIMA model (with minimal AIC criteria)
		Arima optimalModel = fmodels.get(0);
		double aic = fmodels.get(0).aic();
		double aic_curr;
		for (int i = 1; i < models.size(); i++) {
			if ((aic_curr = fmodels.get(i).aic()) < aic) {
				aic = aic_curr;
				optimalModel = fmodels.get(i);
			}
		}
		Forecast forecast = optimalModel.forecast(tis.size());
		//logger.debug("FC: "+forecast);
		for (i = 0; i < tis.size(); i++) {
			saveImputedValues(tis.get(i), (new BigDecimal(forecast.pointEstimates().at(i))));
		}

		return tis.size();
	}

	private void saveImputedValues(TimeInterval ti, BigDecimal value) {

		GeriatricFactorInterpolationValue gef = new GeriatricFactorInterpolationValue();
		gef.setUserInRole(userInRole);
		gef.setUserInRoleId(userInRole.getId());
		gef.setDetectionVariable(detectionVariable);
		gef.setDetectionVariableId(detectionVariable.getId());
		gef.setTimeInterval(ti);
		gef.setGefValue(value);
		geriatricFactorInterpolationValueRepository.save(gef);

	}

	public TimeInterval getFollowingTimeInterval(Calendar date) {

		int monthOffset = 1;

		date.add(Calendar.MONTH, monthOffset);
		date.add(Calendar.DAY_OF_MONTH, 6); // correction for daylight savings

		String format = "yyyy-MM-01 00:00:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String formatted = simpleDateFormat.format(date.getTime());

		TimeInterval timeInterval = measuresService.getOrCreateTimeInterval(Timestamp.valueOf(formatted), eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		return timeInterval;

	}

}
