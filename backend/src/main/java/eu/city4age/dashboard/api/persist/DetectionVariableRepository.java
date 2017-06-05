package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;

@Repository(value = "detectionVariableRepository")
@Transactional(readOnly = true)
public interface DetectionVariableRepository extends GenericRepository<DetectionVariable, Long> {}