package eu.city4age.dashboard.api.pojo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericTableData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6471701922179579204L;

	private List<String> headers = new ArrayList<String>();
	
	private List<List<String>> data = new ArrayList<List<String>>();


	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}

}
