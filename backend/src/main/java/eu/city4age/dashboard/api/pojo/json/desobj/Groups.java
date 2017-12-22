package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Groups{
	
	private String name;
	
	private int level;
	
	private String formula;
	
	private BigDecimal weight;
	
	private List<Gef> factors;
	
	public Groups(
			@JsonProperty(value= "factors",required = true) List<Gef> factors,
			@JsonProperty(value= "formula",required = false)String formula,
			@JsonProperty(value= "name",required = true)String name,
			@JsonProperty(value= "level",required = false)Integer level,
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		if (formula != null) this.formula = formula;
		else this.formula = "";
		if (level != null) this.level = level;
		else this.level = 0;
		this.name = name;
		this.weight = weight;
		this.factors = factors;		
		}

	public List<Gef> getFactors() {
		return factors;
	}

	public void setFactors(List<Gef> factors) {
		this.factors = factors;
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
