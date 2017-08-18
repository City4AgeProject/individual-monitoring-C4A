package eu.city4age.dashboard.api.pojo.dto;

public class NuisForMeasure {
	
	private Long avg, std, best, delta;

	public Long getAvg() {
		return avg;
	}

	public void setAvg(Long avg) {
		this.avg = avg;
	}

	public Long getStd() {
		return std;
	}

	public void setStd(Long std) {
		this.std = std;
	}

	public Long getBest() {
		return best;
	}

	public void setBest(Long best) {
		this.best = best;
	}

	public Long getDelta() {
		return delta;
	}

	public void setDelta(Long delta) {
		this.delta = delta;
	}

	public Long nuiValue(String nuiName) {
		if(nuiName.equals("avg")) {
			return avg;
		} else if(nuiName.equals("std")) {
			return std;
		} else if(nuiName.equals("best")) {
			return best;
		} else if(nuiName.equals("delta")) {
			return delta;
		}
		return null;
	}
}
