package eu.city4age.dashboard.api.jpa;

import org.springframework.stereotype.Repository;

import eu.city4age.dashboard.api.jpa.annotation.EntityFilter;
import eu.city4age.dashboard.api.jpa.annotation.FilterQuery;
import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.ViewGroupAnalyticsData;
import eu.city4age.dashboard.api.pojo.domain.ViewGroupAnalyticsDataKey;

@Repository(value = "viewGroupAnalyticsDataRepository")
@EntityFilter(filterQueries = {
		@FilterQuery(name = "grAn",
					jpql = "SELECT avg (vwgad.value), COUNT (DISTINCT vwgad.userInSystemId) FROM ViewGroupAnalyticsData vwgad")
})
public interface ViewGroupAnalyticsDataRepository extends GenericRepository<ViewGroupAnalyticsData, ViewGroupAnalyticsDataKey> {}