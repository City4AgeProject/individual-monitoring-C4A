package eu.city4age.dashboard.api.pojo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table (name = "value_evidence_notice")
@SequenceGenerator(name = "default_gen", sequenceName = "value_evidence_notice_id_seq", allocationSize = 1)
public class ValueEvidenceNotice extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308810001192772690L;
		
	@JoinColumn(name = "value_id", referencedColumnName = "id", insertable = true, updatable = false, nullable = false)
	@OneToOne (fetch = FetchType.LAZY)
	private VariationMeasureValue value;
	
	@Column (name = "notice", insertable = true, updatable = true)
	@JsonView(View.VariationMeasureValueView.class)
	private String notice;
	
	@JoinColumn (name = "author_id", referencedColumnName = "id", insertable = true, updatable = false)
	@ManyToOne (fetch = FetchType.LAZY)
	private UserInRole userInRole;
	
	@JoinColumn (name = "source_evidence_id", referencedColumnName = "id", insertable = true, updatable = true)
	@ManyToOne (fetch = FetchType.LAZY)
	private SourceEvidence sourceEvidence;	

	public ValueEvidenceNotice() {
		
	}

	public ValueEvidenceNotice(VariationMeasureValue value, String notice, UserInRole userInRole,
			SourceEvidence sourceEvidence) {
		super();
		this.value = value;
		this.notice = notice;
		this.userInRole = userInRole;
		this.sourceEvidence = sourceEvidence;
	}	

	public VariationMeasureValue getValue() {
		return value;
	}

	public void setValue(VariationMeasureValue value) {
		this.value = value;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public UserInRole getUserInRole() {
		return userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public SourceEvidence getSourceEvidence() {
		return sourceEvidence;
	}

	public void setSourceEvidence(SourceEvidence sourceEvidence) {
		this.sourceEvidence = sourceEvidence;
	}	

}
