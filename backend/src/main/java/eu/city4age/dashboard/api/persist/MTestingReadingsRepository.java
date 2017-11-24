package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.MTestingReadings;

@Repository(value = "mTestingReadingsRepository")
public interface MTestingReadingsRepository extends GenericRepository<MTestingReadings, Long> {

}
