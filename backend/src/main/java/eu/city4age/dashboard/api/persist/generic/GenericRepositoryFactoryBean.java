package eu.city4age.dashboard.api.persist.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class GenericRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {

	// needed for spring-boot 1.5.0 and up
	/*public GenericRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}*/

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new GenericRepositoryFactory(entityManager);
	}
}