package eu.city4age.dashboard.api.service;

import java.util.List;
import java.util.Set;

import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.dto.OJDiagramFrailtyStatus;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;

public interface ViewService {

	DataIdValue createMonthLabel(Long timeIntervalId);
	
	Set<DataIdValue> createMonthLabels(List<ViewGefCalculatedInterpolatedPredictedValues> gefs);
	
	OJDiagramFrailtyStatus transformToDto(List<ViewGefCalculatedInterpolatedPredictedValues> gefs, Set<DataIdValue> months);
}
