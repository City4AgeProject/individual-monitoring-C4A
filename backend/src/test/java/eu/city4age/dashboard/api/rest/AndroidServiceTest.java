package eu.city4age.dashboard.api.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.dto.AddAssessment;
import eu.city4age.dashboard.api.pojo.dto.C4AAndroidResponse;
import eu.city4age.dashboard.api.pojo.json.AndroidActivitiesDeserializer;

/**
 * @author Andrija Petrovic
 *
 */
public class AndroidServiceTest {

	static protected Logger logger = LogManager.getLogger(AndroidServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Test
	public void AndroidServiceTest() throws Exception {

		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/android/postFromAndroid";
			String input = "{ \"ID\": \"3\", \"date\": \"Tue Aug 01 22:11:31 GMT+00:00 2017\","
					+ " \"activities\": [{\"type\": \"Walking\",\"start\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\","
					+ " \"end\": \"Tue Aug 01 22:05:02 GMT+00:00 2017\",\"gps\": [{\"longitude\": -122.084,"
					+ "\"latitude\": 37.421998333333, \"date\": \"Tue Aug 01 22:04:50 GMT+00:00 2017\"}]}]}";

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
			
				@SuppressWarnings("deprecation")
				C4AAndroidResponse data = objectMapper.reader(C4AAndroidResponse.class)
						.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);
		

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
