package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;

@Repository(value = "nuiRepository")
@Transactional(readOnly = true)
public interface NUIRepository extends GenericRepository<NumericIndicatorValue, Long> {
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN nui.userInRole uir LEFT JOIN nui.timeInterval ti WHERE nui.detectionVariable = :gesDv AND uir.pilotCode = :pilotCode AND ti.intervalStart = :yearMonth AND ti.typicalPeriod = 'MON'")
	List<NumericIndicatorValue> getNuisFor1Month(@Param("pilotCode") final String pilotCode,
			@Param("yearMonth") final Timestamp yearMonth, @Param("gesDv") final DetectionVariable gesDv);

	@Query("SELECT MIN(nui.id) FROM NumericIndicatorValue nui WHERE nui.detectionVariable.id = :dvId AND nui.userInRole.id = :uirId")
	Long findMonthZero(@Param("dvId") Long dvId, @Param("uirId") Long uirId);

	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN FETCH nui.detectionVariable dv INNER JOIN FETCH nui.userInRole uir LEFT JOIN FETCH nui.timeInterval ti WHERE nui.detectionVariable.id IN (SELECT pdv.detectionVariable.id FROM PilotDetectionVariable pdv INNER JOIN pdv.detectionVariable dv WHERE pdv.derivedDetectionVariable.id = :dvId AND dv.detectionVariableType = 'NUI') AND nui.userInRole.id = :uirId")
	List<NumericIndicatorValue> getNuisForSelectedGes(@Param("uirId") Long uirId, @Param("dvId") Long dvId);
}