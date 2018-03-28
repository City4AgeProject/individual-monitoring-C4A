package eu.city4age.dashboard.api.prediction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.Ts;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

import eu.city4age.dashboard.api.jpa.GeriatricFactorPredictionValueRepository;
import eu.city4age.dashboard.api.jpa.NativeQueryRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorPredictionValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.rest.MeasuresService;

/*
 * author: Marina-Andric
 */

public class CreatePrediction {

	private int predictionSize;
	private int trasholdPoint;

	@Autowired
	private GeriatricFactorPredictionValueRepository geriatricFactorPredictionValueRepository;

	@Autowired
	private NativeQueryRepository nativeQueryRepository;

	@Autowired
	private MeasuresService measuresService;

	private Timestamp endDate;

	static protected Logger logger = LogManager.getLogger(CreatePrediction.class);

	public CreatePrediction() {
		predictionSize = 3;
		trasholdPoint = 3;
	}


	public void makePrediction(Long dvId, Long uId) {

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
				prediction.setGefValue(new BigDecimal(forecast.pointEstimates().at(i)));
				prediction.setTimeInterval(computeMonthWithOffset(i+1));
				geriatricFactorPredictionValueRepository.save(prediction);
			}
		}
	}

	// Finds date with monthOffset from the endDate
	private TimeInterval computeMonthWithOffset(int offset) {

		TimeInterval timeIntervalEnd = measuresService.getOrCreateTimeInterval(endDate, eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH);

		Calendar date = Calendar.getInstance();
		date.setTime(timeIntervalEnd.getIntervalStart());
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

		int lastIndex = jointGefList.size()-1;
		endDate = ((Timestamp) jointGefList.get(lastIndex)[1]);

		double[] gefValues = new double[jointGefList.size()];

		if (jointGefList != null && jointGefList.size() != 0) {

			for (int i = 0; i < jointGefList.size(); i++) {

				gefValues[i] = ((BigDecimal) jointGefList.get(i)[0]).doubleValue();

			}			
		}

		return gefValues;

	}

}
