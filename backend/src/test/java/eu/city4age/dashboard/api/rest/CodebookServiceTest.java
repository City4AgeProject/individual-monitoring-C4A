package eu.city4age.dashboard.api.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CodebookServiceTest {
	
	static protected Logger logger = LogManager.getLogger(CodebookServiceTest.class);
	
	static protected RestTemplate rest = new TestRestTemplate();
	
	@Test
	public void selectTableTest() throws Exception {
		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/codebook/selectTable/assessment";
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
	public void getAllRiskStatusTest() throws Exception {

		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/codebook/getAllRiskStatus";
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
	public void getAllRolesForStakeholderAbbrTest() throws Exception {
		
		try {
			String uri = "http://localhost:8080/C4A-dashboard/rest/codebook/getAllRolesForStakeholderAbbr/GES";
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
	
	//getAllDataValitidityStatusTest is not used

}
