package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;

@Repository(value = "detectionVariableRepository")
public interface DetectionVariableRepository extends GenericRepository<DetectionVariable, Long> {

	DetectionVariable findByDetectionVariableNameAndDetectionVariableType(String string, DetectionVariableType dvt);

}
