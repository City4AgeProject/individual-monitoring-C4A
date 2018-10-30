package eu.city4age.dashboard.api.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.PathSegment;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

public interface GroupAnalyticsService {
	
	int findDetectionVariableValues(DetectionVariable overall, DetectionVariable dv, UserInRole uir,
			List<Date> ovlDates, List<Date> dvDates, double[] ovlValuesDoubles,
			double[] detectionVariableValuesDoubles);
	
	void averageCorrelationValues(LinkedHashMap<String, Double> valuesList, String name,
			List<Double> correlations);
	
	List<Date> findAllDatesForDetectionVariable(DetectionVariable dv, Date intervalStartDate,
			Date intervalEndDate, UserInRole uir);
	
	List<Pilot> getPilots(String pilotString);
	
	List<DetectionVariable> getDetectionVariables(List<PathSegment> detectionVariableId);
	
	void calculateCorrelationCoefficientsForUser(DetectionVariable overall, DetectionVariable dv,
			List<Double> correlations, Date intervalStartDate, Date intervalEndDate, UserInRole uir);
}
