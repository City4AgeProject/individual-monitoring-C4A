package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Role;

@Repository(value = "roleRepository")
public interface RoleRepository extends GenericRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.stakeholderAbbreviation IN :stakeholderAbbr")
	List<Role> findByStakeholderAbbreviation(@Param("stakeholderAbbr") final List<String> stakeholderAbbr);

}