package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public interface NuisProjection {
	
	BigDecimal getAvg();
	
	BigDecimal getStd();
	
	BigDecimal getBest();
	
	BigDecimal getDelta();

}
