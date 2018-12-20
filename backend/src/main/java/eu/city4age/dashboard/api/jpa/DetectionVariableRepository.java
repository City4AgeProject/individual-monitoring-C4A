package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;

@Repository(value = "detectionVariableRepository")
public interface DetectionVariableRepository extends GenericRepository<DetectionVariable, Long> {

	DetectionVariable findByDetectionVariableNameAndDetectionVariableType(String string, DetectionVariableType dvt);
		
	List<DetectionVariable> findByDetectionVariableType(DetectionVariableType dvt);

}
