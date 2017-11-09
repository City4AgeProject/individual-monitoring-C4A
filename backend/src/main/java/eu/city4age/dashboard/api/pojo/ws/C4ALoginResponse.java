package eu.city4age.dashboard.api.pojo.ws;

/**
 *
 * @author EMantziou
 */
public class C4ALoginResponse {
	
	private Long uirId;

    private int responseCode;

    private String jwToken;

    private String displayName;

    private String pilotName;
    
    
 
    public Long getUirId() {
		return uirId;
	}

	public void setUirId(Long uirId) {
		this.uirId = uirId;
	}

	public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getJwToken() {
        return jwToken;
    }

    public void setJwToken(String jwToken) {
        this.jwToken = jwToken;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }
}
