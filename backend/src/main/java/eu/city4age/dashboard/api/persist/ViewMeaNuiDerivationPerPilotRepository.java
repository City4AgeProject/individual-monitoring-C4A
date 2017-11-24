package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;

@Repository(value = "viewMeaNuiDerivationPerPilotRepository")
public interface ViewMeaNuiDerivationPerPilotRepository extends GenericRepository<ViewMeaNuiDerivationPerPilot, Long> {}