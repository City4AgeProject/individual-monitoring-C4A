package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.SourceEvidence;

@Repository(value = "sourceEvidenceRepository")
public interface SourceEvidenceRepository extends GenericRepository<SourceEvidence, Long> {
	
}