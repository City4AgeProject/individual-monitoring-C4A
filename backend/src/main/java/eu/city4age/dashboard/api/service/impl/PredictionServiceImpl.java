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

import eu.city4age.dashboard.api.jpa.CareProfileRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorPredictionValueRepository;
import eu.city4age.dashboard.api.jpa.GeriatricFactorRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
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
import eu.city4age.dashboard.api.service.ImputeFactorService;
import eu.city4age.dashboard.api.service.MeasuresService;
import eu.city4age.dashboard.api.service.PredictionService;

/**
 * author: Marina-Andric
 */

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
public class PredictionServiceImpl implements PredictionService {

	private static final int predictionSize = 3;
	
	private static final int tresholdPoint = 3;
	
	private static final Long overallId = 501L;
	
	private static final String systemUserName = "system";

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
	private CareProfileRepository careProfileRepository;

	static protected Logger logger = LogManager.getLogger(PredictionServiceImpl.class);

	@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void imputeAndPredict(Pilot pilot) {
		
		//List<Pilot> pilots = pilotRepository.findPilotsComputed();
		logger.debug("*** Start of imputeAndPredict ***");
		logger.debug("PilotName: " + pilot.getName());

		List<DetectionVariable> detectionVariables = pilotDetectionVariableRepository.findDetectionVariablesForPrediction(pilot.getPilotCode());

		for (DetectionVariable dv : detectionVariables) {

			logger.debug("DetectionVariableId: " + dv.getId());
			List<UserInRole> userInRoles = userInRoleRepository.findCRsByPilotCode(pilot.getPilotCode()); 

			for (UserInRole userInRole : userInRoles) {

				logger.debug("UserInRoleId: " + userInRole.getId());

				// Last date for which there is at least one data item in the DB for the user
				Date endDate = geriatricFactorRepository.findMaxIntervalStartByUserInRole(userInRole.getId()).getIntervalStart();
				logger.debug("- EndDate: " + endDate);

				imputeFactorService.imputeMissingValues(dv, userInRole, endDate);

				// Delete obsolete predictions -  FOR USER!
				List<GeriatricFactorPredictionValue> predictionsToDelete = geriatricFactorPredictionValueRepository.deleteObsoletePredictions(endDate, userInRole.getId(), dv.getId());
				geriatricFactorPredictionValueRepository.delete(predictionsToDelete);
				logger.debug("- Deleted " + predictionsToDelete.size() + " obsolete predictions");


				this.makePredictions(dv, userInRole, endDate);


			}				
		}			
	}

	private CareProfile getOrCreateCareProfile(Long userInRoleId, UserInRole userInRoleCreatedBy) {

		CareProfile careProfile = careProfileRepository.findByUserId(userInRoleId);

		//		logger.debug("getOrCreateCareProfile");

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

		double[] dataArray = getJointFactorValues(dvId, uId);

		if (dataArray.length > tresholdPoint) { 

			logger.debug("- Making predictions for " + "uId: " + uId + " and factorId " + dvId);

			TimeSeries timeSeries = Ts.newMonthlySeries(dataArray);

			ArrayList<ArimaOrder> models = new ArrayList<ArimaOrder>();

			/* Autoregressive models */

			for (int p = 0; p < 3; p++)
				for (int d = 0; d < 2; d++)
					for (int q = 0; q < 3; q++) {
						//						if (!(p==0 && p==0 && d==1))
						models.add(ArimaOrder.order(p, d, q));
						models.add(ArimaOrder.order(p, d, q, Arima.Drift.INCLUDE));
						models.add(ArimaOrder.order(p, d, q, Arima.Constant.INCLUDE));
						//						logger.debug("p, d, q: " + "( " +  p + " " + d + " " + q + " )");
					}

			ArrayList<Arima> fmodels = new ArrayList<Arima>();

			Arima tmp;
			for (int i = 0; i < models.size(); i++) {
				try {
					tmp = Arima.model(timeSeries, models.get(i));
				} catch (Exception e) {
					continue;	
				}
				if (!Double.isNaN(tmp.aic()))
					fmodels.add(tmp);
			}

			// Finds optimal ARIMA model (with minimal AIC criteria)
			Arima optimalModel = fmodels.get(0);
			double aic = fmodels.get(0).aic();
			double aic_curr;
			for (int i = 1; i < fmodels.size(); i++) {
				//				logger.debug("model params\n" + models);
				if ((aic_curr = fmodels.get(i).aic()) < aic) {
					aic = aic_curr;
					optimalModel = fmodels.get(i);
				}
			}

			Forecast forecast = optimalModel.forecast(predictionSize);

			logger.debug("Optimal model: " + optimalModel);

			// Saves predictions to the database
			for (int i = 0; i < predictionSize; i++) {
				GeriatricFactorPredictionValue prediction = new GeriatricFactorPredictionValue();
				prediction.setUserInRoleId(uId);
				prediction.setGefValue(makeValid(new BigDecimal(forecast.pointEstimates().at(i))));
				prediction.setTimeInterval(computeMonthWithOffset(i+1, endDate));
				prediction.setDetectionVariableId(dvId);
				geriatricFactorPredictionValueRepository.save(prediction);
			}

			if (dv.getDetectionVariableType().toString().equals(DetectionVariableType.OVL.toString()))
				createAttentionStatus(uir.getId());
		}
	}

