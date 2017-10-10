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

public class AssessmentServiceTest {

	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();

	@Test
	public void getLastFiveForDiagramTest() throws Exception {
		try {
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/assessment/getLastFiveForDiagram/userInRoleId/1/parentDetectionVariableId/4/intervalStart/2015-1-1/intervalEnd/2017-1-1";
			/*
			 * add these to db for success: or run test/java/eu/city4age/dashboard/api/persist/TimeIntervalRepositoryTest::testFindLastFiveAssessmentsForDiagram with rollback FALSE
			 * 	time_interval: id = 1, interval_start = 2016-01-01 00:00:00, interval_end = 2016-02-01 00:00:00, typical_period = MON
			 *                 id = 2, interval_start = 2016-02-01 00:00:00, interval_end = 2016-03-01 00:00:00, typical_period = MON
			 *                 id = 3, interval_start = 2016-03-01 00:00:00, interval_end = 2016-04-01 00:00:00, typical_period = MON
			 *                 id = 4, interval_start = 2016-04-01 00:00:00, interval_end = 2016-05-01 00:00:00, typical_period = MON
			 *                 id = 5, interval_start = 2016-05-01 00:00:00, interval_end = 2016-06-01 00:00:00, typical_period = MON
			 *                 id = 6, interval_start = 2016-06-01 00:00:00, interval_end = 2016-07-01 00:00:00, typical_period = MON
			 *                 id = 7, interval_start = 2016-07-01 00:00:00, interval_end = 2016-08-01 00:00:00, typical_period = MON
			 *                 
			 *  cd_detection_variable: id = 4
			 *                         id = 1, derived_variable_id = 4
			 *  md_pilot_detection_variable: id = 1, pilot_code = LCC, detection_variable_id = 1, derived_detection_variable_id = 4
			 *                               id = 2, pilot_code = ATH, detection_variable_id = 1, derived_detection_variable_id = 4
			 *  user_in_role: id = 1, pilot_code = LCC, user_in_system = 1 (added before, if not add)
			 *                id = 2, pilot_code = ATH, user_in_system = 1
			 *  geriatric_factor_value: id = 1, gef_value = 1, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 1
			 *                          id = 2, gef_value = 2, time_interval_id = 2, gef_type_id = 1, user_in_role_id = 2,
			 *  assessment: id = 1, assessment_comment = my_comment, risk_status = A, created: 2017-05-22 12:00:00, author_id = 1
			 *              id = 2, assessment_comment = my_comment2, risk_status = A, created: 2017-05-22 12:00:00, author_id = 2
			 *  assessed_gef_value_id: gef_value_id = 1, assessment_id = 1
			 *                         gef_value_id = 2, assessment_id = 2
			 *  cd_typical_period: typical_period = MON, period_description = Month
			 */
			
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("1.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/assessment/getLastFiveForDiagram/userInRoleId/2/parentDetectionVariableId/4/intervalStart/2015-1-1/intervalEnd/2017-1-1";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("1.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/assessment/getLastFiveForDiagram/userInRoleId/1/parentDetectionVariableId/1/intervalStart/2015-1-1/intervalEnd/2017-1-1";
			// because of null values in time_interval, shows them all, it will not happen if db filled correctly
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("1.3: " + response3.getBody());
			
			String uri4 = "http://localhost:8080/C4A-dashboard/rest/assessment/getLastFiveForDiagram/userInRoleId/2/parentDetectionVariableId/4/intervalStart/2017-1-1/intervalEnd/2018-1-1";
			ResponseEntity<String> response4 = rest.getForEntity(uri4, String.class);
			if (!response4.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response4);
			logger.info("1.4: " + response4.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
    
	@Test
	public void findForSelectedDataSetTest() throws Exception {

		try {
			/*
			 * add these to db for success: or run test/java/eu/city4age/dashboard/api/persist/AssessmentRepositoryTest::testFindForSelectedDataSet with rollback FALSE
			 * 		assessed_gef_value_set: gef_value_id = 1, assessment_id = 1
			 *                              gef_value_id = 1, assessment_id = 2
			 *                              gef_value_id = 1, assessment_id = 3
			 *                              gef_value_id = 1, assessment_id = 4
			 *                              gef_value_id = 2, assessment_id = 5
			 *                              gef_value_id = 2, assessment_id = 6
			 *      assessment: id = 1, risk_status = W, data_validity_status = F, author_id = 1
			 *                  id = 2, risk_status = W, data_validity_status = F, author_id = 2
			 *                  id = 3, risk_status = W, data_validity_status = Q, author_id = 1
			 *                  id = 4, risk_status = A, data_validity_status = F, author_id = 1
			 *                  id = 5, risk_status = W, data_validity_status = F, author_id = 1
			 *                  id = 6,
			 *                  id = 7
			 *      assessment_audience_role = assessment_id = 1, role_id = 1
			 *                                 assessment_id = 2, role_id = 2
			 *                                 assessment_id = 3, role_id = 1
			 *                                 assessment_id = 4, role_id = 1
			 *                                 assessment_id = 5, role_id = 1
			 *      cd_role: id = 1,
			 *               id = 2
			 *      geriatric_factor_value: id = 1, gef_value = 5, user_in_role_id = 1
			 *                              id = 2, gef_value = 6, user_in_role_id = 2
			 *      user_in_role: id = 1
			 *                    id = 2
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/assessment/findForSelectedDataSet/geriatricFactorValueIds/1/3/4";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("2.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/assessment/findForSelectedDataSet/geriatricFactorValueIds/2/3/4";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("2.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/assessment/findForSelectedDataSet/geriatricFactorValueIds/1/2/3/4";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("2.3: " + response3.getBody());
			
			String uri4 = "http://localhost:8080/C4A-dashboard/rest/assessment/findForSelectedDataSet/geriatricFactorValueIds/3/4";
			ResponseEntity<String> response4 = rest.getForEntity(uri4, String.class);
			if (!response4.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response4.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response4);
			logger.info("2.4: " + response4.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/*
	 * To Test deleteForSelectedDataSetTest() alone comment out everything in
	 * addForSelectedDataSetTest() from the, and including, first try block to
	 * the, and including, second catch block and run it once than use last
	 * generated ASSESSMENT_ID in assessment table in
	 * deleteForSelectedDataSetTest() and run it
	 */
	@Ignore
	@Test
	public void deleteForSelectedDataSetTest() {

		try {

			String uri = "http://localhost:8080/C4A-dashboard/rest/assessment/deleteAssessment/ASSESSMENT_ID";
			HttpHeaders headers = rest.getForEntity(uri, String.class).getHeaders();
			ResponseEntity<String> response = rest.getForEntity(uri, String.class);
			logger.info(response.getStatusCode());

			if (!response.getStatusCode().toString().equals("200")) {
				throw new RuntimeException("Failed : HTTP - error code : " + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("5: " + response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void addForSelectedDataSetTest() throws Exception {

		try {
			rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			String uri = "http://localhost:8080/C4A-dashboard/rest/assessment/addForSelectedDataSet";
			String input = "{\"authorId\":1,\"comment\":\"***FROM TEST DELETE***\",\"riskStatus\":\"A\",\"dataValidity\":\"QUESTIONABLE_DATA\",\"geriatricFactorValueIds\":[2,1],\"audienceIds\":[1,2]}";

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
				logger.info("BODY::" + (response.getBody()));
				String json = response.getBody();
				
			
				@SuppressWarnings("deprecation")
				AddAssessment data = objectMapper.reader(AddAssessment.class)
						.with(DeserializationFeature.READ_ENUMS_USING_TO_STRING).readValue(json);


				logger.info("ID from json::" + data.getId());

				String id = data.getId().toString();
				uri = "http://localhost:8080/C4A-dashboard/rest/assessment/deleteAssessment/" + id;

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
