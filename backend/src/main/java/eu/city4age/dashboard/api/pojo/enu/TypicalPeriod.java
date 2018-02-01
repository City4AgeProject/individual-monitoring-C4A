package eu.city4age.dashboard.api.pojo.enu;

public enum TypicalPeriod {
	DAY("day"), MONTH("mon");
	
	private final String dbName;
	
	TypicalPeriod(String dbName) {
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}

}
