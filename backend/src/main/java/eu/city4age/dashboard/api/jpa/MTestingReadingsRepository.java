package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;

@Repository(value = "mTestingReadingsRepository")
public interface MTestingReadingsRepository extends GenericRepository<MTestingReadings, Long> {

}
