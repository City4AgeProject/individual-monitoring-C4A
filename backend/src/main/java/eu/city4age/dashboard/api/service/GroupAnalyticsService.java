package eu.city4age.dashboard.api.service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.PathSegment;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.groupAnalytics.GroupAnalyticsSeries;
import eu.city4age.dashboard.api.pojo.persist.Filter;

public interface GroupAnalyticsService {
	
	LinkedHashMap<String, Double> averageCorrelationValues(LinkedHashMap<String, Double> valuesList, String name,
			List<Double> correlations);
	
	List<Pilot> getPilots(String pilotString);
	
	List<DetectionVariable> getDetectionVariables(List<PathSegment> detectionVariableId);
	
	Double calculateCorrelationCoefficientsForOneUser(DetectionVariable overall, DetectionVariable dv,
			Timestamp intervalStartDate, Timestamp intervalEndDate, UserInRole uir);
	
	List<List<Filter>> createAllFilters(List<List<Filter>> allVariablesFilters,
			List<List<Filter>> allPilotsFilters, List<List<Filter>> allCategoryFilters,
			List<List<Filter>> allTimesFilters);
	
	List<List<Filter>> createAllTimeFilters(OffsetDateTime intervalStartODT,
			OffsetDateTime intervalEndODT, String comparison, int numOfCategories);
	
	List<List<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison);
	
	List<List<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs);
	
	List<List<Filter>> createAllCategoryFilters(List<String> categories);
	
	List<List<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories);

	GenericTableData addGenericTableData(List<Filter> filter, Object[] data, Boolean comp, GenericTableData tableData, List<String> pilotCodes, Boolean hasCategories);
	
	List<GroupAnalyticsSeries> createSeries(boolean comparison, GenericTableData json);
	
	List<?> createGroups(List<String> categories,
			HashMap<String, List<String>> socioEconomics, List<String> datesStringList, boolean comparison, boolean comp);
	
	List<String> createDateList(String url);
	
	HashMap<String, List<String>> createSocioEconomicsMap();
	
	List<String> getPropertyFromURL(String url, String property);
	
	String buildMicroserviceURL(String urlQueryParams, HttpServletRequest req, ServletConfig sc, String path);

}
