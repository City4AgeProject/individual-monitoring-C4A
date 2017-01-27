package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;

@Repository(value = "riskStatusRepository")
@Transactional(readOnly = true)
public interface RiskStatusRepository extends GenericRepository<RiskStatus, Long> {

	List<RiskStatus> findAll();

}