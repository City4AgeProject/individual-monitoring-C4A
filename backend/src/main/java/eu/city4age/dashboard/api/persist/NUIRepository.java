package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;
import eu.city4age.dashboard.api.pojo.dto.Gfvs;

@Repository(value = "nuiRepository")
@Transactional(readOnly = true)
public interface NUIRepository extends GenericRepository<NumericIndicatorValue, Long> {
	
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN FETCH nui.detectionVariable dv INNER JOIN FETCH nui.userInRole uir LEFT JOIN FETCH nui.timeInterval ti WHERE nui.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :dvId AND dv.detectionVariableType = 'NUI') AND nui.userInRole.id = :uirId")
	List<NumericIndicatorValue> getNuisForSelectedGes(@Param("uirId") Long uirId, @Param("dvId") Long dvId);

	@Query(nativeQuery = true)
	List<Gfvs> doAllGess(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth);

}