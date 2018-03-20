package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorPredictionValue;

@Repository(value = "geriatricFactorPredictionValueRepository")
public interface GeriatricFactorPredictionValueRepository extends GenericRepository<GeriatricFactorPredictionValue, Long> {

}
