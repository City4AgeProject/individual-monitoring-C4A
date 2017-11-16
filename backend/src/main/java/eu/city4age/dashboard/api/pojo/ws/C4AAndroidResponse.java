package eu.city4age.dashboard.api.pojo.ws;

import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;
import eu.city4age.dashboard.api.pojo.dto.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel (value = "C4AAndroidResponseModel")
public class C4AAndroidResponse {

	@ApiModelProperty(value = "represents result of operation: 0 - unsuccessful, 1 - successful", hidden = false, allowableValues = "range[0, 1]", example = "1")
	private Long result;
	@ApiModelProperty(value = "list of android app readings", hidden = false)
	private List<MTestingReadings> mtss;
	@ApiModelProperty(value = "http status and response of recordings", hidden = true)
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
