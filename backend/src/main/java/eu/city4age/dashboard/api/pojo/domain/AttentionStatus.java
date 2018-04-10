package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cd_attention_status")
public class AttentionStatus implements Serializable {

	/**
	 * Author: Vladimir Aleksic
	 */
	private static final long serialVersionUID = 1057210618025594058L;
	
//	public static final AttentionStatus A = new AttentionStatus(AttentionStatus.Status.A, "Attention needed automatically assigned by the system user.");
//	public static final AttentionStatus M = new AttentionStatus(AttentionStatus.Status.M, "Attention needed manually assigned by human user.");
//	public static final AttentionStatus S = new AttentionStatus(AttentionStatus.Status.S, "Suspended by human user.");
	
	public enum Status {
		A('A'), M('M'), S('S');
		
		private final Character name;
		
		Status(Character name) {
			this.name = name;
		}

		public Character getName() {
			return name;
		}
	};
	
	@Id
	@Column(name = "attention_status")
//	@org.hibernate.annotations.Type(type = "AttentionEnumUserType")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "attention_status_description", length = 255)
	private String attentionStatusDescription;
	
	public AttentionStatus() {
	}

	public AttentionStatus(AttentionStatus.Status attentionStatus, String attentionStatusDescription) {
		this.status = attentionStatus;
		this.attentionStatusDescription = attentionStatusDescription;
	}
	
	public String toString() {
		return status.toString();
	}

	public AttentionStatus.Status getStatus() {
		return status;
	}

	public void setStatus(AttentionStatus.Status attentionStatus) {
		this.status = attentionStatus;
	}


}
