package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Pilot;

@Repository(value = "pilotRepository")
public interface PilotRepository extends GenericRepository<Pilot, Pilot.PilotCode> {

	Pilot findByPilotCode(Pilot.PilotCode pilotCode);

	@Query("SELECT p FROM Pilot p WHERE p.latestSubmissionCompleted IS NOT NULL AND p.latestVariablesComputed IS NULL AND p.latestConfigurationUpdate IS NOT NULL")
	List<Pilot> findAllNeverComputed();

	@Query("SELECT p FROM Pilot p WHERE p.latestSubmissionCompleted IS NOT NULL AND p.latestVariablesComputed IS NOT NULL AND p.latestSubmissionCompleted > p.latestVariablesComputed AND p.latestConfigurationUpdate IS NOT NULL")
	List<Pilot> findAllComputed();

	@Query("SELECT p FROM Pilot p WHERE p.latestVariablesComputed IS NOT NULL")
	List<Pilot> findPilotsComputed();
	
}
