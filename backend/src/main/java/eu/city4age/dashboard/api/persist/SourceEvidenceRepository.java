package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.SourceEvidence;

@Repository(value = "sourceEvidenceRepository")
@Transactional(readOnly = true)
public interface SourceEvidenceRepository extends GenericRepository<SourceEvidence, Long> {
	
	@Query("SELECT se FROM SourceEvidence se INNER JOIN FETCH se.geriatricFactorValue AS gfv WHERE se.sourceEvidenceId.detectionVariableType = 'GEF'")
	List<SourceEvidence> findAllGef();
	
	@Query("SELECT se FROM SourceEvidence se INNER JOIN FETCH se.variationMeasureValue AS vmv WHERE se.sourceEvidenceId.detectionVariableType = 'MEA'")
	List<SourceEvidence> findAllMea();
	
}