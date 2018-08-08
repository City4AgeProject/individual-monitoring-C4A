package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;

@Repository(value = "nuiRepository")
public interface NUIRepository extends GenericRepository<NumericIndicatorValue, Long> {
	
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN FETCH nui.detectionVariable dv INNER JOIN FETCH nui.userInRole uir LEFT JOIN FETCH nui.timeInterval ti WHERE nui.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :dvId AND dv.detectionVariableType = 'nui') AND nui.userInRole.id = :uirId ORDER BY ti.intervalStart")
	List<NumericIndicatorValue> getNuisForSelectedGes(@Param("uirId") Long uirId, @Param("dvId") Long dvId);
	        
	        
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN FETCH nui.detectionVariable dv INNER JOIN FETCH nui.userInRole uir LEFT JOIN FETCH nui.timeInterval ti WHERE nui.detectionVariable.id IN (SELECT pdv.derivedDetectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN UserInRole uir ON pdv.pilotCode=uir.pilotCode WHERE pdv.detectionVariable.id=:meaId) AND nui.userInRole.id = :uirId ORDER BY ti.intervalStart")
	List<NumericIndicatorValue> getNuisForSelectedMea(@Param("uirId") Long uirId, @Param("meaId") Long meaId);
	
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN FETCH nui.detectionVariable dv INNER JOIN FETCH nui.userInRole uir LEFT JOIN FETCH nui.timeInterval ti WHERE nui.userInRole.id = :uirId ORDER BY ti.intervalStart")
	List<NumericIndicatorValue> getNuisForAllMea(@Param("uirId") Long uirId);


}