package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;
import eu.city4age.dashboard.api.pojo.dto.Nuis;

@Repository(value = "variationMeasureValueRepository")
@Transactional(readOnly = true)
public interface VariationMeasureValueRepository extends GenericRepository<VariationMeasureValue, Long> {
	
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN FETCH vm.detectionVariable dv LEFT JOIN FETCH vm.timeInterval ti WHERE vm.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :gesId AND dv.detectionVariableType = 'MEA') AND vm.userInRole.id = :uId ORDER BY ti.intervalStart")				
	List<VariationMeasureValue> findByUserAndGes(@Param("uId") final Long uId, @Param("gesId") final Long gesId);
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.detectionVariable dv LEFT JOIN vm.timeInterval ti WHERE dv.id = :dvId AND vm.userInRole.id = :uId AND ti.intervalStart >= :intervalStart AND ti.intervalStart <= :intervalEnd AND ((ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'MON') )")
	List<VariationMeasureValue> findMMByUserInRoleId(@Param("uId") final Long uId, @Param("dvId") final Long dvId, @Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.detectionVariable dv LEFT JOIN vm.timeInterval ti WHERE dv.id = :dvId AND vm.userInRole.id = :uId AND ti.intervalStart >= :intervalStart AND ti.intervalStart <= :intervalEnd ")
	List<VariationMeasureValue> findByUserInRoleId(@Param("uId") final Long uId, @Param("dvId") final Long dvId, @Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.userInRole uir LEFT JOIN vm.timeInterval ti WHERE uir.pilotCode = :pilotCode AND (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'DAY' OR ti.typicalPeriod = '1WK' OR ti.typicalPeriod = 'MON')")
	List<VariationMeasureValue> findAllForMonthByPilotCode(@Param("pilotCode") final String pilotCode,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query("SELECT MIN(vm.id) FROM VariationMeasureValue vm WHERE vm.detectionVariable = :dv AND vm.userInRole.id = :uirId")
	Long findMinId(@Param("dv") DetectionVariable dv, @Param("uirId") Long uirId);
	
	@Query("SELECT vm FROM VariationMeasureValue vm LEFT JOIN vm.timeInterval ti WHERE (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'DAY' OR ti.typicalPeriod = '1WK')")
	List<VariationMeasureValue> findAllForMonthByPilotCodeNui(@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query(nativeQuery = true)
	List<Nuis> doAllNui(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth);
	
	@Query("SELECT vm FROM VariationMeasureValue vm LEFT JOIN vm.timeInterval ti WHERE (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'DAY' OR ti.typicalPeriod = '1WK' OR ti.typicalPeriod = 'MON')")
	List<VariationMeasureValue> findAllForMonth(@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);
	
}