package eu.city4age.dashboard.api.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import eu.city4age.dashboard.api.dao.BaseDAO;
import eu.city4age.dashboard.api.model.AbstractBaseEntity;


/** Hibernate implementacija za osnovni dao.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public class HibernateBaseDAO extends HibernateDaoSupport implements BaseDAO {

    public AbstractBaseEntity insert(AbstractBaseEntity entity) {
        getHibernateTemplate().save(entity);
        return entity;
    }

    public void update(AbstractBaseEntity entity) {
        getHibernateTemplate().update(entity);
    }

    public AbstractBaseEntity insertOrUpdate(AbstractBaseEntity entity) {
        getHibernateTemplate().saveOrUpdate(entity);
        return entity;
    }

    public AbstractBaseEntity getById(Class<? extends AbstractBaseEntity> clazz, Serializable id) {
        return (AbstractBaseEntity) getHibernateTemplate().get(clazz, id);
    }

    public List<? extends AbstractBaseEntity> getAll(Class<? extends AbstractBaseEntity> clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }
    
	public <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
	    List<T> r = new ArrayList<T>(c.size());
	    for(Object o: c)
	      r.add(clazz.cast(o));
	    return r;
	}

	public void delete(AbstractBaseEntity entity) {
		getHibernateTemplate().delete(entity);
	}
	
	public void insertOrUpdateAll(List<AbstractBaseEntity> entities) {
		for (Iterator<AbstractBaseEntity> it = entities.iterator(); it.hasNext();) {
			getHibernateTemplate().saveOrUpdate(it.next());
		}
	}
	
}
