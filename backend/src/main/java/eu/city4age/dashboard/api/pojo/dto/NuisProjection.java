package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public interface NuisProjection {
	
	Long getUserId();
	
	Long getNuiDvId();
	
	BigDecimal getNuiValue();
}
