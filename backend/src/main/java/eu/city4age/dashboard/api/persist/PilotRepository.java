package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;

@Repository(value = "pilotRepository")
public interface PilotRepository extends GenericRepository<Pilot, String> {

	Pilot findByPilotCode(String pilotCode);

}
