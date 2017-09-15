package eu.city4age.dashboard.api.persist;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGefValuesPersistedSourceGesTypes;

@Repository(value = "viewGefValuesPersistedSourceGesTypesRepository")
@Transactional(readOnly = true)
public interface ViewGefValuesPersistedSourceGesTypesRepository extends GenericRepository<ViewGefValuesPersistedSourceGesTypes, Long> {

	@Query("SELECT vg FROM ViewGefValuesPersistedSourceGesTypes vg WHERE vg.id.pilotCode = :pilotCode AND vg.intervalStart >= :startOfMonth AND vg.typicalPeriod = 'MON' AND vg.derivedGefType = :derivedType")
	List<ViewGefValuesPersistedSourceGesTypes> findAllForMonthByPilotCode(@Param("pilotCode") String pilotCode, @Param("startOfMonth") Timestamp startOfMonth, @Param("derivedType") String derivedType);

}
