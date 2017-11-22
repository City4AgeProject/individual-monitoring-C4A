package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public class Gfvs implements GfvsProjection {
	
	private Long uirId, ddvId;
	
	private BigDecimal gesValue, weight;
	

	public Gfvs(Long uirId, Long ddvId, BigDecimal gesValue, BigDecimal weight) {
		this.uirId = uirId;
		this.ddvId = ddvId;
		this.gesValue = gesValue;
		this.weight = weight;
	}


	public Long getUirId() {
		return uirId;
	}

	public void setUirId(Long uirId) {
		this.uirId = uirId;
	}

	public Long getDdvId() {
		return ddvId;
	}

	public void setDdvId(Long ddvId) {
		this.ddvId = ddvId;
	}

	public BigDecimal getGesValue() {
		return gesValue;
	}

	public void setGesValue(BigDecimal gesValue) {
		this.gesValue = gesValue;
	}


	public BigDecimal getWeight() {
		return weight;
	}


	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}


}