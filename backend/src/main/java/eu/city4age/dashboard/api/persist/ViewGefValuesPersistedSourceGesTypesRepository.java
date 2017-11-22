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
import eu.city4age.dashboard.api.pojo.dto.Gfvs;

@Repository(value = "viewGefValuesPersistedSourceGesTypesRepository")
@Transactional(readOnly = true)
public interface ViewGefValuesPersistedSourceGesTypesRepository extends GenericRepository<ViewGefValuesPersistedSourceGesTypes, Long> {

	@Query(nativeQuery = true)
	List<Gfvs> doAllGfvs(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("detectionVariableType") DetectionVariableType detectionVariableType);

}
