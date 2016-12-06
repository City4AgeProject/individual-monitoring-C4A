package eu.city4age.dashboard.api.ws;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Test;

public class AssessmentServiceTest {
	
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
			
			System.out.println("Output from Server .... ");
			String output = response.toString();
			System.out.println(output);
			
			Object siftResult = response.readEntity(String.class);
			System.out.println(siftResult);

			
		} catch (Exception e) {

			e.printStackTrace();

		}
	
	}
	
	@Test
	public void getAssessmentsForSelectedDataSetTest() {
	

		try {

			Client client = ClientBuilder.newClient().register(JacksonFeature.class);

			WebTarget WebTarget = client
					.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/selectedDataSet");

			String input = "{\"factor\":\"Climbing stairs\",\"group\":\"Jan 2016\"}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

			/*if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/

			System.out.println("Output from Server .... ");
			String output = response.toString();
			System.out.println(output);

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
			
			String input = "{\"factor\":\"Climbing stairs\",\"group\":\"Jan 2016\"}";

			Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

			/*if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}*/

			System.out.println("Output from Server .... ");
			String output = response.toString();
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Test
	public void addAssessmentsForSelectedDataSetTest() {

		Client client = ClientBuilder.newClient().register(JacksonFeature.class);

		WebTarget WebTarget = client
				.target("http://localhost:8080/api-1.0-SNAPSHOT/v1/assessments/addAssessmentsForSelectedDataSet");
		
		String input = "{\"factor\":\"Climbing stairs\",\"group\":\"Jan 2016\"}";

		Response response = WebTarget.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(input,MediaType.APPLICATION_JSON_TYPE), Response.class);

		/*if (response.getStatus() != 204) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}*/

		System.out.println("Output from Server .... ");
		String output = response.toString();
		System.out.println(output);

	}

}
