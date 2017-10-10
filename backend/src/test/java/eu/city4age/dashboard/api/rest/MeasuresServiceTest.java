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
import eu.city4age.dashboard.api.pojo.json.Configuration;
import eu.city4age.dashboard.api.pojo.json.desobj.Gef;
import eu.city4age.dashboard.api.pojo.json.desobj.Ges;
import eu.city4age.dashboard.api.pojo.json.desobj.Groups;
import eu.city4age.dashboard.api.pojo.json.desobj.Mea;


public class MeasuresServiceTest {

	static protected Logger logger = LogManager.getLogger(MeasuresServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	public void getDailyMeasuresTest() throws Exception {

		try {
			/*
			 * insert in db for success: or run test/java/eu/city4age/dashboard/api/persist/VariationMeasureValueRepositoryTest::testFindByUserAndGes with rollback FALSE
			 * 		cd_detection_variable: id = 91, detection_variable_name = MEA1, detection_variable_type = MEA
			 *                             id = 95, detection_variable_name = MEA2, detection_variable_type = MEA
			 *                             id = 98, detection_variable_name = MEA3, detection_variable_type = MEA
			 *                             id = 514, detection_variable_name = GES, detection_variable_type = GES
			 *      cd_detection_variable_type: MEA, GES
			 *      md_pilot_detection_variable: id = 1, detection_variable_id = 91, derived_detection_variable_id = 514
			 *                                   id = 2, detection_variable_id = 95, derived_detection_variable_id = 514
			 *                                   id = 3, detection_variable_id = 98, derived_detection_variable_id = 514
			 *      time_interval: id = 1, interval_start: 2017-03-05 00:00:00, interval_end: 2017-03-05 00:00:00
			 *      user_in_role: id = 10
			 *      variation_measure_value: id = 1, user_in_role = 10, measure_type_id = 91, time_interval_id = 1
			 *                               id = 2, user_in_role = 10, measure_type_id = 95, time_interval_id = 1
			 *                               id = 3, user_in_role = 10, measure_type_id = 98, time_interval_id = 1
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/measures/getDailyMeasures/userInRoleId/10/gesId/514";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getDailyMeasures).... ");
			logger.info(response1);
			logger.info("1.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/measures/getDailyMeasures/userInRoleId/9/gesId/514";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getDailyMeasures).... ");
			logger.info(response2);
			logger.info("1.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/measures/getDailyMeasures/userInRoleId/10/gesId/513";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getDailyMeasures).... ");
			logger.info(response3);
			logger.info("1.3: " + response3.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
		
	@Test
	public final void computeFromMeasuresTest() throws Exception {
		
		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/measures/computeFromMeasures";
			HttpHeaders headers = rest.getForEntity(uri, String.class).getHeaders();
			ResponseEntity<String> response = rest.getForEntity(uri, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
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
	public void getNuiValuesTest() throws Exception {

		try {
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/measures/getNuiValues/userInRoleId/1/detectionVariableId/1";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getNuiValuesTest).... ");
			logger.info(response1);
			logger.info("3.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/measures/getNuiValues/userInRoleId/2/detectionVariableId/1";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getNuiValuesTest).... ");
			logger.info(response2);
			logger.info("3.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/measures/getNuiValues/userInRoleId/1/detectionVariableId/2";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server (INSIDE getNuiValuesTest).... ");
			logger.info(response3);
			logger.info("3.3: " + response3.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}
