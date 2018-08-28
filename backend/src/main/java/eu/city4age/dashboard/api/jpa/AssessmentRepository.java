package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.annotation.EntityFilter;
import eu.city4age.dashboard.api.jpa.annotation.FilterQuery;
import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;

@Repository(value = "assessmentRepository")
@EntityFilter(filterQueries = {
		@FilterQuery(name = "findForSelectedDataSet", 
				jpql = "SELECT aa FROM Assessment aa JOIN aa.geriatricFactorValue AS gfv JOIN aa.roles AS aar LEFT JOIN FETCH aa.userInRole AS uir LEFT JOIN FETCH uir.userInSystem AS uis WHERE gfv.id IN :geriatricFactorIds"),
		@FilterQuery(name = "findClusterForSelectedDataSet", 
				jpql = "SELECT DISTINCT a FROM Assessment a INNER JOIN a.vmvFiltering vmvFilter INNER JOIN vmvFilter.vmv vmv WHERE vmv.id IN :dataPointsIds ORDER BY a.created DESC")})
public interface AssessmentRepository extends GenericRepository<Assessment, Long> {}