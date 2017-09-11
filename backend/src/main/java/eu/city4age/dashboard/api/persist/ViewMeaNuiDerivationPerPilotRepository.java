package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;

@Repository(value = "viewMeaNuiDerivationPerPilotRepository")
@Transactional(readOnly = true)
public interface ViewMeaNuiDerivationPerPilotRepository extends GenericRepository<ViewMeaNuiDerivationPerPilot, Long> {

	@Query("SELECT vmn FROM ViewMeaNuiDerivationPerPilot vmn WHERE vmn.derivedNuiId = :nuiId AND vmn.pilotCode = :pilotCode")
	ViewMeaNuiDerivationPerPilot findByDerivedDetectionVariableAndPilotCode(@Param("nuiId") Long nuiId,
			@Param("pilotCode") String pilotCode);

	@Query("SELECT vmn FROM ViewMeaNuiDerivationPerPilot vmn WHERE vmn.meaId = :meaId  AND vmn.pilotCode = :pilotCode")
	List<ViewMeaNuiDerivationPerPilot> findAllNuiForMea(@Param("meaId") Long meaId, @Param("pilotCode") String pilotCode);

}