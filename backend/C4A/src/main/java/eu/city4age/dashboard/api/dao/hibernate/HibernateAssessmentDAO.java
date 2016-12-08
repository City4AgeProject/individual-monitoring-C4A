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

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.model.Assessment;

/** Hibernate implementacija dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public class HibernateAssessmentDAO extends HibernateBaseDAO implements AssessmentDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;

    public List<Object[]> getDiagramDataForUserInRoleId(final Integer userInRoleId, final Timestamp start, final Timestamp end) {
		return castList(Object[].class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT g, g.timeInterval.intervalStart, g.cdDetectionVariable.id FROM GeriatricFactorValue g LEFT JOIN g.timeInterval AS timeInterval LEFT JOIN g.cdDetectionVariable AS cdDetectionVariable WHERE g.userInRoleId = :userInRoleId AND g.timeInterval.intervalStart >= :start AND g.timeInterval.intervalEnd <= :end ORDER BY g.timeInterval.intervalStart ASC, g.cdDetectionVariable.id ASC");
				q.setParameter("userInRoleId", userInRoleId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				return q.list();
			}
		}));	
    }

	public List<Assessment> getAssessmentsForGeriatricFactorId(final Long geriatricFactorId) {
		return castList(Assessment.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT a FROM Assessment a LEFT JOIN FETCH a.userInRole AS userInRole INNER JOIN a.assessedGefValueSets AS assessedGefValueSets INNER JOIN assessedGefValueSets.geriatricFactorValue AS geriatricFactorValue INNER JOIN a.assessmentAudienceRoles AS assessmentAudienceRoles WHERE geriatricFactorValue.id = :geriatricFactorId ");
				q.setParameter("geriatricFactorId", geriatricFactorId);
				return q.list();
			}
		}));
	}

	public List<Assessment> getAssessmentsByFilter(final Long geriatricFactorId, final String filter) {
		return castList(Assessment.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT a FROM Assessment a INNER JOIN a.assessedGefValueSets AS assessedGefValueSets INNER JOIN assessedGefValueSets.geriatricFactorValue AS geriatricFactorValue WHERE geriatricFactorValue.id = :geriatricFactorId ");
				q.setParameter("geriatricFactorId", geriatricFactorId);
				return q.list();
			}
		}));
	}

	public List<Assessment> getLastFiveAssessments() {
		return null;
	}

}
