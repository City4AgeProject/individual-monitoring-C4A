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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.pojo.dto.AddAssessment;

public class MeasuresServiceTest {

	static protected Logger logger = LogManager.getLogger(MeasuresServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	public final void computeMeasuresTest() throws Exception {
		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/measures/computeMeasures";

			HttpHeaders headers = rest.getForEntity(uri, String.class).getHeaders();
			ResponseEntity<String> response = rest.getForEntity(uri, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("2: " + response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

}
