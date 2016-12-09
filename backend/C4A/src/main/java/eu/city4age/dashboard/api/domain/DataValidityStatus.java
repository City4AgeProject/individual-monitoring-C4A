package eu.city4age.dashboard.api.domain;

public enum DataValidityStatus {
	


	QUESTIONABLE_DATA('Q'),
	FAULTY_DATA('F'),
    VALID_DATA('V');

	private final Character c;

	private DataValidityStatus(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
