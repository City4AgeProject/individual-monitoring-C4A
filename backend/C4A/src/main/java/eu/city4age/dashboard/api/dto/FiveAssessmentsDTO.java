package eu.city4age.dashboard.api.dto;

import eu.city4age.dashboard.api.model.AssessedGefValueSet;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

public class FiveAssessmentsDTO {
	
	private TimeInterval ti;

	public TimeInterval getTi() {
		return ti;
	}

	public void setTi(TimeInterval ti) {
		this.ti = ti;
	}

	public void setGfv(GeriatricFactorValue gfv) {}
	
	public void setAgvs(AssessedGefValueSet agvs) {}
	
	public void setAa(Assessment aa) {}

}
