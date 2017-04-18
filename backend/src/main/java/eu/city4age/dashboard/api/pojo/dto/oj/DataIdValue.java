package eu.city4age.dashboard.api.pojo.dto.oj;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

public class DataIdValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168277437873517671L;

	@JsonView(View.TimeIntervalView.class)
	private Long id;

	@JsonView(View.TimeIntervalView.class)
	private String name;

	public DataIdValue() {
		
	}

	public DataIdValue(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataIdValue other = (DataIdValue) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
