package eu.city4age.dashboard.api.json;

import java.util.List;

import eu.city4age.dashboard.api.domain.OrderBy;

public class GetAssessmentsByFilterWrapper {
	
	List<Long> geriatricFactorValueIds;
	
	List<Boolean> status;
	
	Short authorRoleId;

	OrderBy orderBy;

	
	public List<Long> getGeriatricFactorValueIds() {
		return geriatricFactorValueIds;
	}

	public void setGeriatricFactorValueIds(List<Long> geriatricFactorValueIds) {
		this.geriatricFactorValueIds = geriatricFactorValueIds;
	}

	public List<Boolean> getStatus() {
		return status;
	}

	public void setStatus(List<Boolean> status) {
		this.status = status;
	}

	public Short getAuthorRoleId() {
		return authorRoleId;
	}

	public void setAuthorRoleId(Short authorRoleId) {
		this.authorRoleId = authorRoleId;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

}
