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
	List<Object[]> computeAllNuis(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("pilotCode") String pilotCode);
	
	@Query(nativeQuery = true)
	List<Object[]> computeAllGess(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("pilotCode") String pilotCode);

	@Query(nativeQuery = true)
	List<Object[]> computeAllGfvs(@Param("startOfMonth") Timestamp startOfMonth, @Param("endOfMonth") Timestamp endOfMonth, @Param("detectionVariableType") DetectionVariableType detectionVariableType, @Param("pilotCode") String pilotCode);

	@Query(nativeQuery = true)
	List<Object[]> getLast5AssessmentsForDiagramTimeline(@Param("userInRoleId") final Long userInRoleId, @Param("parentDetectionVariableId") final Long parentDetectionVariableId,
			@Param("intervalStart") final Timestamp intervalStart, @Param("intervalEnd") final Timestamp intervalEnd);

	@Query(nativeQuery = true)
	List<Object[]> getJointGefValues(@Param("factorId") final Long factorId, @Param("userInRoleId") final Long userInRoleId);
	
}
