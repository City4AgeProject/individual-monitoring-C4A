package eu.city4age.dashboard.api.pojo.other;

public class ConfigurationCounter {
	
	private int inserted;
	private int updated;
	
	public int getUpdated() {
		return updated;
	}
	public void incrementUpdated() {
		this.updated++;
	}
	public int getInserted() {
		return inserted;
	}
	public void incrementInserted() {
		this.inserted++;
	}

}
