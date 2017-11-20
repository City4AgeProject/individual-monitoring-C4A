package eu.city4age.dashboard.api.pojo.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.city4age.dashboard.api.pojo.json.desobj.Configuration;

public class ConfigureDailyMeasuresDeserializer {

	private List<Configuration> configurations;
	
	public ConfigureDailyMeasuresDeserializer(
			@JsonProperty(value= "configurations", required = true)List<Configuration> configurations ) {
		this.configurations = configurations;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

}
