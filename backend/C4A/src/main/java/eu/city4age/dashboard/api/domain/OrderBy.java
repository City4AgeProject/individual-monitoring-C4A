package eu.city4age.dashboard.api.domain;

public enum OrderBy {
	

	DATE_ASC('1'),
	DATE_DESC('2'),
	AUTHOR_NAME_ASC('3'),
	AUTHOR_NAME_DESC('4'),
	AUTHOR_ROLE_ASC('5'),
	AUTHOR_ROLE_DESC('6');

	private final Character c;

	private OrderBy(Character ch) {
		c = ch;
	}

	public Character toChar() {
		return c;
	}

}
