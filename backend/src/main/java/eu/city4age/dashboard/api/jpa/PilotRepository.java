package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;

@Repository(value = "pilotRepository")
public interface PilotRepository extends GenericRepository<Pilot, String> {

	Pilot findByPilotCode(String pilotCode);

	@Query("SELECT p FROM Pilot p WHERE p.latestSubmissionCompleted IS NOT NULL AND p.latestVariablesComputed IS NULL ")
	List<Pilot> findAllNeverComputed();

	@Query("SELECT p FROM Pilot p WHERE p.latestSubmissionCompleted IS NOT NULL AND p.latestVariablesComputed IS NOT NULL AND p.latestSubmissionCompleted > p.latestVariablesComputed")
	List<Pilot> findAllComputed();

}
