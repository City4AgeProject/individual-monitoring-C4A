package eu.city4age.dashboard.api.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

public class AssessmentServiceTest {

	static protected Logger logger = LogManager.getLogger(AssessmentServiceTest.class);

	@Test
	public void getDiagramDataTest() {
		
		logger.info("start of getDiagramDataTest");

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client.target("http://localhost:8080/C4A/rest/assessments/getDiagramData");

			String input = "{\"timestampStart\":\"2016-01-01 00:00:00\",\"timestampEnd\":\"2017-01-01 00:00:00\",\"crId\":1,\"dvParentId\":4}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), Response.class);

			if (response.getStatus() != 200) {

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			}

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);

			Object siftResult = response.readEntity(String.class);
			logger.info("1: " + siftResult);

		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();

		}
		
		logger.info("end of getDiagramDataTest");

	}

	@Test
	public void getLastFiveAssessmentsTest() {
		
		logger.info("start of getLastFiveAssessmentsTest");

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/C4A/rest/assessments/getLastFiveAssessmentsForDiagram");

			String input = "{\"timestampStart\":\"2016-01-01 00:00:00\",\"timestampEnd\":\"2017-01-01 00:00:00\",\"crId\":1}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), Response.class);

			if (response.getStatus() != 200) {

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			}

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);

			Object siftResult = response.readEntity(String.class);
			logger.info("2: " + siftResult);

		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();

		}
		
		logger.info("end of getLastFiveAssessmentsTest");

	}

	@Test
	public void getAssessmentsForSelectedDataSetTest() {
		
		logger.info("start of getAssessmentsForSelectedDataSetTest");

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/C4A/rest/assessments/getAssessmentsForSelectedDataSet");

			String input = "{\"geriatricFactorValueIds\":[1,2,3],\"status\":[true,false,true,false,true],\"authorRoleId\":1,\"orderBy\":\"AUTHOR_NAME_ASC\"}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), Response.class);

			if (response.getStatus() != 204) {

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			}

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);

			Object siftResult = response.readEntity(String.class);
			logger.info("3: " + siftResult);

		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();

		}
		
		logger.info("end of getAssessmentsForSelectedDataSetTest");

	}

	@Test
	public void addAssessmentsForSelectedDataSetTest() {
		
		logger.info("start of addAssessmentsForSelectedDataSetTest");

		try {
			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/C4A/rest/assessments/addAssessmentsForSelectedDataSet");

			String input = "{\"authorId\":1,\"comment\":\"My comment...\",\"riskStatus\":\"A\",\"dataValidity\":\"QUESTIONABLE_DATA\",\"geriatricFactorValueIds\":[1,2],\"audienceIds\":[1,2]}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input, MediaType.APPLICATION_JSON_TYPE), Response.class);

			if (response.getStatus() != 204) {

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			}

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);

			Object siftResult = response.readEntity(String.class);
			logger.info("5: " + siftResult);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail();

		}
		logger.info("end of addAssessmentsForSelectedDataSetTest");

	}

}
