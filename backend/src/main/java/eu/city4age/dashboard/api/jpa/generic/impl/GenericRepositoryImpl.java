package eu.city4age.dashboard.api.jpa.generic.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import eu.city4age.dashboard.api.jpa.annotation.EntityFilter;
import eu.city4age.dashboard.api.jpa.annotation.FilterQuery;
import eu.city4age.dashboard.api.jpa.generic.GenericRepository;
import eu.city4age.dashboard.api.pojo.domain.AbstractBaseEntity;

/**
 * Generic jpa repository implementation.
 * 
 * @author milos.holclajtner
 */
@NoRepositoryBean
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
	@SuppressWarnings("unchecked")
	public List<T> doQueryWithFilter(List<eu.city4age.dashboard.api.pojo.persist.Filter> filters,
			String filterQueryName, Map<String, Object> inQueryParams) {

		if (GenericRepository.class.isAssignableFrom(getSpringDataRepositoryInterface())) {
			Annotation entityFilterAnn = getSpringDataRepositoryInterface().getAnnotation(EntityFilter.class);

			if (entityFilterAnn != null) {

				EntityFilter entityFilter = (EntityFilter) entityFilterAnn;
				FilterQuery[] filterQuerys = entityFilter.filterQueries();

				for (FilterQuery fQuery : filterQuerys) {

					if (StringUtils.equals(filterQueryName, fQuery.name())) {
						String jpql = fQuery.jpql();

						for (eu.city4age.dashboard.api.pojo.persist.Filter flt : filters) {
							Filter filter = entityManager.unwrap(Session.class).enableFilter(flt.getName());

							// Set filter parameter
							for (Object key : flt.getInParams().keySet()) {

								// FilterParam map key must be filter name
								if (flt.getName().equals(key.toString())) {

									String filterParamName = key.toString();

									if (flt.getInParams().get(key) instanceof List) {
										
										List<T> filterParamValue = (List<T>) flt.getInParams().get(key);
										filter.setParameterList(filterParamName, filterParamValue);
									} else {
										Object filterParamValue = flt.getInParams().get(key);
										filter.setParameter(filterParamName, filterParamValue);
									}

								}

							}

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
	
	@Override
	@SuppressWarnings("unchecked")
	public Object[] doQueryWithFilterAggr(List<eu.city4age.dashboard.api.pojo.persist.Filter> filters,
			String filterQueryName, Map<String, Object> inQueryParams) {

		if (GenericRepository.class.isAssignableFrom(getSpringDataRepositoryInterface())) {
			Annotation entityFilterAnn = getSpringDataRepositoryInterface().getAnnotation(EntityFilter.class);

			if (entityFilterAnn != null) {

				EntityFilter entityFilter = (EntityFilter) entityFilterAnn;
				FilterQuery[] filterQuerys = entityFilter.filterQueries();

				for (FilterQuery fQuery : filterQuerys) {

					if (StringUtils.equals(filterQueryName, fQuery.name())) {
						String jpql = fQuery.jpql();

						for (eu.city4age.dashboard.api.pojo.persist.Filter flt : filters) {
							Filter filter = entityManager.unwrap(Session.class).enableFilter(flt.getName());

							// Set filter parameter
							for (Object key : flt.getInParams().keySet()) {

								// FilterParam map key must be filter name
								if (flt.getName().equals(key.toString())) {

									String filterParamName = key.toString();

									if (flt.getInParams().get(key) instanceof List) {
										
										List<T> filterParamValue = (List<T>) flt.getInParams().get(key);
										filter.setParameterList(filterParamName, filterParamValue);
									} else {
										Object filterParamValue = flt.getInParams().get(key);
										filter.setParameter(filterParamName, filterParamValue);
									}

								}

							}

						}

						// Set query parameter
						Query query = entityManager.createQuery(jpql);
						for (Object key : inQueryParams.keySet()) {
							String queryParamName = key.toString();
							Object queryParamValue = inQueryParams.get(key);
							query.setParameter(queryParamName, queryParamValue);
						}

						return (Object[]) query.getSingleResult();

					}
				}
			}
		}

		return null;

	}

	public void disableFilter(String name) {
		entityManager.unwrap(Session.class).disableFilter(name);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	
	public <S extends T> S merge(S entity) {
		return this.entityManager.merge(entity);
	}
	
	public <S extends AbstractBaseEntity<?>> Collection<S> bulkSave(Collection<S> entities) {
		//int i = 0;
		for (S s : entities) {
			persistOrMerge(s);
			//i++;
			/*if (i % batchSize == 0) {
				// Flush a batch of inserts and release memory
				entityManager.flush();
				entityManager.clear();
			}*/
		}
		/*entityManager.flush();
		entityManager.clear();*/
		return entities;
	}

	private <S extends AbstractBaseEntity<?>> S persistOrMerge(S entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}


}