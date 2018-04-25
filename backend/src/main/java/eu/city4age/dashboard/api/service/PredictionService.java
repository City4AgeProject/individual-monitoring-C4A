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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.Ts;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

import eu.city4age.dashboard.api.jpa.CareProfileRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorPredictionValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.AttentionStatus;
import eu.city4age.dashboard.api.pojo.domain.CareProfile;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorPredictionValue;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;

/*
 * author: Marina-Andric
 */

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
public class PredictionService {

	private int predictionSize;
	private int trasholdPoint;
	private Long overallId;

	@Autowired
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private PilotDetectionVariableRepository pilotDetectionVariableRepository;

	@Autowired
	private UserInRoleRepository userInRoleRepository;

	@Autowired
	private GeriatricFactorRepository geriatricFactorRepository;

	@Autowired
	private ImputeFactorService imputeFactorService;

	@Autowired
	private MeasuresService measuresService;
	
	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;

	@Autowired
	private PilotRepository pilotRepository;

	@Autowired
	private CareProfileRepository careProfileRepository;

	private Date endDate;
	private String systemUserName;

	static protected Logger logger = LogManager.getLogger(PredictionService.class);

	public PredictionService() {
		predictionSize = 3;
		trasholdPoint = 3;
		overallId = 501L;
		systemUserName = "system";
	}

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void imputeAndPredict(Pilot pilot) {

		//		List<Pilot> pilots = pilotRepository.findPilotsComputed();
		logger.info("*** Start of imputeAndPredict ***");
		logger.info("PilotName: " + pilot.getName());

		List<DetectionVariable> detectionVariables = pilotDetectionVariableRepository.findDetectionVariablesForPrediction(pilot.getPilotCode());

		for (DetectionVariable dv : detectionVariables) {

			logger.info("DetectionVariableId: " + dv.getId());
			List<UserInRole> userInRoles = userInRoleRepository.findCRsByPilotCode(pilot.getPilotCode()); 

			for (UserInRole userInRole : userInRoles) {

				logger.info("UserInRoleId: " + userInRole.getId());

				// Last date for which there is at least one data item in the DB for the user
				this.endDate = geriatricFactorRepository.findMaxIntervalStartByUserInRole(userInRole.getId()).getIntervalStart();
				logger.info("- EndDate: " + endDate);

				imputeFactorService.imputeMissingValues(dv, userInRole, endDate);

				// Delete obsolete predictions -  FOR USER!
				List<GeriatricFactorPredictionValue> predictionsToDelete = geriatricFactorPredictionValueRepository.deleteObsoletePredictions(endDate, userInRole.getId(), dv.getId());
				geriatricFactorPredictionValueRepository.delete(predictionsToDelete);
				logger.info("- Deleted " + predictionsToDelete.size() + " obsolete predictions");

				this.makePredictions(dv, userInRole, endDate);

			}				
		}			
	}

	private CareProfile getOrCreateCareProfile(Long userInRoleId, UserInRole userInRoleCreatedBy) {

		CareProfile careProfile = careProfileRepository.findByUserId(userInRoleId);

		//		logger.info("getOrCreateCareProfile");

		if(careProfile == null) {
			careProfile = new CareProfile();
			careProfile.setUserInRoleId(userInRoleId);	
			careProfile.setUserInRoleByCreatedBy(userInRoleCreatedBy);	
			careProfile.setCreated(new Date());
		} else {
			careProfile.setLastUpdated(new Date());
			careProfile.setUserInRoleByLastUpdatedBy(userInRoleCreatedBy);
		}

		return careProfile;

	}

