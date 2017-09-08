package eu.city4age.dashboard.api.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

public class CareRecipientServiceTest {

	static protected Logger logger = LogManager.getLogger(CareRecipientServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	RestTemplate template = new TestRestTemplate();
	

	@Test
	public void getCareRecipientsTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getCareRecipients/pilotCode/LCC";
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
	public void getGroupsTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getGroups/careRecipientId/1/parentFactors/OVL/GFG";
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
			logger.info("3: " + response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	public void findOneTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/careRecipient/findOne/4";
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

}
