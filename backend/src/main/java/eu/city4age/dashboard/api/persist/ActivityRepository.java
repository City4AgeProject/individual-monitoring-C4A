package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Activity;

@Repository(value = "activityRepository")
@Transactional(readOnly = true)
public interface ActivityRepository extends GenericRepository<Activity, Long> {

	Activity findOneByName(String name);
	
}
