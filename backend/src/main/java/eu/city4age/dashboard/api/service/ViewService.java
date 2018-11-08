package eu.city4age.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.persist.Filter;

public interface ViewService {
	
	TreeSet<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs);
	
	OJDiagramFrailtyStatus transformToDto(List<ViewGefCalculatedInterpolatedPredictedValues> gefs, TreeSet<DataIdValue> months);

	List<ViewGefCalculatedInterpolatedPredictedValues> convertToViewGFVs(List<DerivedMeasureValue> derivedMeasures);
	
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
	
}