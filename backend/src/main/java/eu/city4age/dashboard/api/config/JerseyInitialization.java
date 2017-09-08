package eu.city4age.dashboard.api.config;

import javax.annotation.PostConstruct;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/*import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;*/


/**
 * Custom Jersey initialization.
 * 
 * @author milos.holclajtner
 */
@Component
public class JerseyInitialization extends ResourceConfig {
	
	@Value("${spring.jersey.application-path:/}")
	private String apiPath;

	/**
	 * Register JAX-RS application components.
	 */
	public JerseyInitialization() {
		packages("eu.city4age.dashboard.api");
		this.registerEndpoints();
	}
	
	private void registerEndpoints() {
		property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
		property(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
		property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

		this.register(new JacksonJsonProvider(ObjectMapperFactory.create()));
		this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		this.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		this.packages(true, "eu.city4age.dashboard.api.rest");		
	}
	
	@PostConstruct
	public void init() {
		// Register components where DI is needed
		this.configureSwagger();
	}
	
	
	private void configureSwagger() {

		BeanConfig config = new BeanConfig();
		config.setConfigId("C4A-dashboard");
		config.setTitle("City4Age Assessments REST Web Services.");
		config.setVersion("v1");
		config.setContact("milos.holclajtner");
		config.setSchemes(new String[] { "http", "https" });
		config.setBasePath(this.apiPath);
		config.setResourcePackage("eu.city4age.dashboard.api.rest");
		config.setPrettyPrint(true);
		config.setScan(true);
		
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

	}

}