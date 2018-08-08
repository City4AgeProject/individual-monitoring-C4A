package eu.city4age.dashboard.api.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.CareProfile;



@Repository(value = "careProfileRepository")
public interface CareProfileRepository extends GenericRepository<CareProfile, Long> {


	@Query("SELECT cp FROM CareProfile cp WHERE cp.userInRoleId = :userInRoleId")
	CareProfile findByUserId(@Param("userInRoleId") final Long userInRoleId);


	
}



