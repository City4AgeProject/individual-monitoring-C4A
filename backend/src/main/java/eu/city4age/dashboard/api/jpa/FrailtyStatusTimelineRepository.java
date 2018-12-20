package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "frailtyStatusTimelineRepository")
public interface FrailtyStatusTimelineRepository extends GenericRepository<FrailtyStatusTimeline, Long> {

	@Query("SELECT fst FROM FrailtyStatusTimeline fst INNER JOIN fst.timeInterval ti WHERE fst.timeInterval IN :timeintervals AND fst.userInRole.id = :userId ORDER BY ti.intervalStart ASC")
	List<FrailtyStatusTimeline> findByPeriodAndUserId(@Param("timeintervals") final List<TimeInterval> timeintervals,
			@Param("userId") final Long uId);
	
	@Query("SELECT fst FROM FrailtyStatusTimeline fst INNER JOIN fst.timeInterval ti WHERE fst.timeInterval.id IN :timeintervals AND fst.userInRole.id = :userId ORDER BY ti.intervalStart ASC")
	List<FrailtyStatusTimeline> findByPeriodIdsAndUserId(@Param("timeintervals") final List<Long> timeintervals,
			@Param("userId") final Long uId);
	
}