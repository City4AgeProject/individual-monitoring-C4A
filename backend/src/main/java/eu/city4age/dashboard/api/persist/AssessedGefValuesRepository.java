package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessedGefValueSet;

@Repository(value = "assessedGefValuesRepository")
@Transactional(readOnly = true)
public interface AssessedGefValuesRepository extends GenericRepository<AssessedGefValueSet, Long> {
	
	List<AssessedGefValueSet> findByAssessmentId(Integer assessmentId);

}