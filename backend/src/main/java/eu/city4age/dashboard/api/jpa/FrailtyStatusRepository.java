package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.FrailtyStatus;

@Repository(value = "frailtyStatusRepository")
public interface FrailtyStatusRepository extends GenericRepository<FrailtyStatus, Long> {}