package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nui  {

	private String name;
	
	private String formula;
	
	private BigDecimal weight;
	
	public Nui(
			@JsonProperty(value= "name", required = true)String name, 
			@JsonProperty(value= "formula", required = true) String formula,
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		
		this.name = name;
		this.formula = formula;
		this.weight = weight;		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	
}
