package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

public class Ges extends ElementWithFormula {
	
	private List<Mea> measures;

	public List<Mea> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Mea> measures) {
		this.measures = measures;
	}

}
