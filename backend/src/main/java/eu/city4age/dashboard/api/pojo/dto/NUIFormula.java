package eu.city4age.dashboard.api.pojo.dto;

public class NUIFormula {
	
	Double avg;
	
	Double stDev;
	
	Double best25Perc;
	
	Double delta25PercAvg;


	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	public Double getStDev() {
		return stDev;
	}

	public void setStDev(Double stDev) {
		this.stDev = stDev;
	}

	public Double getBest25Perc() {
		return best25Perc;
	}

	public void setBest25Perc(Double best25Perc) {
		this.best25Perc = best25Perc;
	}

	public Double getDelta25PercAvg() {
		return delta25PercAvg;
	}

	public void setDelta25PercAvg(Double delta25PercAvg) {
		this.delta25PercAvg = delta25PercAvg;
	}

}
