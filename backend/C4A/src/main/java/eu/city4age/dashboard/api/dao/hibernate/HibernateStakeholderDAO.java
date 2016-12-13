package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.StakeholderDao;
import eu.city4age.dashboard.api.model.Stakeholder;

public class HibernateStakeholderDAO extends HibernateBaseDAO implements StakeholderDao {
	
	@Autowired
	protected SessionFactory sessionFactory;

	public List<Stakeholder> getAllStockholders() {
        return castList(Stakeholder.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT s FROM Stakeholder AS s");
				return q.list();
			}
		}));
	}

}
