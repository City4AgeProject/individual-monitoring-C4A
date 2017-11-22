package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public interface GfvsProjection {
	
	Long getUirId();
	
	Long getDdvId();
	
	BigDecimal getGesValue();
	
	BigDecimal getWeight();

}
