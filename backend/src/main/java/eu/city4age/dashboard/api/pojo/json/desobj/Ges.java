package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ges extends ElementWithFormula {

	private List<Mea> measures;
	
	public Ges(
			@JsonProperty(value= "measures",required = true)List<Mea> measures,
			@JsonProperty(value= "formula",required = true)String formula,
			@JsonProperty(value= "name", required = true)String name,
			@JsonProperty(value= "level",required = true)Integer level,
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		super(formula, name, level, weight);
		this.measures = measures;
	}

	public List<Mea> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Mea> measures) {
		this.measures = measures;
	}

}
