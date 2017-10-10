package eu.city4age.dashboard.api.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;

public class PilotDetectionVariableServiceTest {

	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
		
	@Test
	public void updateConfigurationServiceTest() throws Exception {

		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/configuration/updateFromConfigFile";
			String input = "{\"configurations\":[{\"name\":\"overall\",\"level\":0,\"validFrom\":\"2016-01-01 00:00:01\",\"validTo\":\"2020-09-07 00:00:00\",\"pilotCode\":\"MAD\",\"groups\":[{\"name\":\"contextual\",\"level\":1,\"weight\":0.5,\"formula\":\"\",\"factors\":[]},{\"name\":\"behavioural\",\"level\":1,\"weight\":0.5,\"formula\":\"\",\"factors\":[{\"name\":\"motility\",\"level\":2,\"weight\":0.13,\"formula\":\"\",\"subFactors\":[{\"name\":\"walking\",\"level\":3,\"weight\":1,\"formula\":\"\",\"measures\":[{\"name\":\"walk_distance\",\"level\":4,\"weight\":0.4},{\"name\":\"walk_time_outdoor\",\"level\":4,\"weight\":0.3}]}]}]}]}]}";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(input, headers);

			ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);
			
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed: Http error code:" + response.getStatusCode() + response.getBody());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("4: " + response.getBody());


			/*try {
				logger.info("BODY::" + (response.getBody().getClass()));

				String json = response.getBody();
			
				HttpHeaders headers2 = rest.getForEntity(uri, String.class).getHeaders();
				ResponseEntity<String> response2 = rest.getForEntity(uri, String.class);
				if (!response.getStatusCode().equals(HttpStatus.OK)) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
				}
				logger.info("Output from Server .... ");
				logger.info(response);
				logger.info("5: " + response.getBody());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			} */

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
}
