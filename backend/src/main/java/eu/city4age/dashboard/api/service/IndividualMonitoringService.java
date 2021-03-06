package eu.city4age.dashboard.api.service;

import java.util.List;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;

public interface IndividualMonitoringService {
	
	TreeSet<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs);
	
	OJDiagramFrailtyStatus transformToDto(List<ViewGefCalculatedInterpolatedPredictedValues> gefs, TreeSet<DataIdValue> months);

	List<ViewGefCalculatedInterpolatedPredictedValues> convertToViewGFVs(List<DerivedMeasureValue> derivedMeasures);
	
	String[] getColorsOfClusters (int num);
	
	String getClusteredSeries(String path, Long userInRoleId, Long varId) throws Exception;
	
}