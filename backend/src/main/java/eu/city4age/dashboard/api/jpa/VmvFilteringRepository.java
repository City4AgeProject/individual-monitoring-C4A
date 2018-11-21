package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;

@Repository(value = "vmvFilteringRepository")
public interface VmvFilteringRepository extends GenericRepository<VmvFiltering, Long> {
	
	@Query ("SELECT DISTINCT vf.filterType FROM VmvFiltering vf WHERE vf.assessment = :assessment")
	public Character findFilterTypeByAssessmentId (@Param ("assessment") Assessment assessment);

	@Query ("SELECT vmv.id FROM VmvFiltering vf INNER JOIN vf.vmv vmv INNER JOIN vmv.userInRole uir INNER JOIN vmv.detectionVariable dv WHERE vf.assessment = :assessment AND uir = :uir AND dv = :measureType")
	public List<Long> findVmvIDsByAssessment(@Param ("assessment") Assessment a, @Param ("uir") UserInRole uir, @Param ("measureType") DetectionVariable measureType);
	
	@Query ("SELECT vf FROM VmvFiltering vf INNER JOIN vf.vmv vmv WHERE vmv.id = :vmvId ORDER BY vf.validFrom DESC")
	public List<VmvFiltering> findFilterTypeByVmvId (@Param ("vmvId") Long vmvId);
	
	@Query ("SELECT DISTINCT vf FROM VmvFiltering vf WHERE vf.assessment = :assessment")
	public List<VmvFiltering> findByAssessment (@Param ("assessment") Assessment assessment);

	@Query ("SELECT DISTINCT uir FROM VmvFiltering vf INNER JOIN vf.vmv vmv INNER JOIN vmv.userInRole uir WHERE vf.assessment IS NULL ORDER BY uir ASC")
	public List<UserInRole> findUsersForSystemExclusion();

	@Query ("SELECT DISTINCT dv FROM VmvFiltering vf INNER JOIN vf.vmv vmv INNER JOIN vmv.userInRole uir INNER JOIN vmv.detectionVariable dv WHERE vf.assessment IS NULL AND uir = :uir ORDER BY dv ASC")
	public List<DetectionVariable> findMeasuresByUsersForSystemExclusion(@Param ("uir") UserInRole uir);

	@Query ("SELECT DISTINCT vf FROM VmvFiltering vf INNER JOIN vf.vmv vmv INNER JOIN vmv.userInRole uir INNER JOIN vmv.detectionVariable dv WHERE vf.assessment IS NULL AND uir = :uir AND dv = :dv ORDER BY vf ASC")
	public List<VmvFiltering> findFilteredByUserAndMeasureForSystemExclusion(@Param ("uir") UserInRole uir, @Param ("dv") DetectionVariable dv);
	
	/*@Query ("SELECT DISTINCT vf FROM VmvFiltering vf WHERE vf.assessment IS NULL")
	public List<VmvFiltering> findForSystemExclusion();*/
	
	@Query ("SELECT DISTINCT vf FROM VmvFiltering vf WHERE vf.assessment IS NULL ORDER BY vf.id")
	public List<VmvFiltering> findForSystemExclusion(Pageable size);
	
	@Query ("SELECT COUNT(DISTINCT vf) FROM VmvFiltering vf WHERE vf.assessment IS NULL")
	public int findCountOfVmvFiltering();
}