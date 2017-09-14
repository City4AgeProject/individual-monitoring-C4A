package eu.city4age.dashboard.api.persist;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewPilotDetectionVariable;

@Repository(value = "viewPilotDetectionVariableRepository")
@Transactional(readOnly = true)
public interface ViewPilotDetectionVariableRepository extends GenericRepository<ViewPilotDetectionVariable, Long> {
	
	@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.pilotCode = :pilotCode AND vpdv.id.userInRoleId = :userId AND vpdv.detectionVariableType = 'MEA' AND vpdv.derivedDetectionVariableType = 'GES'")
	List<ViewPilotDetectionVariable> findAllMeaGes(@Param("pilotCode") final String pilotCode, @Param("userId") final Long userId);

	/*@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv WHERE vpdv.id.pilotCode = :pilotCode AND vpdv.detectionVariableType = 'MEA' AND vpdv.derivedDetectionVariableName = vpdv.formula || CAST('_' AS string) || :detectionVariableName AND vpdv.derivedDetectionVariableType = 'NUI'")
	List<ViewPilotDetectionVariable> findAllDvNuisForMeasure(@Param("detectionVariableName") String detectionVariableName, @Param("pilotCode") String pilotCode);*/
	
	/*@Query("SELECT vpdv FROM ViewPilotDetectionVariable vpdv 
	 * WHERE vpdv.mpdvId = :meaId 
	 * AND vpdv.derivedDetectionVariableType = 'NUI'")
	List<ViewPilotDetectionVariable> findAllNuiForMea(@Param("meaId") Long meaId);*/

}