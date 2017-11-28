package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewNuiValuesPersistedSourceMeaTypes;

@Repository(value = "viewNuiValuesPersistedSourceMeaTypesRepository")
public interface ViewNuiValuesPersistedSourceMeaTypesRepository extends GenericRepository<ViewNuiValuesPersistedSourceMeaTypes, Long> {
	
	@Query("SELECT vnv FROM ViewNuiValuesPersistedSourceMeaTypes vnv WHERE vnv.userInRoleId = :uirId AND vnv.derivedNuiId = :gesDvId AND vnv.intervalStart = :yearMonth")
	ViewNuiValuesPersistedSourceMeaTypes findNuiFor1Month(@Param("yearMonth") final Timestamp yearMonth, @Param("gesDvId") final Long gesDvId, @Param("uirId") final Long uirId);

}