	private void makePredictions(DetectionVariable dv, UserInRole uir, Date endDate) {

		Long dvId = dv.getId();
		Long uId = uir.getId();

		logger.info("- Making predictions for " + "uId: " + uId + " and factorId " + dvId);

		double[] dataArray = getJointFactorValues(dvId, uId);

		if (dataArray.length > trasholdPoint) { 

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
			for (int i = 1; i < models.size(); i++)
				if ((aic_curr = fmodels.get(i).aic()) < aic) {
					aic = aic_curr;
					optimalModel = fmodels.get(i);
				}

			Forecast forecast = optimalModel.forecast(predictionSize);

			// Saves predictions to the database
			for (int i = 0; i < predictionSize; i++) {
				GeriatricFactorPredictionValue prediction = new GeriatricFactorPredictionValue();
				prediction.setUserInRoleId(uId);
				prediction.setGefValue(makeValid(new BigDecimal(forecast.pointEstimates().at(i))));
				prediction.setTimeInterval(computeMonthWithOffset(i+1));
				prediction.setDetectionVariableId(dvId);
				geriatricFactorPredictionValueRepository.save(prediction);
			}

			if (dv.getDetectionVariableType().toString().equals(DetectionVariableType.OVL.toString()))
				createAttentionStatus(uir.getId());
		}
	}

	private int createAttentionStatus(Long uId) {

		logger.info("- Checking AttentionStatus for uId: " + uId);

		AttentionStatus.Status attentionStatus;

		UserInRole system = userInRoleRepository.findBySystemUsername(systemUserName);

		List<ViewGefCalculatedInterpolatedPredictedValues> viewGeriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableId(overallId, uId);

		int lastIndex = viewGeriatricFactorValue.size();		
		BigDecimal lastComputedValue = viewGeriatricFactorValue.get(lastIndex-4).getGefValue();

		logger.info("-Overall lastComputedValue: " + lastComputedValue);
		BigDecimal lastPredictedValue = viewGeriatricFactorValue.get(lastIndex-1).getGefValue();
		logger.info("- Overall lastPredictedValue: " + lastPredictedValue);
		BigDecimal difference = new BigDecimal(lastPredictedValue.doubleValue() - lastComputedValue.doubleValue());

		if (difference.doubleValue() < 0 && Math.abs(difference.doubleValue()) >= 0.2 * lastComputedValue.doubleValue() || lastPredictedValue.equals(new BigDecimal(1))) {
			logger.info("- Creating alert");

			attentionStatus = AttentionStatus.Status.A;
			CareProfile careProfile = this.getOrCreateCareProfile(uId, system);			
			careProfile.setAttentionStatus(attentionStatus.getName());
			careProfileRepository.save(careProfile);

		} else {
			CareProfile careProfile = careProfileRepository.findByUserId(uId);
			if (careProfile != null && careProfile.getAttentionStatus() != null && !String.valueOf(careProfile.getAttentionStatus()).equals(AttentionStatus.Status.M.toString())) {
				careProfile.setAttentionStatus(null);
				careProfile.setUserInRoleByLastUpdatedBy(system);
				careProfile.setLastUpdated(new Date());
				careProfileRepository.save(careProfile);
			}				
		}

		return viewGeriatricFactorValue.size();
	}

	private BigDecimal makeValid(BigDecimal value) {

		if (value.doubleValue() < 1)
			return new BigDecimal(1);
		else if (value.doubleValue() > 5)
			return new BigDecimal(5);
		else
			return value;

	}

	// Finds date with monthOffset from the endDate
	private TimeInterval computeMonthWithOffset(int offset) {

		TimeInterval timeIntervalEnd = measuresService.getOrCreateTimeInterval(endDate, eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		Calendar date = Calendar.getInstance();
		date.setTime(timeIntervalEnd.getIntervalStart());

		date.add(Calendar.DAY_OF_MONTH, 6); // correction for daylight savings
		date.add(Calendar.MONTH, offset);


		String format = "yyyy-MM-01 00:00:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String formatted = simpleDateFormat.format(date.getTime());

		TimeInterval timeInterval = measuresService.getOrCreateTimeInterval(Timestamp.valueOf(formatted), eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		return timeInterval;

	}


	private double[] getJointFactorValues(Long dvId, Long uId) {

		List<Object[]> jointGefList = new ArrayList<Object[]>();
		jointGefList.addAll(nativeQueryRepository.getJointGefValues(dvId, uId));

		double[] gefValues = new double[jointGefList.size()];

		if (jointGefList != null && jointGefList.size() != 0) {

			for (int i = 0; i < jointGefList.size(); i++) {

				gefValues[i] = ((BigDecimal) jointGefList.get(i)[0]).doubleValue();

			}			
		}

		return gefValues;

	}

}
