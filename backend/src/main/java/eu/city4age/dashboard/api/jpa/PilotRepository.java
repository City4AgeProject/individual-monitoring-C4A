package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	/* * query koji odredjuje timeInterval (tacnije pocetni mesec) za racunanje podataka na odredjenom pilotu */
	@Query ("SELECT DATE_TRUNC ('month', TIMEZONE (p.compZone, p.latestVariablesComputed)) FROM Pilot p WHERE p.pilotCode = :pilotCode") 
	Timestamp findNextMonthForPilot (@Param ("pilotCode") Pilot.PilotCode pilotCode);
	
	//upit koji vraca sve pilote za koje se radi proracun
	@Query("SELECT p FROM Pilot p WHERE p.newestSubmittedData IS NOT NULL AND p.latestConfigurationUpdate IS NOT NULL AND (p.latestVariablesComputed IS NULL OR (p.latestVariablesComputed IS NOT NULL AND DATE_TRUNC('mon', p.latestVariablesComputed) < DATE_TRUNC('mon', p.newestSubmittedData) AND DATE_TRUNC('mon', p.newestSubmittedData) < DATE_TRUNC('mon', CURRENT_TIMESTAMP)))")
	List<Pilot> findAllPilotsForComputation();
	
}
