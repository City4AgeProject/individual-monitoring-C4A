package eu.city4age.dashboard.api.pojo.ws;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class JerseyResponse {


	/*private static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	private static final String MAX_AGE = "Access-Control-Max-Age";
	private static final String ALL_HOSTS = "*";*/
	//private static final String LOCAL_HOST_8383 = "http://localhost:8383";
	//private static final String LOCAL_HOST = "http://localhost:8383, http://localhost:8080";
	/*private static final String GET_POST_PATCH_PUT_DELETE_OPTIONS = "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD";
	private static final String ORIGIN_CONTENT_TYPE_AUTHORIZATION = "X-Authorization, Origin, X-Requested-With, Content-Type, Accept, Authorization,  X-Auth-Token";
	private static final String TRUE = "true";
	private static final String _1209600 = "1209600";
	private static final String OPTIONS = "OPTIONS";*/
	
	public static Response build(Object response) {
		return Response.ok(response)
        		/*.header(ALLOW_ORIGIN, ALL_HOSTS)
				.header(ALLOW_METHODS, GET_POST_PATCH_PUT_DELETE_OPTIONS)
				.header(ALLOW_HEADERS, ORIGIN_CONTENT_TYPE_AUTHORIZATION)
				.header(ALLOW_CREDENTIALS, TRUE)
				.header(MAX_AGE, _1209600)
				.allow(OPTIONS)*/
				.build();
	}
	
	public static Response build(Object response, int responseCode) {
		return Response.status(responseCode).entity(response)
				/*.header(ALLOW_ORIGIN, ALL_HOSTS)
				.header(ALLOW_METHODS, GET_POST_PATCH_PUT_DELETE_OPTIONS)
				.header(ALLOW_HEADERS, ORIGIN_CONTENT_TYPE_AUTHORIZATION)
				.header(ALLOW_CREDENTIALS, TRUE)
				.header(MAX_AGE, _1209600)
				.allow(OPTIONS)*/
				.build();
	}
	
	public static Response buildTextPlain(Object response) {
		return Response.ok(response)
				.type(MediaType.TEXT_PLAIN)
        		/*.header(ALLOW_ORIGIN, ALL_HOSTS)
				.header(ALLOW_METHODS, GET_POST_PATCH_PUT_DELETE_OPTIONS)
				.header(ALLOW_HEADERS, ORIGIN_CONTENT_TYPE_AUTHORIZATION)
				.header(ALLOW_CREDENTIALS, TRUE)
				.header(MAX_AGE, _1209600)
				.allow(OPTIONS)*/
				.build();
	}
	
	public static Response buildTextPlain(Object response, int responseCode) {
		return Response.status(responseCode).entity(response)
				.type(MediaType.TEXT_PLAIN)
				/*.header(ALLOW_ORIGIN, ALL_HOSTS)
				.header(ALLOW_METHODS, GET_POST_PATCH_PUT_DELETE_OPTIONS)
				.header(ALLOW_HEADERS, ORIGIN_CONTENT_TYPE_AUTHORIZATION)
				.header(ALLOW_CREDENTIALS, TRUE)
				.header(MAX_AGE, _1209600)
				.allow(OPTIONS)*/
				.build();
	}

}
