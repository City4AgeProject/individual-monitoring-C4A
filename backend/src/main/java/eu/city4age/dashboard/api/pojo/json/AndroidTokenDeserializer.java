package eu.city4age.dashboard.api.pojo.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AndroidTokenDeserializer {
	
	private Long userId;
	
	private String token;

	/**
	 * 
	 */
	public AndroidTokenDeserializer() {
	}

	/**
	 * @param userId
	 * @param token
	 */
	public AndroidTokenDeserializer(@JsonProperty(value = "id", required = true) Long userId, 
									@JsonProperty(value = "token", required = true) String token) {
		this.userId = userId;
		this.token = token;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
