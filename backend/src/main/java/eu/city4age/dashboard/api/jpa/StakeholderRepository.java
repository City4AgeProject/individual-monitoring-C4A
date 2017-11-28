package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Stakeholder;

@Repository(value = "stakeholderRepository")
public interface StakeholderRepository extends GenericRepository<Stakeholder, Long> {}