package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;

@Repository(value = "vmvFilteringRepository")
public interface VmvFilteringRepository extends GenericRepository<VmvFiltering, Long> {}