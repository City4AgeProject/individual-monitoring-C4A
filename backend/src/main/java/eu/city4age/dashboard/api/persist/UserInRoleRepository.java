package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;

@Repository(value = "userInRoleRepository")
@Transactional(readOnly = true)
public interface UserInRoleRepository extends GenericRepository<UserInRole, Long> {

	@Query("SELECT u FROM UserInRole u LEFT JOIN u.crProfile AS crProfile LEFT JOIN u.careProfile AS careProfile LEFT JOIN u.frailtyStatusTimeline AS fst LEFT JOIN fst.cdFrailtyStatus AS cdFrailtyStatus WHERE u.roleId = :roleId AND (fst IS NULL OR fst.changed = (SELECT max(fst.changed) FROM FrailtyStatusTimeline fst WHERE fst.userInRoleId = u.id))")
	List<UserInRole> findByRoleId(@Param("roleId") final Short roleId);
	
	@Query("SELECT u FROM UserInRole u LEFT JOIN u.crProfile AS crProfile LEFT JOIN u.careProfile AS careProfile LEFT JOIN u.frailtyStatusTimeline AS fst LEFT JOIN fst.cdFrailtyStatus AS cdFrailtyStatus WHERE u.roleId = :roleId AND u.pilotCode = :pilotCode AND (fst IS NULL OR fst.changed = (SELECT max(fst.changed) FROM FrailtyStatusTimeline fst WHERE fst.userInRoleId = u.id))")
	List<UserInRole> findByRoleIdAndPilotCode(@Param("roleId") final Short roleId,@Param("pilotCode") final String pilotCode);

	@Query("SELECT u FROM UserInRole u INNER JOIN FETCH u.userInSystem AS userInSystem WHERE userInSystem.username = :username AND userInSystem.password=:password")
	UserInRole findBySystemUsernameAndPassword(@Param("username") final String username, @Param("password") final String password);

	List<UserInRole> findByUserInSystemId(Long id);

}