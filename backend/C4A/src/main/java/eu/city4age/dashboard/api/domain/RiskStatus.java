package eu.city4age.dashboard.api.domain;

public enum RiskStatus {
	

	W('W'),
	A('A');

	private final Character c;

	private RiskStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
