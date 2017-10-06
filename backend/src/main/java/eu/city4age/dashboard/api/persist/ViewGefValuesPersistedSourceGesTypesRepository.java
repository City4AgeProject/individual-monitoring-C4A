package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;

@Repository(value = "viewGefValuesPersistedSourceGesTypesRepository")
@Transactional(readOnly = true)
public interface ViewGefValuesPersistedSourceGesTypesRepository extends GenericRepository<ViewGefValuesPersistedSourceGesTypes, Long> {

	@Query("SELECT vg FROM ViewGefValuesPersistedSourceGesTypes vg WHERE vg.id.userInRoleId = :uirId AND vg.intervalStart >= :startOfMonth AND vg.intervalStart <= :endOfMonth AND vg.typicalPeriod = 'MON' AND vg.derivedGefType = :derivedType")
	List<ViewGefValuesPersistedSourceGesTypes> findAllForMonthByUserId(@Param("uirId") Long uirId, @Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("derivedType") DetectionVariableType derivedType);

}
