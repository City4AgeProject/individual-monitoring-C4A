package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;

@Repository(value = "riskStatusRepository")
public interface RiskStatusRepository extends GenericRepository<RiskStatus, Long> {}