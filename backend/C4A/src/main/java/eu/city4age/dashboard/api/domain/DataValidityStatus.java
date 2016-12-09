package eu.city4age.dashboard.api.domain;

public enum DataValidityStatus {
	

	QUESTIONABLE_DATA('V'),
	FAULTY_DATA('F');

	private final Character c;

	private DataValidityStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
