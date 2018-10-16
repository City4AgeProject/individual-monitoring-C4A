package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DerivedMeasureValue;

@Repository(value = "derivedMeasureValueRepository")
public interface DerivedMeasureValueRepository extends GenericRepository<DerivedMeasureValue, Long> {
	
	@Query("SELECT dmv FROM DerivedMeasureValue dmv WHERE dmv.userInRoleId = :userInRoleId AND dmv.derivedDetectionVariableId = :parentFactorId")
	List<DerivedMeasureValue> findByUserInRoleIdAndParentFactorId(@Param("userInRoleId") Long userInRoleId, @Param("parentFactorId") Long parentFactorId);

}
