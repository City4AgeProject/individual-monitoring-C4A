package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AssessmentAudienceRole;

@Repository(value = "audienceRolesRepository")
public interface AudienceRolesRepository extends GenericRepository<AssessmentAudienceRole, Long> {
	
	List<AssessmentAudienceRole> findByAssessmentId(Integer assessmentId);

}