package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;

@Repository(value = "vmvFilteringRepository")
public interface VmvFilteringRepository extends GenericRepository<VmvFiltering, Long> {
	
	@Query ("SELECT DISTINCT vf.filterType FROM VmvFiltering vf WHERE vf.assessment = :assessment")
	public Character findFilterTypeByAssessmentId (@Param ("assessment") Assessment assessment);

	@Query ("SELECT vmv.id FROM VmvFiltering vf INNER JOIN vf.vmv vmv WHERE vf.assessment = :assessment")
	public List<Long> findVmvIDsByAssessment(@Param ("assessment") Assessment a);
}