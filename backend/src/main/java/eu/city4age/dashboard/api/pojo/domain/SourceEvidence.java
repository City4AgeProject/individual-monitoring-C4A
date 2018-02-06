package eu.city4age.dashboard.api.pojo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="source_evidence")
@SequenceGenerator(name = "default_gen", sequenceName = "source_evidence_seq", allocationSize = 1)
public class SourceEvidence extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5255970811658923638L;
	
	@JoinColumn(name="author_id", referencedColumnName = "id")
	@ManyToOne (fetch = FetchType.LAZY)
	private UserInRole userInRole;
	
	@Column(name="text_evidence")
	private String textEvidence;
	
	@Column(name="multimedia_evidence")
	private byte[] multimediaEvidence;

	@Column (name = "uploaded")
	private Date uploaded;
	
	public SourceEvidence() {
		
	}
	
	public SourceEvidence(UserInRole userInRole,
			String textEvidence, byte[] multimediaEvidence, Date uploaded) {
		
		this.userInRole = userInRole;
		this.textEvidence = textEvidence;
		this.multimediaEvidence = multimediaEvidence;
		this.uploaded = uploaded;
	}
	
	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public String getTextEvidence() {
		return this.textEvidence;
	}

	public void setTextEvidence(String textEvidence) {
		this.textEvidence = textEvidence;
	}

	public byte[] getMultimediaEvidence() {
		return this.multimediaEvidence;
	}

	public void setMultimediaEvidence(byte[] multimediaEvidence) {
		this.multimediaEvidence = multimediaEvidence;
	}

	public Date getUploaded() {
		return this.uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

}
