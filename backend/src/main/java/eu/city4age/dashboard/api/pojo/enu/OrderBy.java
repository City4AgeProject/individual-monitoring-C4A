package eu.city4age.dashboard.api.pojo.enu;

public enum OrderBy {

	DATE_ASC("DATE_ASC"), DATE_DESC("DATE_DESC"), AUTHOR_NAME_ASC("AUTHOR_NAME_ASC"), AUTHOR_NAME_DESC(
			"AUTHOR_NAME_DESC"), AUTHOR_ROLE_ASC("AUTHOR_ROLE_ASC"), AUTHOR_ROLE_DESC("AUTHOR_ROLE_DESC");

	private final String name;

	private OrderBy(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
