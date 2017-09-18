package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cd_detection_variable_type")
public class DetectionVariableType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2367967721511008325L;

	public static final DetectionVariableType MEA = new DetectionVariableType(DetectionVariableType.Type.MEA, "Variation measure");
	public static final DetectionVariableType NUI = new DetectionVariableType(DetectionVariableType.Type.NUI, "Numeric indicator");
	public static final DetectionVariableType GES = new DetectionVariableType(DetectionVariableType.Type.GES, "Geriatric sub-factor");
	public static final DetectionVariableType GEF = new DetectionVariableType(DetectionVariableType.Type.GEF, "Geriatric factor");
	public static final DetectionVariableType GFG = new DetectionVariableType(DetectionVariableType.Type.GFG, "Geriatric factor group");
	public static final DetectionVariableType OVL = new DetectionVariableType(DetectionVariableType.Type.OVL, "Overall frailty score");
	
	public enum Type {
		MEA("MEA"), NUI("NUI"), GES("GES"), GEF("GEF"), GFG("GFG"), OVL("OVL");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};

	@Id
	@Column(name = "detection_variable_type")
	@Enumerated(EnumType.STRING)
	private DetectionVariableType.Type detectionVariableType;

	@Column(name = "detection_variable_type_description")
	private String detectionVariableTypeDescription;
	
	public DetectionVariableType() {
	}

	private DetectionVariableType(DetectionVariableType.Type detectionVariableType, String detectionVariableTypeDescription) {
		this.detectionVariableType = detectionVariableType;
		this.detectionVariableTypeDescription = detectionVariableTypeDescription;
	}

}
