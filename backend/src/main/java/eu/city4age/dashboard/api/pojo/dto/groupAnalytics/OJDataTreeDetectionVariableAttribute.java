package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType.Type;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;

public class OJDataTreeDetectionVariableAttribute {
	
	private Long id;
	
	private String title;
	
	private List<Pilot.PilotCode> pilots;
	
	private Boolean allPilots;
	
	private DetectionVariableType.Type type;
	
	private Long detectionVariableId;

	/**
	 * 
	 */
	public OJDataTreeDetectionVariableAttribute() {
	}

	/**
	 * @param id
	 * @param title
	 * @param pilots
	 * @param allPilots
	 * @param type
	 * @param detectionVariableId
	 */
	public OJDataTreeDetectionVariableAttribute(Long id, String title, List<PilotCode> pilots, Boolean allPilots,
			Type type, Long detectionVariableId) {
		this.id = id;
		this.title = title;
		this.pilots = pilots;
		this.allPilots = allPilots;
		this.type = type;
		this.detectionVariableId = detectionVariableId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the pilots
	 */
	public List<Pilot.PilotCode> getPilots() {
		return pilots;
	}


	/**
	 * @param pilots the pilots to set
	 */
	public void setPilots(List<Pilot.PilotCode> pilots) {
		this.pilots = pilots;
	}


	/**
	 * @return the allPilots
	 */
	public Boolean getAllPilots() {
		return allPilots;
	}


	/**
	 * @param allPilots the allPilots to set
	 */
	public void setAllPilots(Boolean allPilots) {
		this.allPilots = allPilots;
	}

	/**
	 * @return the type
	 */
	public DetectionVariableType.Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DetectionVariableType.Type type) {
		this.type = type;
	}

	/**
	 * @return the detectionVariableId
	 */
	public Long getDetectionVariableId() {
		return detectionVariableId;
	}

	/**
	 * @param detectionVariableId the detectionVariableId to set
	 */
	public void setDetectionVariableId(Long detectionVariableId) {
		this.detectionVariableId = detectionVariableId;
	}

}
