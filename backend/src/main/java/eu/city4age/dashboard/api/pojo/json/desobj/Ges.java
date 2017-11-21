package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ges {
	
	private String name;
	
	private String formula;
	
	private int level;
	
	private BigDecimal weight;

	private List<Mea> measures;
	
	public Ges(
			@JsonProperty(value= "measures",required = true)List<Mea> measures,
			@JsonProperty(value= "formula",required = true)String formula,
			@JsonProperty(value= "name", required = true)String name,
			@JsonProperty(value= "level",required = true)Integer level,
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		this.name = name;
		this.formula = formula;
		this.level = level;
		this.weight = weight;
		this.measures = measures;
	}

	public List<Mea> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Mea> measures) {
		this.measures = measures;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	

}
