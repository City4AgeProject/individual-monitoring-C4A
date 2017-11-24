package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;

@Repository(value = "viewPilotDetectionVariableRepository")
public interface ViewPilotDetectionVariableRepository extends GenericRepository<ViewPilotDetectionVariable, Long> {
	
	@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'GES'")
	List<ViewPilotDetectionVariable> findAllGes(@Param("userId") final Long userId);
	
	@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'GEF'")
	List<ViewPilotDetectionVariable> findAllGef(@Param("userId") final Long userId);

}