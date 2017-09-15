package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

public class Groups extends ElementWithFormula {
	
	private List<Gef> factors;

	public List<Gef> getFactors() {
		return factors;
	}

	public void setFactors(List<Gef> factors) {
		this.factors = factors;
	}

}
