package eu.city4age.dashboard.api.config;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Custom Jersey initialization.
 * 
 * @author milos.holclajtner
 */
public class JerseyInitialization extends ResourceConfig {

	/**
	 * Register JAX-RS application components.
	 */
	public JerseyInitialization() {
		property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
		property(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
		property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

		this.register(new JacksonJsonProvider(ObjectMapperFactory.create()));
		this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		this.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		this.packages(true, "eu.city4age.dashboard.api.rest");
	}

}