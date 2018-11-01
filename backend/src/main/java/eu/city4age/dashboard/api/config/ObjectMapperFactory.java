package eu.city4age.dashboard.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * Jackson's ObjectMapper config.
 * 
 * @author milos.holclajtner
 */
public class ObjectMapperFactory {

	private static ObjectMapper objectMapper;

	static {

		objectMapper = new ObjectMapper();
		Hibernate5Module hbm = new Hibernate5Module();
		hbm.enable(Hibernate5Module.Feature.REQUIRE_EXPLICIT_LAZY_LOADING_MARKER);
		hbm.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
		objectMapper.registerModule(hbm);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		/*objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);*/
		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

	}

	public static ObjectMapper create() {
		return objectMapper;
	}

}