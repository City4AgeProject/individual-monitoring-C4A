package eu.city4age.dashboard.api.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

public class CareRecipientServiceTest {

	static protected Logger logger = LogManager.getLogger(CareRecipientServiceTest.class);

	static protected TestRestTemplate rest = new TestRestTemplate();

	TestRestTemplate template = new TestRestTemplate();
	

	@Test
	public void getCareRecipientsTest() throws Exception {

		try {
			/*
			 * insert in db for success: (or run test/java/eu/cit4age/dashboard/api/persist/UserInRoleRepositoryTest::testFindByRoleIdAndPilotCode with rollback FALSE
			 * 		cd_role: id = 1
			 *               id = 2
			 *      pilot: id = 1, name = Lecce, pilot_code = LCC
			 *             id = 2, name = Athens, pilot_code = ATH
			 *      user_in_role: id = 1, pilot_code = LCC, role_id = 1
			 *                    id = 2, pilot_code = LCC, role_id = 1
			 *                    id = 3, pilot_code = ATH, role_id = 1
			 *                    id = 4, pilot_code = ATH, role_id = 2
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getCareRecipients/pilotCode/LCC";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("1.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getCareRecipients/pilotCode/ATH";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("1.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getCareRecipients/pilotCode/MPL";
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

	@Test
	public void getGroupsTest() throws Exception {

		try {
			/*
			 * add these in db for successful test 
			 * (or run test/java/eu/city4age/dashboard.api.persist.TimeIntervalRepositoryTest::testGetGroups with rollback FALSE)
			 * 		cd_detection_variable: id = 1, detection_variable_name = DV1, detection_variable_type = GEF
			 *                             id = 2, detection_variable_name = DV2, detection_variable_type = GES
			 *      cd_detection_variable_type: detection_variable_type = GEF
			 *                                  detection_variable_type = GES
			 *      cd_typical_period: typical_period = MON
			 *      cd_frailty_status: Frail
			 *      frailty_status_timeline: time_interval_id = 1, changed_by = 1, changed = any_date, user_in_role_id = 1, frailty_status = Frail
			 *                               time_interval_id = 2, changed_by = 2, changed = any_date, user_in_role_id = 2, frailty_status = Frail
			 *      geriatric_factor_value: id = 1, gef_value = 3, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 1
			 *                              id = 2, gef_value = 4, time_interval_id = 2, gef_type_id = 1, user_in_role_id = 1
			 *                              id = 3, gef_value = 5, time_interval_id = 3, gef_type_id = 2, user_in_role_id = 1
			 *                              id = 4, gef_value = 6, time_interval_id = 4, gef_type_id = 1, user_in_role_id = 2
			 *      time_interval: id = 1, time_interval_start: 2016-01-01 00:00:00, time_interval_end: 2016-02-01 00:00:00, typical_period: MON
			 *                     id = 2, time_interval_start: 2016-02-01 00:00:00, time_interval_end: 2016-03-01 00:00:00, typical_period: MON
			 *                     id = 3, time_interval_start: 2016-03-01 00:00:00, time_interval_end: 2016-04-01 00:00:00, typical_period: MON
			 *                     id = 4, time_interval_start: 2016-04-01 00:00:00, time_interval_end: 2016-05-01 00:00:00, typical_period: MON
			 *                     id = 5, time_interval_start: 2016-05-01 00:00:00, time_interval_end: 2016-06-01 00:00:00, typical_period: MON
			 *      user_in_role: id = 1, user_in_system = 1
			 *                    id = 2, user_in_system = 2
			 *      user_in_system: id = 1, username = aaaa, password = aaaa, display_name = aaaa
			 *                      id = 2, username = bbbb, password = bbbb, display_name = bbbb
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getGroups/careRecipientId/1/parentFactors/GES/GEF";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("2.1: " + response1.getBody());
			
			//test1 displays error, 
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getGroups/careRecipientId/2/parentFactors/GES/GEF";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("2.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getGroups/careRecipientId/1/parentFactors/GES";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("2.3: " + response3.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void getDiagramDataTest() throws Exception {

		try {
			/*
			 * add these in db for success: or run test/java/eu/city4age/dashboard/api/persist/GeriatricFactorRepositoryTest::testFindByDetectionVariableId with rollback FALSE
			 * 		cd_detection_variable: id = 1, detection_variable_name = DV1
			 *                             id = 2, detection_variable_name = DV2
			 *      cd_detection_variable_type: GEF
			 *      geriatric_factor_value: id = 1, gef_value = 1, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 1
			 *                              id = 2, gef_value = 2, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 1
			 *                              id = 3, gef_value = 3, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 1
			 *                              id = 4, gef_value = 4, time_interval_id = 1, gef_type_id = 2, user_in_role_id = 1
			 *                              id = 5, gef_value = 5, time_interval_id = 1, gef_type_id = 1, user_in_role_id = 2
			 *      md_pilot_detection_variable: id = 1, pilot_code = LCC, detection_variable_id = 1, derived_detection_variable_id = 2
			 *                                   id = 2, pilot_code = LCC, detection_variable_id = 2, derived_detection_variable_id = 1
			 *      time_interval: id = 1, interval_start = 2016-01-01 00:00:00, typical_period = MON
			 *      user_in_role: id = 1, pilot_code = LCC
			 *                    id = 2, pilot_code = LCC
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getDiagramData/careRecipientId/1/parentFactorId/2";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("3.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getDiagramData/careRecipientId/2/parentFactorId/2";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("3.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getDiagramData/careRecipientId/1/parentFactorId/1";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("3.3: " + response3.getBody());
			
			String uri4 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/getDiagramData/careRecipientId/2/parentFactorId/1";
			ResponseEntity<String> response4 = rest.getForEntity(uri4, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response4.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response4);
			logger.info("3.4: " + response4.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test
	public void findOneTest() throws Exception {

		try {
			/*
			 * insert in db:
			 * 	cr_profile: id = 1, user_in_role = 1, gender = t
			 *              id = 2, user_in_role = 2, gender = f
			 *              id = 3, gender = t
			 *  user_in_role: id = 1
			 *                id = 2      
			 */
			String uri1 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/findOne/1";
			ResponseEntity<String> response1 = rest.getForEntity(uri1, String.class);
			if (!response1.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response1.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response1);
			logger.info("4.1: " + response1.getBody());
			
			String uri2 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/findOne/2";
			ResponseEntity<String> response2 = rest.getForEntity(uri2, String.class);
			if (!response2.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response2);
			logger.info("4.2: " + response2.getBody());
			
			String uri3 = "http://localhost:8080/C4A-dashboard/rest/careRecipient/findOne/3";
			ResponseEntity<String> response3 = rest.getForEntity(uri3, String.class);
			if (!response3.getStatusCode().equals(HttpStatus.OK)) {
				throw new RuntimeException("Failed : HTTP error code : " + response3.getStatusCode());
			}
			logger.info("Output from Server .... ");
			logger.info(response3);
			logger.info("4.3: " + response3.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

}
