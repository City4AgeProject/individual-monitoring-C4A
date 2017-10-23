package eu.city4age.dashboard.api.pojo.dto;

import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;


public class C4AAndroidResponse {

	private Long result;
	private List<MTestingReadings> mtss;
	private Status status = new Status();
	
	
	
	public Long getResult() {
		return result;
	}
	public void setResult(Long result) {
		this.result = result;
	}
	public List<MTestingReadings> getMtss() {
		return mtss;
	}
	public void setMtss(List<MTestingReadings> mtss) {
		this.mtss = mtss;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

}
