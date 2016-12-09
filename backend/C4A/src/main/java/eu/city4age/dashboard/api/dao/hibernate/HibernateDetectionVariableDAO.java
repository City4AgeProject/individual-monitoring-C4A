package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.DetectionVariableDAO;

public class HibernateDetectionVariableDAO extends HibernateBaseDAO implements DetectionVariableDAO {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public List<String> getAllDetectionVariableNamesForParentId(final Short parentId) {
		return castList(String.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT dv.detectionVariableName FROM CdDetectionVariable AS dv WHERE dv.parentId = :parentId");
				q.setParameter("parentId", parentId);
				return q.list();
			}
		}));
	}
	
}
