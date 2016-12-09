package eu.city4age.dashboard.api.ws;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Test;

public class AssessmentServiceTest {
	
	static protected Logger logger = Logger.getLogger(AssessmentServiceTest.class);
	
	@Test
	public void getDiagramDataTest() {

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);
			
			WebTarget WebTarget = client
					.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/getDiagramData");
			
			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.get(Response.class);

			/*if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/
			
			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);
			
			Object siftResult = response.readEntity(String.class);
			logger.info("1: " + siftResult);

			
		} catch (Exception e) {

			e.printStackTrace();

		}
	
	}
	
	
	@Test
	public void getLastFiveAssessmentsTest() {
		
		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/getLastFiveAssessmentsForDiagram");

			String input = "{\"timestampStart\":\"2016-01-01 00:00:00\",\"timestampEnd\":\"2017-01-01 00:00:00\",\"patientId\":1}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

			/*if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/
			
			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);
			
			Object siftResult = response.readEntity(String.class);
			logger.info("2: " + siftResult);

			
		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}
		
	
	
	@Test
	public void getAssessmentsForSelectedDataSetTest() {
	

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/getAssessmentsForSelectedDataSet");

			String input = "{\"geriatricFactorValueIds\":[1,2,3]}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

			/*if (response.getStatus() != 204) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);
			
			Object siftResult = response.readEntity(String.class);
			logger.info("3: " + siftResult);

		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}

	@Test
	public void getAssessmentsByFilerTest() {
	

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/getAssessmentsByFiler");
			
			String input = "{\"geriatricFactorValueIds\":[1,2,3],\"status\":[1,2,3],\"authorRoleId\":1,\"orderBy\":\"AUTHOR_NAME_ASC\"}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

			/*if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/

			logger.info("Output from Server .... ");
			String output = response.toString();
			logger.info(output);

			Object siftResult = response.readEntity(String.class);
			logger.info("4: " + siftResult);


		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Test
	public void addAssessmentsForSelectedDataSetTest() {

		Client client = ClientBuilder.newClient().register(JacksonFeature.class);

		WebTarget WebTarget = client
				.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/addAssessmentsForSelectedDataSet");
		
		String input = "{\"authorId\":1,\"comment\":\"My comment...\",\"riskStatus\":\"RISK_ALERT\",\"dataValidityStatus\":\"QUESTIONABLE_DATA\",\"geriatricFactorValueIds\":[1,2],\"audienceIds\":[1,2]}";

		Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

		/*if (response.getStatus() != 204) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}*/

		logger.info("Output from Server .... ");
		String output = response.toString();
		logger.info(output);
		
		Object siftResult = response.readEntity(String.class);
		logger.info("5: " + siftResult);


	}

}
