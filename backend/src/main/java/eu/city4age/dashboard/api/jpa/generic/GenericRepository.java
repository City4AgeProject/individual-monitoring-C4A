package eu.city4age.dashboard.api.jpa.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import eu.city4age.dashboard.api.pojo.domain.AbstractBaseEntity;
import eu.city4age.dashboard.api.pojo.persist.Filter;

/**
 * Generic jpa repository.
 * 
 * @author milos.holclajtner
 */

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable>
		extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	List<T> doQueryWithFilter(List<Filter> flts, String filterQueryName, Map<String, Object> inQueryParams);

	void disableFilter(String name);
	
	<S extends AbstractBaseEntity<?>> Collection<S> bulkSave(Collection<S> entities);
	
	<S extends AbstractBaseEntity<?>> S persistOrMerge(S s);

}