	private int createAttentionStatus(Long uId) {

		logger.debug("- Checking AttentionStatus for uId: " + uId);

		AttentionStatus.Status attentionStatus;

		UserInRole system = userInRoleRepository.findBySystemUsername(systemUserName);

		List<ViewGefCalculatedInterpolatedPredictedValues> viewGeriatricFactorValue = viewGefCalculatedInterpolatedPredictedValuesRepository.findByDetectionVariableId(overallId, uId);

		int lastIndex = viewGeriatricFactorValue.size();		
		BigDecimal lastComputedValue = viewGeriatricFactorValue.get(lastIndex-4).getGefValue();

		logger.debug("-Overall lastComputedValue: " + lastComputedValue);
		BigDecimal lastPredictedValue = viewGeriatricFactorValue.get(lastIndex-1).getGefValue();
		logger.debug("- Overall lastPredictedValue: " + lastPredictedValue);
		BigDecimal difference = new BigDecimal(lastPredictedValue.doubleValue() - lastComputedValue.doubleValue());

		if (difference.doubleValue() < 0 && Math.abs(difference.doubleValue()) >= 0.2 * lastComputedValue.doubleValue() || lastPredictedValue.equals(new BigDecimal(1))) {
			logger.debug("- Creating alert");

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
	private TimeInterval computeMonthWithOffset(int offset, Date endDate) {

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

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void imputeAndPredictFor1User(UserInRole uir) {

		// List<Pilot> pilots = pilotRepository.findPilotsComputed();
		logger.debug("*** Start of imputeAndPredict ***");
		logger.debug("uir: " + uir.getId());

		List<DetectionVariable> detectionVariables = pilotDetectionVariableRepository
				.findDetectionVariablesForPrediction(uir.getPilotCode());
		
		if (uir.getPilot().getLatestVariablesComputed() == null) return;

		for (DetectionVariable dv : detectionVariables) {

			logger.debug("DetectionVariableId: " + dv.getId());

			
			// Last date for which there is at least one data item in the DB for the user
			Date endDate = geriatricFactorRepository.findMaxIntervalStartByUserInRole(uir.getId())
					.getIntervalStart();
			logger.debug("- EndDate: " + endDate);

			imputeFactorService.imputeMissingValues(dv, uir, endDate);

			// Delete obsolete predictions - FOR USER!
			List<GeriatricFactorPredictionValue> predictionsToDelete = geriatricFactorPredictionValueRepository
					.deleteObsoletePredictions(endDate, uir.getId(), dv.getId());
			geriatricFactorPredictionValueRepository.delete(predictionsToDelete);
			logger.debug("- Deleted " + predictionsToDelete.size() + " obsolete predictions");

			this.makePredictions(dv, uir, endDate);

		}

	}

}
