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
}
