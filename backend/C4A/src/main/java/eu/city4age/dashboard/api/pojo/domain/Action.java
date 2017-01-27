package eu.city4age.dashboard.api.pojo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="action")
public class Action extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5254461979420298384L;

	@Column(name="action_name")
	private String actionName;

	private String category;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="action",fetch=FetchType.LAZY)
	private Set<Eam> eams = new HashSet<Eam>(0);
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="action",fetch=FetchType.LAZY)
	private Set<ExecutedAction> executedActions = new HashSet<ExecutedAction>(0);

	public Action() {
	}

	public Action(String actionName, String category, Set<Eam> eams, Set<ExecutedAction> executedActions) {
		this.actionName = actionName;
		this.category = category;
		this.eams = eams;
		this.executedActions = executedActions;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
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

}
