package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewMeaNuiDerivationPerPilot;

@Repository(value = "viewMeaNuiDerivationPerPilotRepository")
public interface ViewMeaNuiDerivationPerPilotRepository extends GenericRepository<ViewMeaNuiDerivationPerPilot, Long> {}