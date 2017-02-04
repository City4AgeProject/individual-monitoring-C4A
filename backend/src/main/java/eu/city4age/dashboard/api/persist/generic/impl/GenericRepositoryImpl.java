package eu.city4age.dashboard.api.persist.generic.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import eu.city4age.dashboard.api.persist.annotation.EntityFilter;
import eu.city4age.dashboard.api.persist.annotation.FilterQuery;
import eu.city4age.dashboard.api.persist.generic.GenericRepository;

/**
 * Generic jpa repository implementation.
 * 
 * @author milos.holclajtner
 */
public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements GenericRepository<T, ID> {

	private final JpaEntityInformation<T, ID> entityInformation;

	private final EntityManager entityManager;

	private Class<?> springDataRepositoryInterface;

	public Class<?> getSpringDataRepositoryInterface() {
		return springDataRepositoryInterface;
	}

	public void setSpringDataRepositoryInterface(Class<?> springDataRepositoryInterface) {
		this.springDataRepositoryInterface = springDataRepositoryInterface;
	}

	public GenericRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager,
			Class<?> springDataRepositoryInterface) {
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.entityManager = entityManager;
		this.springDataRepositoryInterface = springDataRepositoryInterface;
	}

	@Override
	public <S extends T> S save(S entity) {
		if (this.entityInformation.isNew(entity)) {
			this.entityManager.persist(entity);
			flush();
			return entity;
		}
		entity = this.entityManager.merge(entity);
		flush();
		return entity;
	}

	@Override
	public T saveWithoutFlush(T entity) {
		return super.save(entity);
	}

	@Override
	public List<? extends T> saveWithoutFlush(Iterable<? extends T> entities) {
		List<T> result = new ArrayList<T>();
		if (entities == null) {
			return result;
		}

		for (T entity : entities) {
			result.add(saveWithoutFlush(entity));
		}
		return result;
	}

	@Override
	public List<T> doQueryWithFilter(String filterName, String filterQueryName, Map<String, Object> inFilterParams,
			Map<String, Object> inQueryParams) {

		if (GenericRepository.class.isAssignableFrom(getSpringDataRepositoryInterface())) {
			Annotation entityFilterAnn = getSpringDataRepositoryInterface().getAnnotation(EntityFilter.class);

			if (entityFilterAnn != null) {

				EntityFilter entityFilter = (EntityFilter) entityFilterAnn;
				FilterQuery[] filterQuerys = entityFilter.filterQueries();

				for (FilterQuery fQuery : filterQuerys) {

					if (StringUtils.equals(filterQueryName, fQuery.name())) {
						String jpql = fQuery.jpql();
						Filter filter = entityManager.unwrap(Session.class).enableFilter(filterName);

						// Set filter parameter
						for (Object key : inFilterParams.keySet()) {
							String filterParamName = key.toString();
							Object filterParamValue = inFilterParams.get(key);
							filter.setParameter(filterParamName, filterParamValue);
						}

						// Set query parameter
						Query query = entityManager.createQuery(jpql);
						for (Object key : inQueryParams.keySet()) {
							String queryParamName = key.toString();
							Object queryParamValue = inQueryParams.get(key);
							query.setParameter(queryParamName, queryParamValue);
						}

						return query.getResultList();

					}
				}
			}
		}

		return null;

	}

}