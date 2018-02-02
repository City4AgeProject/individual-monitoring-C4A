package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Superclass for domain model.
 *
 * @author Milos Holclajtner (milos.holclajtner at belit.co.rs)
 */
@MappedSuperclass
@ApiModel
public abstract class AbstractBaseEntity<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView(View.BaseView.class)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
	@Column(name = "id", columnDefinition = "serial", insertable = true, updatable = true, unique = true)
	@ApiModelProperty (hidden = true)
	protected T id;

	@ApiModelProperty (hidden = true)
	public T getId() {
		return id;
	}

	@ApiModelProperty (hidden = true)
	public void setId(T id) {
		this.id = id;
	}

	@JsonIgnore
	public boolean isWithoutId() {
		return (this.id == null || this.id.equals(0L));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof AbstractBaseEntity)) return false;
    	return this.getId().equals(((AbstractBaseEntity)obj).getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
