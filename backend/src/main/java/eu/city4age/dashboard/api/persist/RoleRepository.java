package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Role;

@Repository(value = "roleRepository")
public interface RoleRepository extends GenericRepository<Role, Long> {

	List<Role> findByStakeholderAbbreviation(@Param("stakeholderAbbr") final String stakeholderAbbr);

}