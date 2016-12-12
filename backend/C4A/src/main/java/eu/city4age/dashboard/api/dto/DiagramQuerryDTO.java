package eu.city4age.dashboard.api.dto;

import java.sql.Timestamp;

import eu.city4age.dashboard.api.model.GeriatricFactorValue;

public class DiagramQuerryDTO {
	
	GeriatricFactorValue gef;
	
	Timestamp intervalStart;
	
	Long cdvId;
	
	Long cdvParentId;


	public GeriatricFactorValue getGef() {
		return gef;
	}

	public void setGef(GeriatricFactorValue gef) {
		this.gef = gef;
	}

	public Timestamp getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Timestamp intervalStart) {
		this.intervalStart = intervalStart;
	}

	public Long getCdvId() {
		return cdvId;
	}

	public void setCdvId(Long cdvId) {
		this.cdvId = cdvId;
	}

	public Long getCdvParentId() {
		return cdvParentId;
	}

	public void setCdvParentId(Long cdvParentId) {
		this.cdvParentId = cdvParentId;
	}

}
