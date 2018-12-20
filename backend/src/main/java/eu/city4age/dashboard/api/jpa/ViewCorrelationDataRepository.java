package eu.city4age.dashboard.api.jpa;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewCorrelationData;
import eu.city4age.dashboard.api.pojo.domain.ViewCorrelationDataKey;

public interface ViewCorrelationDataRepository extends GenericRepository<ViewCorrelationData, ViewCorrelationDataKey> {
	
	@Query ("SELECT vcd FROM ViewCorrelationData vcd WHERE vcd.userInRoleId = :uirId AND vcd.detectionVariableId = :dvId AND vcd.intervalStart >= :intervalStart AND vcd.intervalStart <= :intervalEnd")
	List<ViewCorrelationData> findCorrelationDataForUserAndVariableAndInterval (@Param("uirId") Long uirId, @Param("dvId") Long dvId, @Param("intervalStart") Timestamp intervalStart, @Param("intervalEnd") Timestamp intervalEnd);

}
