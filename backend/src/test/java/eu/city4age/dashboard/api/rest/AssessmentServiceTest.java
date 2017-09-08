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
			String uri = "http://localhost:8080/C4A-dashboard/rest/assessment/getLastFiveForDiagram/userInRoleId/1/parentDetectionVariableId/1/intervalStart/2011-1-1/intervalEnd/2017-1-1";
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
	public void getDiagramDataTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getDiagramData/careRecipientId/1/parentFactorId/2";
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

	@Test
	public void findForSelectedDataSetTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/assessment/findForSelectedDataSet/geriatricFactorValueIds/38/35/39/36";
			HttpHeaders headers = rest.getForEntity(uri, String.class).getHeaders();
			ResponseEntity<String> response = rest.getForEntity(uri, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response);
			logger.info("3: " + response.getBody());

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
