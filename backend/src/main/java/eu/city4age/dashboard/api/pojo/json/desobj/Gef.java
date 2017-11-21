package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gef {
	
	private String name;
	
	private int level;
	
	private String formula;
	
	private BigDecimal weight;

	private List<Ges> subFactors;
	
	public Gef(
			@JsonProperty(value= "subFactors",required = true)List<Ges> subFactors, 
			@JsonProperty(value= "formula",required = true)String formula, 
			@JsonProperty(value= "name", required = true)String name, 
			@JsonProperty(value= "level",required = true)Integer level, 
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		
		this.name = name;
		this.formula = formula;
		this.level = level;
		this.weight = weight;
		this.subFactors = subFactors;
		
	}

	public List<Ges> getSubFactors() {
		return subFactors;
	}

	public void setSubFactors(List<Ges> subFactors) {
		this.subFactors = subFactors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
