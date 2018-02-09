package eu.city4age.dashboard.api.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "timeIntervalRepository")
public interface TimeIntervalRepository extends GenericRepository<TimeInterval, Long> {

	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt IN :gefType OR dv IS NULL) ORDER BY ti.intervalStart")
	List<TimeInterval> getGroups2(@Param("userId") final Long userId, @Param("gefType") final List<DetectionVariableType> parentFactors);
	
	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt.detectionVariableType IN :gefType OR dv IS NULL) ORDER BY ti.intervalStart")
	List<TimeInterval> getGroups3(@Param("userId") final Long userId, @Param("gefType") final List<String> parentFactors);
	
	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt.detectionVariableType IN :gefType OR dv IS NULL) ORDER BY ti.intervalStart")
	List<TimeInterval> getGroups(@Param("userId") final Long userId, @Param("gefType") final List<DetectionVariableType.Type> parentFactors);
	
	TimeInterval findByIntervalStartAndTypicalPeriod(Date intervalStart, String typicalPeriod);

	@Query("SELECT ti FROM TimeInterval ti LEFT JOIN FETCH ti.geriatricFactorValue AS gfv LEFT JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.pilotDetectionVariable AS pdv LEFT JOIN FETCH gfv.userInRole AS uir LEFT JOIN FETCH uir.pilot AS p WHERE (uir IS NULL OR pdv IS NULL OR (uir.id = :careRecipientId AND pdv.derivedDetectionVariable.id = :parentFactorId AND pdv.pilotCode = uir.pilotCode)) AND (p IS NULL OR ti.typicalPeriod = 'MON' AND DATE_TRUNC('month', TIMEZONE(p.timeZone, ti.intervalStart) ) = TIMEZONE(p.timeZone, ti.intervalStart)) AND ti.intervalStart >= (SELECT min(ti1.intervalStart) FROM TimeInterval ti1 INNER JOIN ti1.geriatricFactorValue AS gfv1 INNER JOIN gfv1.detectionVariable AS dv1 INNER JOIN dv1.pilotDetectionVariable AS pdv1 INNER JOIN gfv1.userInRole AS uir1 INNER JOIN gfv1.userInRole AS uir1 WHERE pdv1.derivedDetectionVariable.id = :parentFactorId AND pdv1.pilotCode = uir1.pilotCode) ORDER BY ti.intervalStart ASC")
	List<TimeInterval> getDiagramDataForUserInRoleId(@Param("careRecipientId") final Long careRecipientId, @Param("parentFactorId") final Long parentFactorId);
	
	@Query("SELECT pi.timeZone FROM Pilot pi WHERE DATE_TRUNC('month', TIMEZONE(pi.timeZone, :date) ) = TIMEZONE(pi.timeZone, :date)")
	String testHql(@Param("date") final Date date);
	
}