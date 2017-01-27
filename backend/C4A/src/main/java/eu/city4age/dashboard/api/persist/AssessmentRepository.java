package eu.city4age.dashboard.api.persist;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.city4age.dashboard.api.persist.annotation.EntityFilter;
import eu.city4age.dashboard.api.persist.annotation.FilterQuery;
import eu.city4age.dashboard.api.persist.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.Assessment;

@Repository(value = "assessmentRepository")
@Transactional(readOnly = true)
@EntityFilter(filterQueries = {
		@FilterQuery(name = "findForSelectedDataSet", jpql = "SELECT aa FROM Assessment aa JOIN aa.geriatricFactorValue AS gfv JOIN aa.roles AS aar LEFT JOIN FETCH aa.userInRole AS uir LEFT JOIN FETCH uir.userInSystem AS uis WHERE gfv.id IN :geriatricFactorIds") })
public interface AssessmentRepository extends GenericRepository<Assessment, Long> {
}