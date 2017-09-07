package eu.city4age.dashboard.api.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class C4AAndroidResponse {

	private Long id;
	private int result;
	private String message;
	
	public Long getId() {
		return id;
	}
	@JsonProperty("ID")
	public void setId(Long id) {
		this.id = id;
	}

	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
