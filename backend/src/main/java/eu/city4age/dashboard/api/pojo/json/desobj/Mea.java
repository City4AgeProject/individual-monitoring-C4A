package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mea extends Element {
	
	public Mea(
			@JsonProperty(value= "name", required = true)String name, 
			@JsonProperty(value= "level",required = true)Integer level, 
			@JsonProperty(value= "weight",required = true)BigDecimal weight) {
		super( name, level, weight);
	}

}
