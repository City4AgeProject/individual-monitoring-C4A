package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;

@Repository(value = "audienceRolesRepository")
@Transactional(readOnly = true)
public interface AudienceRolesRepository extends GenericRepository<AssessmentAudienceRole, Long> {
	
	List<AssessmentAudienceRole> findByAssessmentId(Integer assessmentId);

}