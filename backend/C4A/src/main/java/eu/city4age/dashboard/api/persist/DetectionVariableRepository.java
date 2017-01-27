package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;

@Repository(value = "detectionVariableRepository")
@Transactional(readOnly = true)
public interface DetectionVariableRepository extends GenericRepository<DetectionVariable, Long> {

	@Query("SELECT dv.detectionVariableName FROM DetectionVariable AS dv WHERE dv.derivedDetectionVariable.id = :parentId")
	List<String> findNameByParentId(@Param("parentId") final Long parentId);

	@Query("SELECT c FROM DetectionVariable c WHERE c.detectionVariableType.detectionVariableType IN :gefType")
	List<DetectionVariable> findByDetectionVariableTypes(
			@Param("gefType") final List<String> parentFactors);

}