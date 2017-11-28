package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.NativeQuery;

public interface NativeQueryRepository extends GenericRepository<NativeQuery, Long> {
	
	@Query(nativeQuery = true)
	List<Object[]> doAllNuis(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth);
	
	@Query(nativeQuery = true)
	List<Object[]> doAllGess(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth);

	@Query(nativeQuery = true)
	List<Object[]> doAllGfvs(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("detectionVariableType") DetectionVariableType detectionVariableType);

}
