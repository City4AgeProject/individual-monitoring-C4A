package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.VariationMeasureValue;

@Repository(value = "variationMeasureValueRepository")
public interface VariationMeasureValueRepository extends GenericRepository<VariationMeasureValue, Long> {

	@Query("SELECT DISTINCT vm FROM VariationMeasureValue vm LEFT JOIN FETCH vm.detectionVariable dv LEFT JOIN FETCH vm.timeInterval ti LEFT JOIN FETCH vm.valueEvidenceNotice ven WHERE vm.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :gesId AND dv.detectionVariableType = 'mea') AND vm.userInRole.id = :uId ORDER BY ti.intervalStart")				
	List<VariationMeasureValue> findByUserAndGes(@Param("uId") final Long uId, @Param("gesId") final Long gesId);

	@Query("SELECT vm FROM VariationMeasureValue vm LEFT JOIN FETCH vm.userInRole uir LEFT JOIN FETCH vm.timeInterval ti WHERE uir.pilotCode IN :pilotCodes AND (ti.intervalStart >= :intervalStart OR (ti.intervalEnd IS NULL OR ti.intervalEnd >= :intervalStart)) AND (ti.intervalStart <= :intervalEnd OR (ti.intervalEnd IS NULL OR ti.intervalEnd <= :intervalEnd)) AND (ti.typicalPeriod IS NULL OR ti.typicalPeriod = 'day' OR ti.typicalPeriod = '1wk')")
	List<VariationMeasureValue> findAllForMonthByPilotCodeNui(@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd, @Param("pilotCodes") final List<Pilot.PilotCode> pilotCodes);
	
	/*
	 * 
	 *  query koji vraca sve VariationMeasureValue objekte koji su vezani na measure koji imaju veze sa SLEEP zbog fixa, kojima su intervalStart i intervalEnd u istom danu
	 * 
	 */
	
	@Query ("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.timeInterval ti INNER JOIN vm.detectionVariable dv WHERE dv.detectionVariableName like '%sleep%' AND DATE_TRUNC ('day', TIMEZONE ('UTC', ti.intervalStart)) = DATE_TRUNC ('day', TIMEZONE ('UTC', ti.intervalEnd))")
	List<VariationMeasureValue> findAllSleepMeasuresInSameDay ();
	
	/*
	 * 
	 *  query koji vraca sve VariationMeasureValue objekte koji su vezani na measure koji imaju veze sa SLEEP zbog fixa, kojima su intervalStart i intervalEnd u razlicitim danima
	 * 
	 */
	
	@Query ("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.timeInterval ti INNER JOIN vm.detectionVariable dv WHERE dv.detectionVariableName like '%sleep%' AND DATE_TRUNC ('day', TIMEZONE ('UTC', ti.intervalStart)) != DATE_TRUNC ('day', TIMEZONE ('UTC', ti.intervalEnd))")
	List<VariationMeasureValue> findAllSleepMeasuresInDifferentDays ();
	
	/*
	 * 
	 * query koji vraca vmv za zadatog usera, measure i timeInterval
	 * 
	 */
	
	@Query ("SELECT vm FROM VariationMeasureValue vm INNER JOIN vm.timeInterval ti INNER JOIN vm.detectionVariable dv INNER JOIN vm.userInRole uir WHERE uir.id = :userInRoleID AND dv.id = :measureTypeID AND ti.id = :timeIntervalID")
	VariationMeasureValue findByUserInRoleIdAndMeasureTypeIdAndTimeIntervalId (@Param ("userInRoleID") Long userInRoleID, @Param ("measureTypeID") Long measureTypeID, @Param ("timeIntervalID") Long timeIntervalID);

	//@Query("SELECT uir.id, dv.derivedDetectionVariable.id, vm.measureValue, pdv.derivationWeight FROM VariationMeasureValue vm, PilotDetectionVariable pdv INNER JOIN vm.detectionVariable dv INNER JOIN dv.pilotDetectionVariable pdv1 INNER JOIN vm.userInRole uir INNER JOIN vm.timeInterval ti WHERE dv.derivedDetectionVariable IS NOT NULL AND pdv1 IS NOT NULL AND pdv1.pilotCode = uir.pilotCode AND dv.derivedDetectionVariable.detectionVariableType = :dvType AND uir.pilotCode IN :pilotCodes AND ti.intervalStart >= :intervalStart AND ti.intervalStart <= :intervalEnd AND ti.typicalPeriod = 'mon' AND pdv.detectionVariable.detectionVariableType = :dvType AND pdv.pilotCode = uir.pilotCode AND pdv.detectionVariable = dv.derivedDetectionVariable")
	//List<Object[]> computeAllDirect(@Param("intervalStart") Timestamp startOfMonth, @Param("intervalEnd") Timestamp endOfMonth, @Param("pilotCodes") List<Pilot.PilotCode> pilotCodes, @Param("dvType") DetectionVariableType detectionVariableType);

	@Query("SELECT uir.id, dv.derivedDetectionVariable.id, vm.measureValue, pdv.derivationWeight FROM VariationMeasureValue vm, PilotDetectionVariable pdv INNER JOIN vm.detectionVariable dv INNER JOIN dv.pilotDetectionVariable pdv1 INNER JOIN vm.userInRole uir INNER JOIN vm.timeInterval ti WHERE dv.derivedDetectionVariable IS NOT NULL AND pdv1 IS NOT NULL AND pdv1.pilotCode = uir.pilotCode AND dv.derivedDetectionVariable.detectionVariableType = :dvType AND uir.pilotCode IN :pilotCodes AND ti.intervalStart >= :intervalStart AND ti.intervalStart <= :intervalEnd AND ti.typicalPeriod = 'mon' AND pdv.detectionVariable.detectionVariableType = :dvType AND pdv.pilotCode = uir.pilotCode AND pdv.detectionVariable = dv.derivedDetectionVariable")
	List<Object[]> computeAllDirect(@Param("intervalStart") Timestamp startOfMonth, @Param("intervalEnd") Timestamp endOfMonth, @Param("pilotCodes") List<Pilot.PilotCode> pilotCodes, @Param("dvType") DetectionVariableType detectionVariableType);
	
}