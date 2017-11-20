package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Groups extends ElementWithFormula {
	
	private List<Gef> factors;
	
	public Groups(
			@JsonProperty(value= "factors",required = true) List<Gef> factors,
			@JsonProperty(value= "formula",required = true)String formula,
			@JsonProperty(value= "name",required = true)String name,
			@JsonProperty(value= "level",required = true)Integer level,
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		super(formula,name,level,weight);
		this.factors = factors;
		
		}

	public List<Gef> getFactors() {
		return factors;
	}

	public void setFactors(List<Gef> factors) {
		this.factors = factors;
	}

}
