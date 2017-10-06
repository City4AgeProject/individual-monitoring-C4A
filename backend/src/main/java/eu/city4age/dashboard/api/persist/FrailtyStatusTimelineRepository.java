package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "frailtyStatusTimelineRepository")
@Transactional(readOnly = true)
public interface FrailtyStatusTimelineRepository extends GenericRepository<FrailtyStatusTimeline, Long> {

	@Query("SELECT fst FROM FrailtyStatusTimeline fst WHERE fst.timeInterval IN :timeintervals AND fst.userInRole.id = :userId")
	List<FrailtyStatusTimeline> findByPeriodAndUserId(@Param("timeintervals") final List<TimeInterval> timeintervals,
			@Param("userId") final Long uId);

	@Query("SELECT fst FROM FrailtyStatusTimeline fst INNER JOIN fst.timeInterval ti WHERE fst.userInRole.id = :userId AND ti.id = (SELECT MAX(ti1.id) FROM FrailtyStatusTimeline fst1 INNER JOIN fst1.timeInterval ti1 WHERE fst1.userInRole.id = :userId)")
	FrailtyStatusTimeline findLatest(@Param("userId") final Long uId);

}