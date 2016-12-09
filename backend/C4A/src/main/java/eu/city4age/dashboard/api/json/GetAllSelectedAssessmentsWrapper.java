package eu.city4age.dashboard.api.json;

import java.util.List;

public class GetAllSelectedAssessmentsWrapper {
	
	List<Long> geriatricFactorValueIds;

	public List<Long> getGeriatricFactorValueIds() {
		return geriatricFactorValueIds;
	}

	public void setGeriatricFactorValueIds(List<Long> geriatricFactorValueIds) {
		this.geriatricFactorValueIds = geriatricFactorValueIds;
	}

}
