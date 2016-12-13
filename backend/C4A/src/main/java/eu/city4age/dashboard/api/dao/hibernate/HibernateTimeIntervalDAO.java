package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.TimeIntervalDAO;

public class HibernateTimeIntervalDAO extends HibernateBaseDAO implements TimeIntervalDAO {

	@Autowired
	protected SessionFactory sessionFactory;
	
	@Override
	public List<Object[]> getTimeIntervalsForPeriod(final Timestamp start, final Timestamp end) {
		return castList(Object[].class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT ti.intervalStart AS intervalStart, ti.intervalEnd AS intervalEnd FROM TimeInterval AS ti WHERE ti.intervalStart >= :start AND ti.intervalEnd <= :end ");
				q.setParameter("start", start);
				q.setParameter("end", end);
				return q.list();
			}
		}));
	}
	
}
