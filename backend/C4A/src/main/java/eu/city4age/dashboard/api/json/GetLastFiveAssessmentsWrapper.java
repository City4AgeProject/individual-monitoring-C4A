package eu.city4age.dashboard.api.json;

public class GetLastFiveAssessmentsWrapper {
	
	String timestampStart;
	
	String timestampEnd;
	
	Integer patientId;


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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

}
