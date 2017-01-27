package eu.city4age.dashboard.api.pojo.enu;

public enum DataValidity {

	QUESTIONABLE_DATA('Q'), FAULTY_DATA('F'), VALID_DATA('V');

	private final Character c;

	private DataValidity(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
