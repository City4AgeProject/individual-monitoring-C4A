package eu.city4age.dashboard.api.service;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.AnalyticsDiagramData;
import eu.city4age.dashboard.api.pojo.dto.GenericTableData;
import eu.city4age.dashboard.api.pojo.dto.JsonToExcel;
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
			OffsetDateTime intervalEndODT);
	
	List<ArrayList<Filter>> createAllFiltersFromPilotCodes (List<String> pilotCodes, Boolean comparison);
	
	List<ArrayList<Filter>> createAllFiltersFromVariables (List<Long> detectionVariableIDs);
	
	List<ArrayList<Filter>> createAllCategoryFilters(List<String> categories);
	
	List<ArrayList<Filter>> createCategoryFilter(HashMap<String, List<String>> socioEconomics, List<String> categories);
	
	void writeToCsv (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException;
	
	void writeToXls (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException;
	
	void writeToXlsx (int viewSelecter, List<String> categories, List<AnalyticsDiagramData> data, File tmp) throws IOException;
	
	void writeToJSON (int viewSelecter, List<String> categories, GenericTableData data, File tmp) throws IOException;
	
	AnalyticsDiagramData createAnalyticsDiagramData (ArrayList<Filter> filter, Object[] data, Boolean comparison);
	
	JsonToExcel createExcelJson (List<AnalyticsDiagramData> data, List<String> categories, int viewSelecter);

	GenericTableData addGenericTableData(ArrayList<Filter> filter, Object[] data, Boolean comp, GenericTableData tableData, List<String> pilotCodes);
	
}
