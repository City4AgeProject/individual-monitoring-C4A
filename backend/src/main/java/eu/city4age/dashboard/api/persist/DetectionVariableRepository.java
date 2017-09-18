package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;

@Repository(value = "detectionVariableRepository")
@Transactional(readOnly = true)
public interface DetectionVariableRepository extends GenericRepository<DetectionVariable, Long> {

	DetectionVariable findOneByDetectionVariableName(String name);

	DetectionVariable findByDetectionVariableName(String string);

	DetectionVariable findByDetectionVariableNameAndDetectionVariableType(String string, DetectionVariableType dvt);
	
	@Query("SELECT dv FROM DetectionVariable dv INNER JOIN dv.pilotDetectionVariable pdv INNER JOIN dv.detectionVariableType dvt WHERE dvt.detectionVariableType = 'MEA' AND pdv.pilotCode = :pilotCode AND (dv.defaultTypicalPeriod = 'DAY' OR dv.defaultTypicalPeriod = '1WK')")
	List<DetectionVariable> findAllMEADvTypeByPilotCode(@Param("pilotCode") String pilotCode);

}
