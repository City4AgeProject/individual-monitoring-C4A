package eu.city4age.dashboard.api.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;

public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		super();
		super.registerModule(new Hibernate3Module());	
		super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		super.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
	}

}
