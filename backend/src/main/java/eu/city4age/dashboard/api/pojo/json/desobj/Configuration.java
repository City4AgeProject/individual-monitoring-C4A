package eu.city4age.dashboard.api.pojo.json.desobj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author Andrija Petrovic
 *
 */
public class Configuration {

	private String name;
	private Integer level;
	private String validFrom;
	private String validTo;
	private String pilotCode;
	private String username;
	private String password;
	private List<Groups> groups;
	

	public Configuration(	@JsonProperty(value= "name", required = true)String name,
							@JsonProperty(value= "level",required = true)Integer level,
							@JsonProperty(value= "validFrom",required = true)String validFrom,
            				@JsonProperty(value= "validTo",required = true)String validTo,
            				@JsonProperty(value= "pilotCode",required = true)String pilotCode,
            				@JsonProperty(value= "username",required = true)String username,
            				@JsonProperty(value= "password",required = true)String password,
            				@JsonProperty(value= "groups",required = true)List<Groups> groups) {
		this.name = name;
		this.level = level;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.pilotCode = pilotCode;
		this.username = username;
		this.password = password;
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
	public String getPilotCode() {
		return pilotCode;
	}

	public void setPilotCode(String pilotCode) {
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

	@JsonProperty("validFrom")
	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	@JsonProperty("validTo")
	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}



}
