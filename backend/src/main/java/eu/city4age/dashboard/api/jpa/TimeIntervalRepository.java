package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "timeIntervalRepository")
public interface TimeIntervalRepository extends GenericRepository<TimeInterval, Long> {

	@Query("SELECT DISTINCT ti FROM TimeInterval ti LEFT JOIN FETCH ti.frailtyStatusTimeline AS fst LEFT JOIN FETCH ti.geriatricFactorValue AS gfv INNER JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.detectionVariableType AS dvt INNER JOIN FETCH gfv.userInRole AS uir INNER JOIN FETCH uir.userInSystem AS uis WHERE (uir.id = :userId OR uir IS NULL) AND (dvt.detectionVariableType IN :gefType OR dv IS NULL) ORDER BY ti.intervalStart")
	List<TimeInterval> getGroups(@Param("userId") final Long userId, @Param("gefType") final List<DetectionVariableType.Type> parentFactors);
	
	TimeInterval findByIntervalStartAndTypicalPeriod(Timestamp intervalStart, String typicalPeriod);
	
	@Query("SELECT ti FROM TimeInterval ti LEFT JOIN FETCH ti.geriatricFactorValue AS gfv LEFT JOIN FETCH gfv.detectionVariable AS dv LEFT JOIN FETCH dv.pilotDetectionVariable AS pdv LEFT JOIN FETCH gfv.userInRole AS uir WHERE (uir IS NULL OR pdv IS NULL OR (uir.id = :careRecipientId AND pdv.derivedDetectionVariable.id = :parentFactorId AND pdv.pilotCode = uir.pilotCode)) AND ti.typicalPeriod = 'MON' AND DATE_TRUNC('month', TIMEZONE('UTC',ti.intervalStart) ) = TIMEZONE('UTC',ti.intervalStart) ORDER BY ti.intervalStart ASC")
	List<TimeInterval> getDiagramDataForUserInRoleId(@Param("careRecipientId") final Long careRecipientId, @Param("parentFactorId") final Long parentFactorId);
	
}