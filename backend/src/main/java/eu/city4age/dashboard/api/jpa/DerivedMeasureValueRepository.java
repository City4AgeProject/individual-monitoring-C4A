package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.util.Date;
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
	
	@Query("SELECT DISTINCT ti.intervalStart FROM DerivedMeasureValue dmv INNER JOIN dmv.timeInterval ti WHERE dmv.userInRoleId = :uirId AND dmv.detectionVariableId = :dvId AND ti.intervalStart >= :intervalStart AND ti.intervalStart <= :intervalEnd")
	List<Date> findDatesForUserInRoleIdAndDetectionVariableIdForInterval(@Param("uirId") Long uirId, @Param("dvId") Long dvId, @Param("intervalStart") Date intervalStart, @Param("intervalEnd") Date intervalEnd);

	@Query("SELECT dmv.dmValue FROM DerivedMeasureValue dmv INNER JOIN dmv.timeInterval ti WHERE dmv.userInRoleId = :uirId AND dmv.detectionVariableId = :dvId AND ti.intervalStart = :intervalStart")
	BigDecimal findByUserInRoleIdAndDetectionVariableIdForOneMonth(@Param("uirId") Long uirId, @Param("dvId") Long dvId, @Param("intervalStart") Date intervalStart);

}
