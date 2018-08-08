package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "geriatricFactorRepository")	
public interface GeriatricFactorRepository extends GenericRepository<GeriatricFactorValue, Long> {

	@Query("SELECT g FROM GeriatricFactorValue g INNER JOIN g.detectionVariable dv INNER JOIN dv.pilotDetectionVariable pdv INNER JOIN g.userInRole uir WHERE pdv.derivedDetectionVariable.id = :varId AND g.userInRole.id = :userId AND pdv.pilotCode = (SELECT uir.pilotCode FROM uir WHERE id = :userId) ORDER BY g.timeInterval.intervalStart ASC")
	List<GeriatricFactorValue> findByDerivedDetectionVariableId(@Param("varId") final Long dvId,
			@Param("userId") final Long uId);

	@Query("SELECT g FROM GeriatricFactorValue g INNER JOIN g.detectionVariable dv WHERE dv.detectionVariableType = 'ges'")
	List<GeriatricFactorValue> findAllGes();

	@Query("SELECT g FROM GeriatricFactorValue g INNER JOIN g.detectionVariable dv INNER JOIN g.userInRole uir WHERE dv.id = :varId AND uir.id = :userId ORDER BY g.timeInterval.intervalStart ASC")
	List<GeriatricFactorValue> findByDetectionVariableId(@Param("varId") final Long dvId,
			@Param("userId") final Long uId);
	
	@Query ("SELECT ti FROM GeriatricFactorValue gfv INNER JOIN gfv.userInRole uir INNER JOIN gfv.timeInterval ti WHERE uir.id = :userInRoleId AND ti.intervalStart = (SELECT max(ti1.intervalStart) FROM GeriatricFactorValue gfv1 INNER JOIN gfv1.userInRole uir1 INNER JOIN gfv1.timeInterval ti1 WHERE uir1.id = :userInRoleId)")
	TimeInterval findMaxIntervalStartByUserInRole(@Param ("userInRoleId") Long userInRoleId);

}