package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Role;

@Repository(value = "roleRepository")
@Transactional(readOnly = true)
public interface RoleRepository extends GenericRepository<Role, Long> {

	List<Role> findByStakeholderAbbreviation(@Param("stakeholderAbbr") final String stakeholderAbbr);

}