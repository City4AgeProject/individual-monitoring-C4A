package eu.city4age.dashboard.api.pojo.dto;

import java.math.BigDecimal;

public class Nuis implements NuisProjection {
	
	private BigDecimal avg, stDev, std, best25, best, delta;

	public Nuis(BigDecimal avg, BigDecimal stDev, BigDecimal std, BigDecimal best25, BigDecimal best, BigDecimal delta) {
		this.avg = avg;
		this.stDev = stDev;
		this.std = std;
		this.best25 = best25;
		this.best = best;
		this.delta = delta;
	}

	public BigDecimal getAvg() {
		return avg;
	}
	
	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getStDev() {
		return stDev;
	}

	public void setStDev(BigDecimal stDev) {
		this.stDev = stDev;
	}
	
	public BigDecimal getStd() {
		return std;
	}

	public void setStd(BigDecimal std) {
		this.std = std;
	}

	public BigDecimal getBest25() {
		return best25;
	}

	public void setBest25(BigDecimal best25) {
		this.best25 = best25;
	}

	public BigDecimal getBest() {
		return best;
	}

	public void setBest(BigDecimal best) {
		this.best = best;
	}

	public BigDecimal getDelta() {
		return delta;
	}

	public void setDelta(BigDecimal delta) {
		this.delta = delta;
	}

}
