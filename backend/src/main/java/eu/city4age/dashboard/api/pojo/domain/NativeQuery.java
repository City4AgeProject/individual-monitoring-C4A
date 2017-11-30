package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NativeQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474915851303374711L;
	
	@Id
	private Long id;
	
	public NativeQuery() {
		
	}

}
