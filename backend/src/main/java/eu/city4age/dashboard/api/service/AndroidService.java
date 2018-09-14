package eu.city4age.dashboard.api.service;

import java.util.HashMap;

import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;

public interface AndroidService {
	
	void storeInfoInDb (AndroidActivitiesDeserializer data);
	
	HashMap <String, String> generateQuestion ();

}
