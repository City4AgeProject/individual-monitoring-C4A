package eu.city4age.dashboard.api.persist;

import java.math.BigDecimal;
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
	
	@Query("SELECT pdv.derivationWeight FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode AND dv.detectionVariableType = 'GES' AND ddv.detectionVariableType = 'GEF'")
	BigDecimal findWeightByDetectionVariableAndPilotCodeGesGef(@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") String pilotCode);
	
	PilotDetectionVariable findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(
			String pilotCode, Long id, Long id2);
	
	List<PilotDetectionVariable> findByPilotCodeOrderByDetectionVariableId (String pilotCode );

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN FETCH pdv.detectionVariable dv INNER JOIN FETCH pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode")
	PilotDetectionVariable findByDetectionVariableAndPilotCode(@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") String pilotCode);

}