package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

public class Gef extends ElementWithFormula {
	
	private List<Ges> subFactors;

	public List<Ges> getSubFactors() {
		return subFactors;
	}

	public void setSubFactors(List<Ges> subFactors) {
		this.subFactors = subFactors;
	}

}
