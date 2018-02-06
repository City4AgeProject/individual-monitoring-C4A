package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="cd_action")
@SequenceGenerator(name = "default_gen", sequenceName = "cd_action_id_seq", allocationSize = 1)
public class Action extends AbstractBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5254461979420298384L;

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column (name = "action_name", length = 50)
	private String name;

	@Column (name = "action_category", length = 25)
	private String category;
	
	@Column (name = "action_description", length = 250)
	private String description;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="action",fetch=FetchType.LAZY)
	private Set<Eam> eams = new HashSet<Eam>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="action",fetch=FetchType.LAZY)
	private Set<ExecutedAction> executedActions = new HashSet<ExecutedAction>(0);

	public Action() {}

	public Action(String name, String category, String description) {
		this.name = name;
		this.category = category;
		this.description = description;
	}

	public Action(String name, String category, Set<Eam> eams, Set<ExecutedAction> executedActions) {
		this.name = name;
		this.category = category;
		this.eams = eams;
		this.executedActions = executedActions;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<Eam> getEams() {
		return this.eams;
	}

	public void setEams(Set<Eam> eams) {
		this.eams = eams;
	}

	public Set<ExecutedAction> getExecutedActions() {
		return this.executedActions;
	}

	public void setExecutedActions(Set<ExecutedAction> executedActions) {
		this.executedActions = executedActions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
