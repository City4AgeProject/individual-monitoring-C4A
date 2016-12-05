package eu.city4age.dashboard.api.model;

import java.io.Serializable;

/**
 * Glavna klasa za nasledjivanje domenskih objekta.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public abstract class AbstractBaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isWithoutId() {
            return (this.id == null || this.id.equals(0L));
    }

}
