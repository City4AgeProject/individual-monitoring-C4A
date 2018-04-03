package eu.city4age.dashboard.api.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorPredictionValue;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "geriatricFactorPredictionValueRepository")
public interface GeriatricFactorPredictionValueRepository extends GenericRepository<GeriatricFactorPredictionValue, Long> {

	@Query("SELECT g FROM GeriatricFactorValue g INNER JOIN g.detectionVariable dv INNER JOIN dv.pilotDetectionVariable pdv INNER JOIN g.userInRole uir WHERE pdv.derivedDetectionVariable.id = :varId AND g.userInRole.id = :userId AND pdv.pilotCode = (SELECT uir.pilotCode FROM uir WHERE id = :userId) ORDER BY g.timeInterval.intervalStart ASC")
	List<GeriatricFactorValue> findByDetectionVariableId(@Param("varId") final Long dvId,
			@Param("userId") final Long uId);

	
	@Query("SELECT g FROM GeriatricFactorPredictionValue g WHERE g.timeInterval.intervalStart > :timeInterval AND g.userInRoleId = :userInRoleId")
	List<GeriatricFactorPredictionValue> deleteObsoletePredictions(@Param("timeInterval") final Date timeInterval, @Param("userInRoleId") final Long userInRoleId);
	

	@Query("SELECT g from GeriatricFactorPredictionValue g")
	List<GeriatricFactorPredictionValue> getAllPredictions();
	
}
