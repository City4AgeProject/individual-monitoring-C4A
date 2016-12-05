package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.model.Assessment;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

/** Hibernate implementacija dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public class HibernateAssessmentDAO extends HibernateBaseDAO implements AssessmentDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;

    public List<GeriatricFactorValue> getDiagramDataForUserInRoleId(final Integer userInRoleId, final Timestamp start, final Timestamp end) {
		return castList(GeriatricFactorValue.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT g FROM GeriatricFactorValue g LEFT JOIN g.timeInterval AS timeInterval LEFT JOIN g.cdDetectionVariable AS cdDetectionVariable WHERE g.userInRoleId = :userInRoleId AND g.timeInterval.intervalStart >= :start AND g.timeInterval.intervalEnd <= :end ORDER BY g.timeInterval.intervalStart ASC, g.cdDetectionVariable.id ASC");
				q.setParameter("userInRoleId", userInRoleId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				return q.list();
			}
		}));	
    }
    
    public List<TimeInterval> getDiagramDataForUserInRoleId2(final Integer userInRoleId, final Timestamp start, final Timestamp end) {
		return castList(TimeInterval.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT distinct t.id AS id, t.intervalStart AS intervalStart, t.intervalEnd AS intervalEnd FROM TimeInterval t LEFT JOIN t.geriatricFactorValues AS geriatricFactorValues LEFT JOIN geriatricFactorValues.cdDetectionVariable AS cdDetectionVariable WHERE geriatricFactorValues.userInRoleId = :userInRoleId AND t.intervalStart >= :start AND t.intervalEnd <= :end ORDER BY t.intervalStart ASC");
				q.setParameter("userInRoleId", userInRoleId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setResultTransformer(Transformers.aliasToBean(TimeInterval.class));
				return q.list();
			}
		}));	
    }
    
    public List<TimeInterval> getDiagramDataForUserInRoleId(final Integer userInRoleId) {
		return castList(TimeInterval.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT distinct t.id AS id, t.intervalStart AS intervalStart, t.intervalEnd AS intervalEnd FROM TimeInterval t LEFT JOIN t.geriatricFactorValues AS geriatricFactorValues WHERE geriatricFactorValues.userInRoleId = :userInRoleId ORDER BY t.intervalStart ASC");
				q.setParameter("userInRoleId", userInRoleId);
				q.setResultTransformer(Transformers.aliasToBean(TimeInterval.class));
				return q.list();
			}
		}));	
    }

	public List<Assessment> getAssessmentsForGeriatricFactorId(final Long geriatricFactorId) {
		return castList(Assessment.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT a FROM Assessment a LEFT JOIN a.assessedSets AS assessedSets LEFT JOIN assessedSets.geriatricFactorValue AS geriatricFactorValue WHERE geriatricFactorValue.id = :geriatricFactorId ");
				q.setParameter("geriatricFactorId", geriatricFactorId);
				return q.list();
			}
		}));
	}

	@Override
	public List<Assessment> getAssessmentsByFilter(final Long geriatricFactorId, final String filter) {
		return castList(Assessment.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT a FROM Assessment a LEFT JOIN a.assessedSets AS assessedSets LEFT JOIN assessedSets.geriatricFactorValue AS geriatricFactorValue WHERE geriatricFactorValue.id = :geriatricFactorId ");
				q.setParameter("geriatricFactorId", geriatricFactorId);
				return q.list();
			}
		}));
	}

}
