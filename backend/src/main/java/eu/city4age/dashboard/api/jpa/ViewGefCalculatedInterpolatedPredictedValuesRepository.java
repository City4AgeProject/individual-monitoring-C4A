package eu.city4age.dashboard.api.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGefCalculatedInterpolatedPredictedValues;

@Repository(value = "viewGefCalculatedInterpolatedPredictedValuesRepository")
public interface ViewGefCalculatedInterpolatedPredictedValuesRepository extends GenericRepository<ViewGefCalculatedInterpolatedPredictedValues, Long> {

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.id.dataType = :dataType")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDataType(@Param("dataType") String dataType);


	//For calculating interpolated values, previous interpolation is used
	//Vladimir Aleksic

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.id.detectionVariableId = :varId AND gef.id.userInRoleId = :userInRoleId AND  gef.id.dataType<>'p' ORDER BY gef.intervalStart ASC")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDetectionVariableIdNoPredicted(@Param("varId") final Long dvId, @Param("userInRoleId") final Long uId); 

	@Query("SELECT gef FROM ViewGefCalculatedInterpolatedPredictedValues gef WHERE gef.id.detectionVariableId = :varId AND gef.id.userInRoleId = :userInRoleId ORDER BY gef.intervalStart ASC")
	List<ViewGefCalculatedInterpolatedPredictedValues> findByDetectionVariableId(@Param("varId") final Long dvId, @Param("userInRoleId") final Long uId); //AND gef.id.dataType<>'p' 

}
