package eu.city4age.dashboard.api.domain;

public enum RiskStatus {
	

	RISK_WARNING('W'),
	RISK_ALERT('A');


	private final Character c;

	private RiskStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
