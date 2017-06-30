package eu.city4age.dashboard.api.pojo.enu;

public enum DailyMeasure {

	AVG("AVG"), STD("STD"), BEST("BEST"), DELTA("DELTA"), ALL("SELECT");

	private final String formula;
	
	DailyMeasure(String formula) {
		this.formula = formula;
	}

	public String getFormula() {
		return formula;
	}

}
