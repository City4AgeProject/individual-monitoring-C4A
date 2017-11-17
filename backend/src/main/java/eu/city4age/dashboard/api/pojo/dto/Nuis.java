package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public class Nuis implements NuisProjection {
	
	private Long userId, nuiDvId;
	
	private BigDecimal nuiValue;

	public Nuis(Long userId, Long nuiDvId, BigDecimal nuiValue) {
		this.nuiDvId = nuiDvId;
		this.userId = userId;
		this.nuiValue = nuiValue;
	}

	public BigDecimal getNuiValue() {
		return nuiValue;
	}

	public void setNuiValue(BigDecimal nuiValue) {
		this.nuiValue = nuiValue;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getNuiDvId() {
		return nuiDvId;
	}

	public void setNuiDvId(Long nuiDvId) {
		this.nuiDvId = nuiDvId;
	}
}
