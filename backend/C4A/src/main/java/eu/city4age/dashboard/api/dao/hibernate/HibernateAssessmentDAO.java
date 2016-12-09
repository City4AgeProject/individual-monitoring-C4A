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
import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.model.Assessment;

/** Hibernate implementacija dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public class HibernateAssessmentDAO extends HibernateBaseDAO implements AssessmentDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;

    public List<Object[]> getDiagramDataForUserInRoleId(final Integer patientId, final Timestamp start, final Timestamp end) {
		return castList(Object[].class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT g, g.timeInterval.intervalStart, g.cdDetectionVariable.id FROM GeriatricFactorValue g LEFT JOIN g.timeInterval AS timeInterval LEFT JOIN g.cdDetectionVariable AS cdDetectionVariable WHERE g.userInRoleId = :userInRoleId AND g.timeInterval.intervalStart >= :start AND g.timeInterval.intervalEnd <= :end ORDER BY g.timeInterval.intervalStart ASC, g.cdDetectionVariable.id ASC");
				q.setParameter("userInRoleId", patientId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				return q.list();
			}
		}));	
    }
    
	public List<Object[]> getLastFiveAssessmentsForDiagram(final Integer patientId, final Timestamp start, final Timestamp end) {
		return castList(Object[].class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT a, a.created FROM Assessment a LEFT JOIN FETCH a.userInRole AS userInRole INNER JOIN a.assessedGefValueSets AS assessedGefValueSets INNER JOIN assessedGefValueSets.geriatricFactorValue AS geriatricFactorValue LEFT JOIN geriatricFactorValue.timeInterval AS timeInterval WHERE geriatricFactorValue.userInRoleId = :userInRoleId AND geriatricFactorValue.timeInterval.intervalStart >= :start AND geriatricFactorValue.timeInterval.intervalEnd <= :end ORDER BY a.created DESC LIMIT '0' '5'");
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setParameter("userInRoleId", patientId);
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

	public List<Object[]> getAssessmentsByFilter(final Long geriatricFactorId, final List<Boolean> status, final Short authorRoleId, final OrderBy orderBy) {
		return castList(Object[].class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuilder sql = new StringBuilder("SELECT DISTINCT a, a.created, a.userInRole.userInSystemId, a.userInRole.roleId FROM Assessment a LEFT JOIN FETCH a.userInRole AS userInRole INNER JOIN a.assessedGefValueSets AS assessedGefValueSets INNER JOIN assessedGefValueSets.geriatricFactorValue AS geriatricFactorValue INNER JOIN a.assessmentAudienceRoles AS assessmentAudienceRoles WHERE geriatricFactorValue.id = :geriatricFactorId ");
				if(status.get(0) && !status.get(1))
					sql.append(" AND a.riskStatus = :riskWarning ");
				if(status.get(1) && !status.get(0))
					sql.append(" AND a.riskStatus = :riskAlert ");
				if(status.get(0) && status.get(1))
					sql.append(" AND (a.riskStatus = :riskWarning OR a.riskStatus = :riskAlert) ");
				if(status.get(2) && !status.get(3))
					sql.append(" AND a.dataValidityStatus = :questionableData ");
				if(status.get(3) && !status.get(2))
					sql.append(" AND a.dataValidityStatus = :faultyData ");
				if(status.get(2) && status.get(3))
					sql.append(" AND (a.dataValidityStatus = :questionableData OR a.dataValidityStatus = :faultyData) ");
				if(status.get(4) && !status.get(0) && !status.get(1) && !status.get(2) && !status.get(3))
					sql.append(" AND a.assessmentComment != NULL ");
				if(status.get(4) && (status.get(0) || !status.get(1) ||status.get(2) || status.get(3)))
					sql.append(" OR a.assessmentComment != NULL ");
				if(authorRoleId != null)
					sql.append(" AND a.userInRole.roleId = :userInRoleId ");
				if(orderBy != null)
					sql.append(" ORDER BY ");
		        switch (orderBy) {
	            case DATE_ASC:  
	            	sql.append(" a.created ASC ");
	                     break;
	            case DATE_DESC:  
	            	sql.append(" a.created DESC ");
	                     break;
	            case AUTHOR_NAME_ASC:  
	            	sql.append(" a.userInRole.userInSystemId ASC ");
	                     break;
	            case AUTHOR_NAME_DESC:  
	            	sql.append(" a.userInRole.userInSystemId DESC ");
	                     break;
	            case AUTHOR_ROLE_ASC:  
	            	sql.append(" a.userInRole.roleId ASC ");
	                     break;
	            case AUTHOR_ROLE_DESC:  
	            	sql.append(" a.userInRole.roleId DESC ");
	                     break;
		        }
				Query q = session.createQuery(sql.toString());
				q.setParameter("geriatricFactorId", geriatricFactorId);
				if(status.get(0))
					q.setParameter("riskWarning", 'W');
				if(status.get(1))
					q.setParameter("riskAlert", 'A');
				if(status.get(2))
					q.setParameter("questionableData", 'V');
				if(status.get(3))
					q.setParameter("faultyData", 'F');
				if(authorRoleId != null)
					q.setParameter("userInRoleId", authorRoleId);
				return q.list();
			}
		}));
	}

}
