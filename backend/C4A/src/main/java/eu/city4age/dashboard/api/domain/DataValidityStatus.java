package eu.city4age.dashboard.api.domain;

public enum DataValidityStatus {
	

	QUESTIONABLE_DATA('1'),
	FAULTY_DATA('2');

	private final Character c;

	private DataValidityStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
