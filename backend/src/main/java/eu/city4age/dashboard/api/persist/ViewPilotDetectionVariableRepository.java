package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;

@Repository(value = "viewPilotDetectionVariableRepository")
@Transactional(readOnly = true)
public interface ViewPilotDetectionVariableRepository extends GenericRepository<ViewPilotDetectionVariable, Long> {
	
	//@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv INNER JOIN vpdv.variationMeasureValues vms INNER JOIN vms.timeInterval ti WHERE ti.intervalStart >= :startOfMonth AND ti.intervalStart <= :endOfMonth AND vms.id = (SELECT max(vms1.id) FROM VariationMeasureValue vms1 INNER JOIN vms1.timeInterval ti1 WHERE ti1.intervalStart >= :startOfMonth AND ti1.intervalStart <= :endOfMonth ) AND vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'MEA' AND vpdv.derivedDetectionVariableType = 'GES'")
	//@Query("SELECT	 vpdv FROM ViewPilotDetectionVariable vpdv INNER JOIN vpdv.variationMeasureValues vms INNER JOIN vms.timeInterval ti WHERE ti.intervalStart >= :startOfMonth AND ti.intervalStart <= :endOfMonth AND vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'MEA' AND vpdv.id.derivedDetectionVariableId = :dvId")
	@Query("SELECT	 vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'MEA' AND vpdv.id.derivedDetectionVariableId = :dvId")
	List<ViewPilotDetectionVariable> findAllMeaGes(@Param("userId") final Long userId, @Param("dvId") final Long dvId);
	
	@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'GES'")
	List<ViewPilotDetectionVariable> findAllGes(@Param("userId") final Long userId);
	
	@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'GEF'")
	List<ViewPilotDetectionVariable> findAllGef(@Param("userId") final Long userId);

}