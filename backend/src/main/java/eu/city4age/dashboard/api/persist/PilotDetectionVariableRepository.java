package eu.city4age.dashboard.api.persist;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;

@Repository(value = "pilotDetectionVariableRepository")
@Transactional(readOnly = true)
public interface PilotDetectionVariableRepository extends GenericRepository<PilotDetectionVariable, Long> {
	
	@Query(nativeQuery = true, value = "SELECT pdv.derivation_function_formula FROM md_pilot_detection_variable AS pdv")
	String runFormula();

}