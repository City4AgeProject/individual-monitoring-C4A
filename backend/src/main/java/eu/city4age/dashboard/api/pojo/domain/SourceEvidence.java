package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="source_evidence")
public class SourceEvidence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5255970811658923638L;

	@EmbeddedId
	private SourceEvidenceId sourceEvidenceId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="value_id", referencedColumnName = "id", insertable=false, updatable=false)
	private GeriatricFactorValue geriatricFactorValue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="value_id", referencedColumnName = "id", insertable=false, updatable=false)
	private VariationMeasureValue variationMeasureValue;
	
	@ManyToOne
	@JoinColumn(name="author_id")	
	private UserInRole userInRole;
	
	@Column(name="text_evidence")
	private String textEvidence;
	
	@Column(name="multimedia_evidence")
	private byte[] multimediaEvidence;

	private Date uploaded;
	
	public SourceEvidence() {
	}

	public SourceEvidence(GeriatricFactorValue geriatricFactorValue, Date uploaded) {
		this.geriatricFactorValue = geriatricFactorValue;
		this.uploaded = uploaded;
	}

	public SourceEvidence(GeriatricFactorValue geriatricFactorValue, UserInRole userInRole,
			String textEvidence, byte[] multimediaEvidence, Date uploaded) {
		this.geriatricFactorValue = geriatricFactorValue;
		this.userInRole = userInRole;
		this.textEvidence = textEvidence;
		this.multimediaEvidence = multimediaEvidence;
		this.uploaded = uploaded;
	}

	public SourceEvidenceId getSourceEvidenceId() {
		return sourceEvidenceId;
	}

	public void setSourceEvidenceId(SourceEvidenceId sourceEvidenceId) {
		this.sourceEvidenceId = sourceEvidenceId;
	}

	public GeriatricFactorValue getGeriatricFactorValue() {
		return this.geriatricFactorValue;
	}

	public void setGeriatricFactorValue(GeriatricFactorValue geriatricFactorValue) {
		this.geriatricFactorValue = geriatricFactorValue;
	}

	public VariationMeasureValue getVariationMeasureValue() {
		return variationMeasureValue;
	}

	public void setVariationMeasureValue(VariationMeasureValue variationMeasureValue) {
		this.variationMeasureValue = variationMeasureValue;
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
