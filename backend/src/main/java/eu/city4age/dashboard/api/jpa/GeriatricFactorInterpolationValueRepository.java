package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorInterpolationValue;

@Repository(value = "geriatricFactorInterpolationValueRepository")
public interface GeriatricFactorInterpolationValueRepository extends GenericRepository<GeriatricFactorInterpolationValue, Long> {
	
	
	
	
}
