package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.city4age.dashboard.api.pojo.domain.Pilot;
/**
 * @author Andrija Petrovic
 *
 */
public class Configuration {

	private String name;
	
	private Integer level;
	
	private Pilot.PilotCode pilotCode;
	
	private String username;
	
	private String password;
	
	private String personalProfileDataUrl;
	
	private List<Groups> groups;
	

	public Configuration(	@JsonProperty(value= "name", required = true) String name,
							@JsonProperty(value= "level", required = true) Integer level,
            				@JsonProperty(value= "pilotCode", required = true) Pilot.PilotCode pilotCode,
            				@JsonProperty(value= "username", required = true) String username,
            				@JsonProperty(value= "password", required = true) String password,
            				@JsonProperty(value= "personalProfileDataUrl", required = false) String personalProfileDataUrl,
            				@JsonProperty(value= "groups", required = true) List<Groups> groups) {
		this.name = name;
		if (level != null) this.level = level;
		else this.level = 0;
		this.pilotCode = pilotCode;
		this.username = username;
		this.password = password;
		this.personalProfileDataUrl = personalProfileDataUrl;
		this.groups = groups;
	}
	
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("pilotCode")
	public Pilot.PilotCode getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(Pilot.PilotCode pilotCode) {
		this.pilotCode = pilotCode;
	}

	@JsonProperty("level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@JsonProperty("groups")
	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	/**
	 * @return the personalProfileDataUrl
	 */
	public String getPersonalProfileDataUrl() {
		return personalProfileDataUrl;
	}

	/**
	 * @param personalProfileDataUrl the personalProfileDataUrl to set
	 */
	public void setPersonalProfileDataUrl(String personalProfileDataUrl) {
		this.personalProfileDataUrl = personalProfileDataUrl;
	}



}
