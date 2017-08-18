package eu.city4age.dashboard.api.persist;

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

}