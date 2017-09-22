package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.persist.DetectionVariableRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.dto.C4AAndroidResponse;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;

public class PilotDetectionVariableServiceTest {

	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
		
	@Test
	public void updateConfigurationServiceTest() throws Exception {

		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/configuration/updateFromConfigFile";
			String input = "{\"name\": \"overall\",\"level\": 0,\"dateUpdated\": \"2017-09-07 00:00:00\",\"pilotCode\":\"MAD\",\"formula\":\"_TEST_FORMULA_\",\"weight\":1,\"groups\":[ {\"name\": \"contextual\",\"level\": 1,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"factors\": []}, {\"name\": \"behavioural\",\"level\": 1,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"factors\":[   {\"name\": \"motility\",\"level\": 2,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"subFactors\":[{\"name\":\"walking\",\"level\": 3,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"measures\":[{\"name\":\"walk_distance\",\"level\": 4,\"weight\": 0.7,\"formula\":\"_TEST_FORMULA_\"},{\"name\":\"walk_time_outdoor\",\"level\": 4,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\"},{\"name\":\"walk_speed_outdoor\",\"level\": 4,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\"}]}]},{\"name\": \"iadl\",\"level\": 2,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"subFactors\": [{\"name\":\"transportation_usage\",\"level\": 3,\"weight\": 1,\"formula\":\"_TEST_FORMULA_\",\"measures\":[{\"name\":\"publictransport_rides_month\",\"level\": 4,\"weight\": 0.8,\"formula\":\"_TEST_FORMULA_\"}]}]}]}]}";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(input, headers);

			ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);
			
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed: Http error code:" + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("4: " + response.getBody());


			try {
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
}
