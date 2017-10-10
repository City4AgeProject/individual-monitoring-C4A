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

public class UserServiceTest {

	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);

	static protected RestTemplate rest = new TestRestTemplate();

	@Test
	public void loginTest() throws Exception {

		try {
			/*
			 * insert in db:
			 * 		cd_role: id = 6
			 *               id = 1
			 *      user_in_role: id = 1, pilot_code = LCC, user_in_system = 1, role_id = 6
			 *                    id = 2, pilot_code = LCC, user_in_system = 2, role_id = 1
			 *      pilot: id = 1, pilot_name = Lecce, pilot_code = LCC
			 */

			String uri1 = "http://localhost:8080/C4A-dashboard/rest/users/login/username/letizia/password/lventurin1";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("1.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/users/login/username/aaaa/password/aaaa";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("1.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/users/login/username/bbbb/password/bbbb";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("1.3: " + response3.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
