package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.dto.Last5Assessment;

@Repository(value = "timeIntervalRepository")
@Transactional(readOnly = true)
public interface TimeIntervalRepository extends GenericRepository<TimeInterval, Long> {

	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt.detectionVariableType IN :gefType OR dv IS NULL) ORDER BY ti.intervalStart")
	List<TimeInterval> getGroups(@Param("userId") final Long userId, @Param("gefType") final List<DetectionVariableType.Type> parentFactors);

	@Query(nativeQuery = true)
	List<Last5Assessment> getLastFiveForDiagram(@Param("userInRoleId") final Long userInRoleId, @Param("parentDetectionVariableId") final Long parentDetectionVariableId,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);
	
	TimeInterval findByIntervalStartAndTypicalPeriod(Timestamp intervalStart, String typicalPeriod);
	
	/* returns timezone offset in miliseconds; used for converting timeinterval to UTC time */
	
	@Query("select extract (TIMEZONE from ti1.intervalStart ) from TimeInterval ti1 where (ti1.id = :Id OR ti1 IS NULL) ")
	Long getTimeZoneOffset(@Param("Id") final Long Id);
	
	

}