package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;
import eu.city4age.dashboard.api.pojo.dto.LastFiveAssessment;

@Repository(value = "timeIntervalRepository")
@Transactional(readOnly = true)
public interface TimeIntervalRepository extends GenericRepository<TimeInterval, Long> {

	@Query("SELECT ti FROM TimeInterval ti WHERE ti.intervalStart >= :start AND ti.intervalEnd <= :end")
	List<TimeInterval> findByPeriod(@Param("start") final Timestamp start, @Param("end") final Timestamp end);

	// Poziva ovo posle care recipients
	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.cdDetectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt.detectionVariableType IN :gefType OR dv IS NULL) ORDER BY ti.id")
	List<TimeInterval> getGroups(@Param("userId") final Long userId, @Param("gefType") final List<String> parentFactors);

	@Query(nativeQuery = true)
	List<LastFiveAssessment> findLastFiveForDiagram(@Param("userInRoleId") final Long userInRoleId,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query("SELECT ti, ti.geriatricFactorValue FROM TimeInterval ti LEFT JOIN ti.geriatricFactorValue AS geriatricFactorValue INNER JOIN geriatricFactorValue.cdDetectionVariable AS cdDetectionVariable WHERE geriatricFactorValue.userInRole.id = :userInRoleId AND cdDetectionVariable.derivedDetectionVariable.id = :parentId AND ti.intervalStart >= :start AND ti.intervalEnd <= :end")
	List<Object[]> findByUserInRoleId(@Param("userInRoleId") final Long crId, @Param("parentId") final Long dvParentId,
			@Param("start") final Timestamp start, @Param("end") final Timestamp end);

}