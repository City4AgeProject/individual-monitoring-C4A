package eu.city4age.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
	
	List<ArrayList<Filter>> createAllFilters(List<ArrayList<Filter>> allVariablesFilters,
			List<ArrayList<Filter>> allPilotsFilters, List<ArrayList<Filter>> allCategoryFilters,
			List<ArrayList<Filter>> allTimesFilters);
	
	List<ArrayList<Filter>> createAllTimeFilters(OffsetDateTime intervalStartODT,
			OffsetDateTime intervalEndODT, boolean comparison);
	
	List<ArrayList<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison);
	
	List<ArrayList<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs);
	
	List<ArrayList<Filter>> createAllCategoryFilters(List<String> categories);
	
	List<ArrayList<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories);

	GenericTableData addGenericTableData(ArrayList<Filter> filter, Object[] data, Boolean comp, GenericTableData tableData, List<String> pilotCodes);
	
}
