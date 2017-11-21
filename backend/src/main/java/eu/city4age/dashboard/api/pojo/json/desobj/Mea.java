package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mea {
	
	private String name;
	
	private int level;
	
	private BigDecimal weight;
	
	private final String formula = "";
	
	private List<Nui> nuis;
	public Mea(
			@JsonProperty(value= "name", required = true)String name, 
			@JsonProperty(value= "level",required = true)Integer level, 
			@JsonProperty(value= "weight",required = true)BigDecimal weight,
			@JsonProperty(value= "nuis", required = true) List<Nui> nuis) {
		this.name = name;
		this.level = level;
		this.weight = weight;
		this.nuis = nuis;
	}
	public List<Nui> getNuis() {
		return nuis;
	}
	public void setNuis(List<Nui> nuis) {
		this.nuis = nuis;
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
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public String getFormula() {
		return formula;
	}
}
