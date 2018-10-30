package eu.city4age.dashboard.api.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValuesKey;

@Repository(value = "viewGefCalculatedInterpolatedPredictedValuesRepository")
public interface ViewGefCalculatedInterpolatedPredictedValuesRepository extends GenericRepository<ViewGefCalculatedInterpolatedPredictedValues, Long> {

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.id.dataType = :dataType")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDataType(@Param("dataType") String dataType);


	//For calculating interpolated values, previous interpolation is used
	//Vladimir Aleksic

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.detectionVariableId = :varId AND gef.userInRoleId = :userInRoleId AND  gef.id.dataType<>'p' ORDER BY gef.intervalStart ASC")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDetectionVariableIdNoPredicted(@Param("varId") final Long dvId, @Param("userInRoleId") final Long uId); 

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.detectionVariableId = :varId AND gef.userInRoleId = :userInRoleId ORDER BY gef.intervalStart ASC")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDetectionVariableId(@Param("varId") final Long dvId, @Param("userInRoleId") final Long uId); //AND gef.id.dataType<>'p' 

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.userInRoleId = :careRecipientId AND gef.derivedDetectionVariableId IN :parentFactorIds ORDER BY gef.intervalStart")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByCareRecipientIdAndParentFactorIds(@Param("careRecipientId") final Long careRecipientId, @Param("parentFactorIds") final List<Long> parentFactorIds);
	
	@Query("SELECT DISTINCT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.userInRoleId = :careRecipientId AND (gef.derivedDetectionVariableId = 501 OR gef.derivedDetectionVariableId IS NULL) ORDER BY gef.intervalStart")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByCareRecipientId(@Param("careRecipientId") final Long careRecipientId);

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.userInRoleId = (SELECT gef1.userInRoleId FROM ViewGefCalculatedInterpolatedPredictedValues gef1 WHERE gef1.id = :key) AND gef.detectionVariableId = (SELECT gef2.detectionVariableId FROM ViewGefCalculatedInterpolatedPredictedValues gef2 WHERE gef2.id = :key) AND gef.intervalStart <= (SELECT gef3.intervalStart FROM ViewGefCalculatedInterpolatedPredictedValues gef3 WHERE gef3.id = :key) ORDER BY gef.intervalStart")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByKey(@Param("key") final ViewGefCalculatedInterpolatedPredictedValuesKey key);
	
	
	// correlation coefficient
	@Query("SELECT gfv.gefValue FROM ViewGefCalculatedInterpolatedPredictedValues gfv WHERE gfv.userInRoleId = :uirId AND gfv.detectionVariableId = :dvId AND gfv.id.dataType <> 'p' AND gfv.intervalStart = :intervalStart")
	BigDecimal findByUserInRoleIdAndDetectionVariableIdForOneMonth(@Param("uirId") Long uirId, @Param("dvId") Long dvId, @Param("intervalStart") Date intervalStart);
	
	@Query("SELECT DISTINCT gfv.intervalStart FROM ViewGefCalculatedInterpolatedPredictedValues gfv WHERE gfv.userInRoleId = :uirId AND gfv.detectionVariableId = :dvId AND gfv.intervalStart >= :intervalStart AND gfv.intervalStart <= :intervalEnd AND gfv.id.dataType <> 'p'")
	List<Date> findDatesForUserInRoleIdAndDetectionVariableIdForInterval(@Param("uirId") Long uirId, @Param("dvId") Long dvId, @Param("intervalStart") Date intervalStart, @Param("intervalEnd") Date intervalEnd);
	
}