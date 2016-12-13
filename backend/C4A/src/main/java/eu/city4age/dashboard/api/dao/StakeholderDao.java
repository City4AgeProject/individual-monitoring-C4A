package eu.city4age.dashboard.api.dao;

import java.util.List;

import eu.city4age.dashboard.api.model.CdRole;
import eu.city4age.dashboard.api.model.Stakeholder;

public interface StakeholderDao extends BaseDAO {
	
	public List<Stakeholder> getAllStockholders();
	
	public List<CdRole> getAllRolesForStakeholderAbbr(String stakeholderAbbr);

}
