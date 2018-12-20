package eu.city4age.dashboard.api.service;

import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

public interface PredictionService {

	void imputeAndPredict(Pilot pilot);

	void imputeAndPredictFor1User(UserInRole uir);
	
}
