package eu.city4age.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.PathSegment;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsGroups;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsSeries;
import eu.city4age.dashboard.api.pojo.persist.Filter;

public interface GroupAnalyticsService {
	
	int findDetectionVariableValues(DetectionVariable overall, DetectionVariable dv, UserInRole uir,
			List<Date> ovlDates, List<Date> dvDates, double[] ovlValuesDoubles,
			double[] detectionVariableValuesDoubles);
	
	LinkedHashMap<String, Double> averageCorrelationValues(LinkedHashMap<String, Double> valuesList, String name,
			List<Double> correlations);
	
	List<Date> findAllDatesForDetectionVariable(DetectionVariable dv, Date intervalStartDate,
			Date intervalEndDate, UserInRole uir);
	
	List<Pilot> getPilots(String pilotString);
	
	List<DetectionVariable> getDetectionVariables(List<PathSegment> detectionVariableId);
	
	List<Double> calculateCorrelationCoefficientsForOneUser(DetectionVariable overall, DetectionVariable dv,
			List<Double> correlations, Date intervalStartDate, Date intervalEndDate, UserInRole uir);
	
	List<List<Filter>> createAllFilters(List<List<Filter>> allVariablesFilters,
			List<List<Filter>> allPilotsFilters, List<List<Filter>> allCategoryFilters,
			List<List<Filter>> allTimesFilters);
	
	List<List<Filter>> createAllTimeFilters(OffsetDateTime intervalStartODT,
			OffsetDateTime intervalEndODT, String comparison, int numOfCategories);
	
	List<List<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison);
	
	List<List<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs);
	
	List<List<Filter>> createAllCategoryFilters(List<String> categories);
	
	List<List<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories);

	GenericTableData addGenericTableData(List<Filter> filter, Object[] data, Boolean comp, GenericTableData tableData, List<String> pilotCodes);
	
	List<GroupAnalyticsSeries> createSeries(boolean comparison, List<List<String>> data);
	
	List<GroupAnalyticsGroups> createGroups(List<String> categories,
			HashMap<String, List<String>> socioEconomics, List<String> datesStringList, boolean comparison, boolean comp);
	
	List<String> createDateList(String url);
	
	HashMap<String, List<String>> createSocioEconomicsMap();
	
	List<String> getPropertyFromURL(String url, String property);

}
