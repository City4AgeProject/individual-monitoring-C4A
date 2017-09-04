package eu.city4age.dashboard.api.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.utils.ValidationUtils;

public class MeasuresServiceTest {

	static protected Logger logger = LogManager.getLogger(MeasuresServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	

	@Test
	public final void configureDailyMeasuresTest() throws Exception {

		Resource schemaFile = new ClassPathResource("/JsonValidator.json", this.getClass());
		Resource jsonFile = new ClassPathResource("/json1.json", this.getClass());
		
		if (ValidationUtils.isJsonValid(schemaFile.getFile(), jsonFile.getFile())) {
			logger.info("Valid!");
		} else {
			logger.info("NOT valid!");
		}

		FileInputStream fis = null;
		String encoding = "utf-8";
		StringBuilder sb = new StringBuilder();

		try {
			fis = new FileInputStream(jsonFile.getFile());

			BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String jsonString = sb.toString();

		logger.info("##11" + jsonString);


		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/measures/configureDailyMeasures";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);

			ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);

			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed: Http error code:" + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("1: " + response.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	public final void computeDailyMeasuresTest() throws Exception {
		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/measures/computeDailyMeasures";
			String input = "{\"crId\":13,\"pilotCode\":\"LCC\",\"dvIds\":[91,95,98]}";

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

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
