package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@Repository(value = "variationMeasureValueRepository")
@Transactional(readOnly = true)
public interface VariationMeasureValueRepository extends GenericRepository<VariationMeasureValue, Long> {
	
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN FETCH vm.detectionVariable dv LEFT JOIN FETCH vm.timeInterval ti WHERE vm.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :gesId AND dv.detectionVariableType = 'MEA') AND vm.userInRole.id = :uId ORDER BY ti.intervalStart")				
	List<VariationMeasureValue> findByUserAndGes(@Param("uId") final Long uId, @Param("gesId") final Long gesId);
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.detectionVariable dv LEFT JOIN vm.timeInterval ti WHERE vm.userInRole.id = :uId AND (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'MON')")
	List<VariationMeasureValue> findAllByUserInRoleId(@Param("uId") final Long uId,
			@Param("intervalStart") final Timestamp intervalStart);

	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.userInRole uir LEFT JOIN vm.timeInterval ti WHERE uir.pilotCode = :pilotCode AND (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'DAY' OR ti.typicalPeriod = '1WK')")
	List<VariationMeasureValue> findAllForMonthByPilotCodeNui(@Param("pilotCode") final String pilotCode,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);
	
	@Query("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.userInRole uir LEFT JOIN vm.timeInterval ti WHERE uir.pilotCode = :pilotCode AND (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'DAY' OR ti.typicalPeriod = '1WK' OR ti.typicalPeriod = 'MON')")
	List<VariationMeasureValue> findAllForMonthByPilotCode(@Param("pilotCode") final String pilotCode,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query("SELECT MIN(vm.id) FROM VariationMeasureValue vm WHERE vm.detectionVariable = :dv AND vm.userInRole.id = :uirId")
	Long findMinId(@Param("dv") DetectionVariable dv, @Param("uirId") Long uirId);

	@Query(nativeQuery = true)
	BigDecimal doWeightedAvg();

	@Query(nativeQuery = true)
	BigDecimal doWeightedStDev();

	@Query(nativeQuery = true)
	BigDecimal doWeightedBest25Perc();

	@Query(nativeQuery = true)
	BigDecimal doWeightedDelta25PercAvg();

}