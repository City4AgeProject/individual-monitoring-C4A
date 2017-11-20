package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gef extends ElementWithFormula {

	private List<Ges> subFactors;
	
	public Gef(
			@JsonProperty(value= "subFactors",required = true)List<Ges> subFactors, 
			@JsonProperty(value= "formula",required = true)String formula, 
			@JsonProperty(value= "name", required = true)String name, 
			@JsonProperty(value= "level",required = true)Integer level, 
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		super(formula, name, level, weight);
		this.subFactors = subFactors;
		
	}

	public List<Ges> getSubFactors() {
		return subFactors;
	}

	public void setSubFactors(List<Ges> subFactors) {
		this.subFactors = subFactors;
	}

}
