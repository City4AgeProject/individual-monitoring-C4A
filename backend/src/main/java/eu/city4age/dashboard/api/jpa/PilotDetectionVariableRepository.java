package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;

@Repository(value = "pilotDetectionVariableRepository")
public interface PilotDetectionVariableRepository extends GenericRepository<PilotDetectionVariable, Long> {
	
	@Query("SELECT pdv.derivationWeight FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode AND dv.detectionVariableType = 'ges' AND ddv.detectionVariableType = 'gef'")
	BigDecimal findWeightByDetectionVariableAndPilotCodeGesGef(@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") String pilotCode);
	
	PilotDetectionVariable findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(
			Pilot.PilotCode pilotCode, Long id, Long id2);
	
	List<PilotDetectionVariable> findByPilotCodeOrderByDetectionVariableId (Pilot.PilotCode pilotCode );

	@Query("SELECT pdv FROM PilotDetectionVariable pdv INNER JOIN FETCH pdv.detectionVariable dv INNER JOIN FETCH pdv.derivedDetectionVariable ddv WHERE dv.id = :detectionVariableId AND pdv.pilotCode = :pilotCode")
	PilotDetectionVariable findByDetectionVariableAndPilotCode(@Param("detectionVariableId") Long detectionVariableId, @Param("pilotCode") Pilot.PilotCode pilotCode);


	@Query ("SELECT DISTINCT ddv FROM PilotDetectionVariable pdv INNER JOIN pdv.derivedDetectionVariable ddv WHERE pdv.pilotCode = :pilotCode AND ddv.detectionVariableType IN ('ovl', 'gfg', 'gef', 'ges') ")
	List<DetectionVariable> findDetectionVariablesForPrediction(@Param("pilotCode") Pilot.PilotCode pilotCode);
	
	@Query ("SELECT pdv.pilotCode FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE dv.id = :dvId")
	List<Pilot.PilotCode> findPilotCodeByDetectionVariableId (@Param ("dvId") Long dvId);
	
	@Query ("SELECT DISTINCT dv FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN dv.detectionVariableType dvt INNER JOIN pdv.derivedDetectionVariable ddv WHERE ddv.id = :dvId AND pdv.pilotCode IN :pilotCodes AND dvt <> 'nui'")
	List<DetectionVariable> findDetectionVariableByDerivedDetectionVariableIdAndPilotCodes (@Param ("dvId") Long dvId, @Param ("pilotCodes") List<Pilot.PilotCode> pilotCodes);

	@Query ("SELECT pdv.pilotCode FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv INNER JOIN dv.detectionVariableType dvt INNER JOIN pdv.derivedDetectionVariable ddv WHERE dv.id = :dvId AND dvt = :dvType AND ddv.id = :ddvId")
	List<Pilot.PilotCode> findPilotCodesByDetectionVariableIdAndDerivedDetectionVariableIdAndDetectionVariableType (@Param ("dvType") DetectionVariableType dvType, @Param ("dvId") Long dvId, @Param ("ddvId") Long ddvId);

}