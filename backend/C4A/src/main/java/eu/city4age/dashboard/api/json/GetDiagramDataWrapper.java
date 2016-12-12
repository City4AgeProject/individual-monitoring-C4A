package eu.city4age.dashboard.api.json;

public class GetDiagramDataWrapper {
	
	String timestampStart;
	
	String timestampEnd;
	
	Integer crId;
	
	Short dvParentId;

	public String getTimestampStart() {
		return timestampStart;
	}

	public void setTimestampStart(String timestampStart) {
		this.timestampStart = timestampStart;
	}

	public String getTimestampEnd() {
		return timestampEnd;
	}

	public void setTimestampEnd(String timestampEnd) {
		this.timestampEnd = timestampEnd;
	}

	public Integer getCrId() {
		return crId;
	}

	public void setCrId(Integer crId) {
		this.crId = crId;
	}

	public Short getDvParentId() {
		return dvParentId;
	}

	public void setDvParentId(Short dvParentId) {
		this.dvParentId = dvParentId;
	}

}
