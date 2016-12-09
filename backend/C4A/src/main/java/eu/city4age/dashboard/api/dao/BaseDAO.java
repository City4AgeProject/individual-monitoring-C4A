package eu.city4age.dashboard.api.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import eu.city4age.dashboard.api.model.AbstractBaseEntity;

/** Javni API za osnovni dao.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public interface BaseDAO {

    AbstractBaseEntity insert(AbstractBaseEntity entity);

    void update(AbstractBaseEntity entity);

    AbstractBaseEntity insertOrUpdate(AbstractBaseEntity entity);

    AbstractBaseEntity getById(Class<? extends AbstractBaseEntity> clazz, Serializable id);

    List<? extends AbstractBaseEntity> getAll(Class<? extends AbstractBaseEntity> clazz);
    
    <T> List<T> castList(Class<? extends T> clazz, Collection<?> c);
    
    void delete(AbstractBaseEntity entity);
    
    void insertOrUpdateAll(List<AbstractBaseEntity> entities);

}
