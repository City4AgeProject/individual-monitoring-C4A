package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;

@Repository(value = "frailtyStatusRepository")
@Transactional(readOnly = true)
public interface FrailtyStatusRepository extends GenericRepository<FrailtyStatus, Long> {}