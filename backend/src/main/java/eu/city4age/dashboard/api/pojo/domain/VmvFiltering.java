package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "vmv_filtering")
@SequenceGenerator(name = "default_gen", sequenceName = "vmv_filtering_id_seq", allocationSize = 1)
public class VmvFiltering extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6676248651903229308L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vmv_id", referencedColumnName = "id")
	private VariationMeasureValue vmv;
	
	@Column(name = "filter_type")
	private String filterType;
	
	@Column(name = "trans_value")
	private BigDecimal transValue;
	
	@Column(name = "valid_from")
	private Date validFrom;
	
	@Column(name = "valid_to")
	private Date validTo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assessment_id", referencedColumnName = "id")
	private Assessment assessment;
	
	
	public VmvFiltering() {
		
	}

	public VmvFiltering(VariationMeasureValue vmv, String filterType, Date validFrom, Assessment assessment) {
		this.vmv = vmv;
		this.filterType = filterType;
		this.validFrom = validFrom;
		this.assessment = assessment;
	}

	public VariationMeasureValue getVmv() {
		return vmv;
	}

	public void setVmv(VariationMeasureValue vmv) {
		this.vmv = vmv;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public BigDecimal getTransValue() {
		return transValue;
	}

	public void setTransValue(BigDecimal transValue) {
		this.transValue = transValue;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	/**
	 * @return the assessment
	 */
	public Assessment getAssessment() {
		return assessment;
	}

	/**
	 * @param assessment the assessment to set
	 */
	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}
	
	public int hashCode() {
		return id.intValue();
	}

	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof VmvFiltering) {
			VmvFiltering vmvf = (VmvFiltering) obj;
			if (vmvf.getId().equals(this.id)) return true;
			else return false;
		}
		return false;
	}

}
