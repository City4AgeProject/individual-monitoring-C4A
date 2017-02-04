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

	@Query("SELECT fst FROM FrailtyStatusTimeline fst WHERE fst.timeInterval.id IN :timeintervalIds AND fst.userInRole.id = :userId")
	List<FrailtyStatusTimeline> findByPeriodAndUserIdOld(@Param("timeintervalIds") final List<Long> timeintervalIds,
			@Param("userId") final Long uId);

}