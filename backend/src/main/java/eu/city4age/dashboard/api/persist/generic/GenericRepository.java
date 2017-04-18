package eu.city4age.dashboard.api.persist.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Generic jpa repository.
 * 
 * @author milos.holclajtner
 */

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable>
		extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	<S extends T> S save(S entity);

	T saveWithoutFlush(T entity);

	List<? extends T> saveWithoutFlush(Iterable<? extends T> entities);

	List<T> doQueryWithFilter(List<eu.city4age.dashboard.api.pojo.persist.Filter> flts, String filterQueryName, Map<String, Object> inQueryParams);

	void disableFilter(String name);

}