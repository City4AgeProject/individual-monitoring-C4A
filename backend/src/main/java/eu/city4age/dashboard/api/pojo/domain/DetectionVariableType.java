package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cd_detection_variable_type")
public class DetectionVariableType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2367967721511008325L;

	public static final DetectionVariableType MEA = new DetectionVariableType(DetectionVariableType.Type.MEA, "Each factor has it own measures inside it. The measures are different in terms of values and represent the data acquisition registered to the user.");
	public static final DetectionVariableType NUI = new DetectionVariableType(DetectionVariableType.Type.NUI, "Numeric indicator");
	public static final DetectionVariableType GES = new DetectionVariableType(DetectionVariableType.Type.GES, "The Geriatric Sub-Factor values contains the description of what values are performed inside a global Geriatic Factor. An example of a geriatric subfactor value inside 'mobility' geriatric factor, could be 'phone_usage' or 'walking'.");
	public static final DetectionVariableType GEF = new DetectionVariableType(DetectionVariableType.Type.GEF, "The Geriatric Factor values contains the descriptions of primary actions. An example could be 'mobility' which represent all actions related with the movement of the measured user.");
	public static final DetectionVariableType GFG = new DetectionVariableType(DetectionVariableType.Type.GFG, "Geriatric factor group");
	public static final DetectionVariableType OVL = new DetectionVariableType(DetectionVariableType.Type.OVL, "Overall frailty score");
	
	public enum Type {
		MEA("mea"), NUI("nui"), GES("ges"), GEF("gef"), GFG("gfg"), OVL("ovl");
		
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
	@org.hibernate.annotations.Type(type = "DVTEnumUserType")

	private DetectionVariableType.Type detectionVariableType;

	@Column(name = "detection_variable_type_description", length = 255)
	private String detectionVariableTypeDescription;
	
	public DetectionVariableType() {
	}

	public DetectionVariableType(DetectionVariableType.Type detectionVariableType, String detectionVariableTypeDescription) {
		this.detectionVariableType = detectionVariableType;
		this.detectionVariableTypeDescription = detectionVariableTypeDescription;
	}
	
	public String toString() {
		return detectionVariableType.getName();
	}

	public DetectionVariableType.Type getDetectionVariableType() {
		return detectionVariableType;
	}

	public void setDetectionVariableType(DetectionVariableType.Type detectionVariableType) {
		this.detectionVariableType = detectionVariableType;
	}

}
