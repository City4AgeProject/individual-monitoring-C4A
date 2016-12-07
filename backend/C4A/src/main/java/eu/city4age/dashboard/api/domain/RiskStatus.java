package eu.city4age.dashboard.api.domain;

public enum RiskStatus {
	

	RISK_WARNING('1'),
	RISK_ALERT('2');

	private final Character c;

	private RiskStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
