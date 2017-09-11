package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;

@Repository(value = "pilotDetectionVariableRepository")
@Transactional(readOnly = true)
public interface PilotDetectionVariableRepository extends GenericRepository<PilotDetectionVariable, Long> {
	
	@Query(nativeQuery = true, value = "SELECT pdv.derivation_function_formula FROM md_pilot_detection_variable AS pdv")
	String runFormula();

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode AND dv.detectionVariableType = 'MEA' AND ddv.detectionVariableType = 'GES'")
	PilotDetectionVariable findByDetectionVariableAndPilotCodeMeaGes(
			@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") String pilotCode);

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN dv.detectionVariableType dvt WHERE dvt.detectionVariableType = 'NUI'")
	List<PilotDetectionVariable> findAllForDvtNUI();

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.pilotCode = :pilotCode AND dv.detectionVariableName = pdv.formula || CAST('_' AS string) || :detectionVariableName")
	List<PilotDetectionVariable> findAllDvNuisForMeasure(@Param("detectionVariableName") String detectionVariableName,
			@Param("pilotCode") String pilotCode);

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN dv.detectionVariableType dvt WHERE dvt.detectionVariableType = 'MEA' AND pdv.pilotCode = :pilotCode AND dv.defaultTypicalPeriod = 'DAY'")
	List<PilotDetectionVariable> findAllMEADvTypeByPilotCode(@Param("pilotCode") String pilotCode);
	
	@Query("SELECT pdv.derivationWeight FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode AND dv.detectionVariableType = 'GES' AND ddv.detectionVariableType = 'GEF'")
	BigDecimal findByDetectionVariableAndPilotCodeGesGef(@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") String pilotCode);
	PilotDetectionVariable findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableIdAndValidFrom(
			String pilotCode, Long id, Long id2, Date validFrom);
	

}