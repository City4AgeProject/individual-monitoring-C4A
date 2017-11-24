package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.TimeInterval;

@Repository(value = "frailtyStatusTimelineRepository")
public interface FrailtyStatusTimelineRepository extends GenericRepository<FrailtyStatusTimeline, Long> {

	@Query("SELECT fst FROM FrailtyStatusTimeline fst WHERE fst.timeInterval IN :timeintervals AND fst.userInRole.id = :userId")
	List<FrailtyStatusTimeline> findByPeriodAndUserId(@Param("timeintervals") final List<TimeInterval> timeintervals,
			@Param("userId") final Long uId);

}