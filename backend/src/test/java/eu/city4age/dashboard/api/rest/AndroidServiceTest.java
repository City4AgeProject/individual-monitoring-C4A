package eu.city4age.dashboard.api.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;

/**
 * @author Andrija Petrovic
 *
 */
public class AndroidServiceTest extends WebMvcConfigurationSupport {

	static protected Logger logger = LogManager.getLogger(AndroidServiceTest.class);

	static protected TestRestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
	
	@Test
	public void AndroidServiceTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/android/postFromAndroid";
			/*
			 * add these to db for successfull test:
			 * user_in_role: ID = 3, user_in_system = 1 (assumed user_in_system exists. if not add), role_id = 1
			 * cd_role: id = 1
			*/
			
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
			logger.info("1: " + response.getBody());


			try {
				logger.info("BODY::" + (response.getBody().getClass()));

				String json = response.getBody();
				
				HttpHeaders headers2 = new HttpHeaders();
				headers2.setContentType(MediaType.APPLICATION_JSON);
				entity = new HttpEntity<String>(input, headers2);
				ResponseEntity<String> response2 = rest.exchange(uri, HttpMethod.GET, entity, String.class);
				if (!response2.getStatusCode().equals(HttpStatus.METHOD_NOT_ALLOWED)) {
					throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
				}
				logger.info("Output from Server .... ");
				logger.info(response2);
				logger.info("2: " + response2.getBody());

			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
			
			String input2 = input.substring(0, 9) + '4' + input.substring(10);
			logger.info("input2: " + input2);
			entity = new HttpEntity<String>(input2, headers);
			ResponseEntity<String> response3 = rest.exchange(uri, HttpMethod.POST, entity, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed: Http error code:" + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("3: " + response3.getBody());


		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	


}
