package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewNuiValuesPersistedSourceMeaTypes;

@Repository(value = "viewNuiValuesPersistedSourceMeaTypesRepository")
@Transactional(readOnly = true)
public interface ViewNuiValuesPersistedSourceMeaTypesRepository extends GenericRepository<ViewNuiValuesPersistedSourceMeaTypes, Long> {
	
	@Query("SELECT vnv FROM ViewNuiValuesPersistedSourceMeaTypes vnv WHERE vnv.userInRoleId = :uirId AND vnv.derivedNuiId = :gesDvId AND vnv.pilotCode = :pilotCode AND vnv.intervalStart = :yearMonth AND vnv.typicalPeriod = 'MON'")
	List<ViewNuiValuesPersistedSourceMeaTypes> findNuiFor1Month(@Param("pilotCode") final String pilotCode, @Param("yearMonth") final Timestamp yearMonth, @Param("gesDvId") final Long gesDvId, @Param("uirId") final Long uirId);

}