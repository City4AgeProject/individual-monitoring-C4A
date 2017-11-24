package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;

@Repository(value = "detectionVariableTypeRepository")
public interface DetectionVariableTypeRepository extends GenericRepository<DetectionVariableType, Long> {

}