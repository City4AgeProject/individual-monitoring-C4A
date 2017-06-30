package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.NumericIndicatorValue;

@Repository(value = "nuiRepository")
@Transactional(readOnly = true)
public interface NUIRepository extends GenericRepository<NumericIndicatorValue, Long> {
	@Query("SELECT nui FROM NumericIndicatorValue nui INNER JOIN nui.userInRole uir LEFT JOIN nui.timeInterval ti WHERE uir.pilotCode = :pilotCode AND ti.intervalStart = :yearMonth AND ti.typicalPeriod = 'MON'")
	List<NumericIndicatorValue> getNuisFor1Month(@Param("pilotCode") final String pilotCode, @Param("yearMonth") final Timestamp yearMonth);
}