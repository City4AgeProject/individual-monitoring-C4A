package eu.city4age.dashboard.api.service;

import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

public interface ExcludeService {

	void excludeMeasures(List<VariationMeasureValue> vmvMonthly);

}
