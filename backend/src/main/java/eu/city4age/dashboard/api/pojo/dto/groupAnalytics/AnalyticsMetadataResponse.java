package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.Pilot;

public class AnalyticsMetadataResponse {
	
	private List<OJDataTreeDetectionVariableSingleElem> detectionVariables;
	
	private List<Pilot> pilots;
	
	private List<String> socioEconomics;

	/**
	 * 
	 */
	public AnalyticsMetadataResponse() {
	}

	/**
	 * @return the detectionVariables
	 */
	public List<OJDataTreeDetectionVariableSingleElem> getDetectionVariables() {
		return detectionVariables;
	}

	/**
	 * @param detectionVariables the detectionVariables to set
	 */
	public void setDetectionVariables(List<OJDataTreeDetectionVariableSingleElem> detectionVariables) {
		this.detectionVariables = detectionVariables;
	}

	/**
	 * @return the pilots
	 */
	public List<Pilot> getPilots() {
		return pilots;
	}

	/**
	 * @param pilots the pilots to set
	 */
	public void setPilots(List<Pilot> pilots) {
		this.pilots = pilots;
	}

	/**
	 * @return the socioEconomics
	 */
	public List<String> getSocioEconomics() {
		return socioEconomics;
	}

	/**
	 * @param socioEconomics the socioEconomics to set
	 */
	public void setSocioEconomics(List<String> socioEconomics) {
		this.socioEconomics = socioEconomics;
	}

}
