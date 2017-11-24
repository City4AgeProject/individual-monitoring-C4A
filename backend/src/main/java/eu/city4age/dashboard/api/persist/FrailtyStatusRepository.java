package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;

@Repository(value = "frailtyStatusRepository")
public interface FrailtyStatusRepository extends GenericRepository<FrailtyStatus, Long> {}