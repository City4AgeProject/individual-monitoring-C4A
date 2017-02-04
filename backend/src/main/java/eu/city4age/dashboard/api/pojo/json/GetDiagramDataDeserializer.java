package eu.city4age.dashboard.api.pojo.json;

public class GetDiagramDataDeserializer {

	String timestampStart;

	String timestampEnd;

	Integer crId;

	Long dvParentId;

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

	public Long getDvParentId() {
		return dvParentId;
	}

	public void setDvParentId(Long dvParentId) {
		this.dvParentId = dvParentId;
	}

}
