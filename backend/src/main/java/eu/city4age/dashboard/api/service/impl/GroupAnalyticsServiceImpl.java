package eu.city4age.dashboard.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.PathSegment;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.city4age.dashboard.api.jpa.DerivedMeasureValueRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.ViewGefCalculatedInterpolatedPredictedValuesRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.service.AssessmentService;
import eu.city4age.dashboard.api.service.GroupAnalyticsService;

@Component
public class GroupAnalyticsServiceImpl implements GroupAnalyticsService {
	
	static protected Logger logger = LogManager.getLogger(GroupAnalyticsServiceImpl.class);
	
	@Autowired
	private ViewGefCalculatedInterpolatedPredictedValuesRepository viewGefCalculatedInterpolatedPredictedValuesRepository;
	
	@Autowired
	private DerivedMeasureValueRepository derivedMeasureValueRepository;
	
	@Autowired
	private PilotRepository pilotRepository;
	
	@Autowired
	private DetectionVariableRepository detectionVariableRepository;
	
	@Autowired
	private AssessmentService assessmentService;
	

	/**
	 * @param overall
	 * @param dv
	 * @param uir
	 * @param ovlDates
	 * @param dvDates
	 * @param ovlValuesDoubles
	 * @param detectionVariableValuesDoubles
	 * @return
	 */
	public int findDetectionVariableValues(DetectionVariable overall, DetectionVariable dv, UserInRole uir,
			List<Date> ovlDates, List<Date> dvDates, double[] ovlValuesDoubles,
			double[] detectionVariableValuesDoubles) {
		Date intervalStart = null;
		int cnt = 0;
		for (Date ovlDate : ovlDates) {
			if (dvDates.contains(ovlDate)) {
				intervalStart = ovlDate;


				BigDecimal ovlValue = viewGefCalculatedInterpolatedPredictedValuesRepository
						.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), overall.getId(),
								intervalStart);
				ovlValuesDoubles[cnt] = ovlValue.doubleValue();

				BigDecimal detectionVariableValue = null;
				if (dv.getDetectionVariableType()
						.getDetectionVariableType() != DetectionVariableType.Type.MEA) {
					detectionVariableValue = viewGefCalculatedInterpolatedPredictedValuesRepository
							.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), dv.getId(),
									intervalStart);
				} else {
					detectionVariableValue = derivedMeasureValueRepository
							.findByUserInRoleIdAndDetectionVariableIdForOneMonth(uir.getId(), dv.getId(),
									intervalStart);
				}
				detectionVariableValuesDoubles[cnt] = detectionVariableValue.doubleValue();

				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * @param valuesList
	 * @param dv
	 * @param correlations
	 * @return 
	 */
	@Override
	public LinkedHashMap<String, Double> averageCorrelationValues(LinkedHashMap<String, Double> valuesList, String name,
			List<Double> correlations) {
		if (!correlations.isEmpty()) {
			Double sum = 0d;
			for (Double c : correlations) {
				sum += c;
			}
			valuesList.put(name, sum/correlations.size());
		} else {
			valuesList.put(name, null);
		}
		
		return valuesList;
	}

	/**
	 * @param dv
	 * @param intervalStartDate
	 * @param intervalEndDate
	 * @param uir
	 * @return
	 */
	@Override
	public List<Date> findAllDatesForDetectionVariable(DetectionVariable dv, Date intervalStartDate,
			Date intervalEndDate, UserInRole uir) {
		
		List<Date> dvDates = new ArrayList<Date>();

		if (dv.getDetectionVariableType()
				.getDetectionVariableType() != DetectionVariableType.Type.MEA) {
			dvDates = viewGefCalculatedInterpolatedPredictedValuesRepository
					.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), dv.getId(), intervalStartDate, intervalEndDate);
		} else {
			dvDates = derivedMeasureValueRepository
					.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), dv.getId(), intervalStartDate, intervalEndDate);
		}
		return dvDates;
	}
	
	/**
	 * @param pilotString
	 * @return
	 */
	@Override
	public List<Pilot> getPilots(String pilotString) {
		List<Pilot> pilots = new ArrayList<Pilot>();

		if (pilotString != null && !pilotString.contains("whole_population")) {
			List<String> pilotsString = Arrays.asList(pilotString.split(" "));

			for (String ps : pilotsString) pilots.add(pilotRepository.findByPilotCode(PilotCode.valueOf(ps.toUpperCase())));
		} else {
			pilots = pilotRepository.findAll();
		}
		return pilots;
	}
	
	/**
	 * @param detectionVariableId
	 * @return
	 */
	@Override
	public List<DetectionVariable> getDetectionVariables(List<PathSegment> detectionVariableId) {
		List<DetectionVariable> detectionVariables = new ArrayList<DetectionVariable>();
		List<Long> detectionVariableIds = assessmentService.convertToListLong(detectionVariableId);
		for (Long detectionVariable : detectionVariableIds) detectionVariables.add(detectionVariableRepository.findOne(detectionVariable));
		return detectionVariables;
	}
	
	/**
	 * @param overall
	 * @param dv
	 * @param correlations
	 * @param intervalStartDate
	 * @param intervalEndDate
	 * @param uir
	 * @return 
	 */
	@Override
	public List<Double> calculateCorrelationCoefficientsForOneUser(DetectionVariable overall, DetectionVariable dv,
			List<Double> correlations, Date intervalStartDate, Date intervalEndDate, UserInRole uir) {
		
		// find min and max dates for overall and the selected detection variable and determine the interval common to both
		List<Date> ovlDates = viewGefCalculatedInterpolatedPredictedValuesRepository
				.findDatesForUserInRoleIdAndDetectionVariableIdForInterval(uir.getId(), overall.getId(), intervalStartDate, intervalEndDate);

		List<Date> dvDates = findAllDatesForDetectionVariable(dv, intervalStartDate, intervalEndDate,
				uir);

		correlations = calculateCorrelationCoefficients(overall, dv, correlations, uir, ovlDates, dvDates);
		
		return correlations;
	}
	
	/**
	 * @param overall
	 * @param dv
	 * @param correlations
	 * @param uir
	 * @param ovlDates
	 * @param dvDates
	 * @return 
	 */
	private List<Double> calculateCorrelationCoefficients(DetectionVariable overall, DetectionVariable dv, List<Double> correlations,
			UserInRole uir, List<Date> ovlDates, List<Date> dvDates) {
		
		if (!ovlDates.isEmpty() && !dvDates.isEmpty()) {

			int arraySize = getArraySize(ovlDates, dvDates);

			double ovlValuesDoubles[] = new double[arraySize];
			double detectionVariableValuesDoubles[] = new double[arraySize];

			// find appropriate values for overall and the selected detection variable for the selected interval
			int cnt = findDetectionVariableValues(overall, dv, uir, ovlDates, dvDates, ovlValuesDoubles,
					detectionVariableValuesDoubles);

			// calculate the correlation coefficient
			if (cnt > 2) {

				double matrix[][] = { ovlValuesDoubles, detectionVariableValuesDoubles };

				RealMatrix mat = MatrixUtils.createRealMatrix(matrix);
				mat = mat.transpose();

				PearsonsCorrelation corrP = new PearsonsCorrelation(mat);

				RealMatrix corrMatrix = corrP.getCorrelationPValues();

				double corrPValue = corrMatrix.getEntry(0, 1);

				if (corrPValue <= 0.05) {
					double corr = new PearsonsCorrelation().correlation(ovlValuesDoubles,
							detectionVariableValuesDoubles);

					correlations.add(corr);
				} else {
					logger.info("p value previse visoka!!!");
				}

			} else {
				logger.info("nema dovoljno podataka!!!");
			}
		}
		
		return correlations;
	}
	
	/**
	 * @param ovlDates
	 * @param dvDates
	 * @return
	 */
	private int getArraySize(List<Date> ovlDates, List<Date> dvDates) {
		int arraySize = 0;
		if (ovlDates.size() <= dvDates.size())
			arraySize = ovlDates.size();
		else
			arraySize = dvDates.size();
		return arraySize;
	}

}
