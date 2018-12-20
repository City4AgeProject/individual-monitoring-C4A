package eu.city4age.dashboard.api.pojo.dto;

import java.util.List;

public class JsonToExcel {
	
	private List<String> headers;
	
	private List<String[]> data;

	/**
	 * 
	 */
	public JsonToExcel() {
	}

	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the data
	 */
	public List<String[]> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<String[]> data) {
		this.data = data;
	}

}
