package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Activity;

@Repository(value = "activityRepository")
public interface ActivityRepository extends GenericRepository<Activity, Long> {

	Activity findOneByName(String name);
	
}
