package eu.city4age.dashboard.api.service;

import eu.city4age.dashboard.api.pojo.domain.Pilot;

public interface PredictionService {

	void imputeAndPredict(Pilot pilot);
	
}
