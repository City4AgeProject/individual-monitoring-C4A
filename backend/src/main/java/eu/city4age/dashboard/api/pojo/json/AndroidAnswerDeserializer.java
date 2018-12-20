package eu.city4age.dashboard.api.pojo.json;

public class AndroidAnswerDeserializer {
	
	private Long id;
	
	private String answer;

	/**
	 * 
	 */
	public AndroidAnswerDeserializer() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
