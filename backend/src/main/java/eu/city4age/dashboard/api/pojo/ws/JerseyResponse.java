package eu.city4age.dashboard.api.pojo.ws;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class JerseyResponse {

	public static Response build() {
		return Response
				.noContent()
				.build();
	}
	
	public static Response build(Object response) {
		return Response
				.ok(response)
				.build();
	}
	
	public static Response build(Object response, int responseCode) {
		return Response
				.status(responseCode)
				.entity(response)
				.build();
	}
	
	public static Response buildTextPlain(Object response) {
		return Response
				.ok(response)
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
	
	public static Response buildTextPlain(Object response, int responseCode) {
		return Response
				.status(responseCode)
				.entity(response)
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

	public static Response buildFile(Object response, String filename) {
		return Response
				.ok(response)
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=" + filename)
				.build();
	}

	public static Response buildFile(Object response, int responseCode, String filename) {
		return Response
				.status(responseCode)
				.entity(response)
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=" + filename)
				.build();
	}

}
