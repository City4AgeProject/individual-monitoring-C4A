package eu.city4age.dashboard.api.dao;

import java.util.List;

import eu.city4age.dashboard.api.model.CareProfile;
import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.CrProfile;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.UserInRole;
import eu.city4age.dashboard.api.model.UserInSystem;

public interface ExternalDAO extends BaseDAO {

	
	/*
	 * 
	 * getGroups dao methods
	 *
	 */
	List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(List<String> parentFactors);
	
	List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(String parentFactor);

	List<GeriatricFactorValue> getGeriatricFactorValueForDetectionVariableId(Long dvId, Long cdId);

	String getUserInSystemUsername(Long gefId); //++++

	List<FrailtyStatusTimeline> getFrailtyStatus(List<Long> timeintervalIds, Long uId);
	
	
	/*
	 * 
	 * getCareReceivers dao methods
	 *
	 */
	List<UserInRole> getUserInRoleByRoleId(Short roleId);
	
	String getUserInSystemUsernameByUserInRoleId(Long uId); //++++

	List<CrProfile> getProfileByUserInRoleId(Long id);

	List<CareProfile> getCareProfileByUserInRoleId(Long id);

	List<FrailtyStatusTimeline> getFrailtyStatusByUserInRoleId(Long id);

	
	/*
	 * 
	 * login dao methods
	 * 
	 */
	UserInSystem getUserInSystem(String username, String password);

	UserInRole getUserInRoleByUserInSystemId(Long uisId);
	

}
