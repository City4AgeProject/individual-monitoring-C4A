package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.RiskStatusDAO;
import eu.city4age.dashboard.api.model.CdRiskStatus;

public class HibernateRiskStatusDAO extends HibernateBaseDAO implements RiskStatusDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;

	public List<CdRiskStatus> getAllRiskStatus() {
        return castList(CdRiskStatus.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT rs FROM CdRiskStatus AS rs");
				return q.list();
			}
		}));
	}

}
