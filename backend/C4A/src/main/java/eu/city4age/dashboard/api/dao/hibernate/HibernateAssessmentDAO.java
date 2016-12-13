package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.dto.DiagramQuerryDTO;
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

    public List<DiagramQuerryDTO> getDiagramDataForUserInRoleId(final Integer crId, final Timestamp start, final Timestamp end) {
		return castList(DiagramQuerryDTO.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT g as gef, g.timeInterval.intervalStart as intervalStart, g.cdDetectionVariable.id as cdvId FROM GeriatricFactorValue g LEFT JOIN g.timeInterval AS timeInterval LEFT JOIN g.cdDetectionVariable AS cdDetectionVariable WHERE g.userInRoleId = :userInRoleId AND g.timeInterval.intervalStart >= :start AND g.timeInterval.intervalEnd <= :end ORDER BY g.timeInterval.intervalStart ASC, g.cdDetectionVariable.id ASC");
				q.setParameter("userInRoleId", crId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setResultTransformer(Transformers.aliasToBean(DiagramQuerryDTO.class));
				return q.list();
			}
		}));	
    }
    
	public List<GeriatricFactorValue> getDiagramDataForUserInRoleId(final Integer crId, final Short dvParentId, final Timestamp start, final Timestamp end) {
		return castList(GeriatricFactorValue.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT DISTINCT g as gef FROM GeriatricFactorValue g RIGHT JOIN g.timeInterval AS timeInterval INNER JOIN g.cdDetectionVariable AS cdDetectionVariable WHERE g.userInRoleId = :userInRoleId AND cdDetectionVariable.parentId = :parentId AND g.timeInterval.intervalStart >= :start AND g.timeInterval.intervalEnd <= :end");
				q.setParameter("userInRoleId", crId);
				q.setParameter("parentId", dvParentId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				return q.list();
			}
		}));	
	}
    
	public List<GeriatricFactorValue> getLastFiveAssessmentsForDiagram(final Integer crId, final Timestamp start, final Timestamp end) {
		return castList(GeriatricFactorValue.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery q = session.createSQLQuery("SELECT DISTINCT gfv.* FROM geriatric_factor_value AS gfv RIGHT JOIN time_interval AS ti ON (gfv.time_interval_id=ti.id) INNER JOIN assessed_gef_value_set AS agvs ON (agvs.gef_value_id = gfv.id) RIGHT JOIN assessment AS a ON (agvs.assessment_id = a.id) WHERE a.id IN (SELECT a.id FROM assessment a ORDER BY created DESC FETCH FIRST 5 ROWS ONLY) AND gfv.user_in_role_id = :userInRoleId AND ti.interval_start >= :start AND ti.interval_end <= :end ");
				q.addEntity("gfv", GeriatricFactorValue.class);
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setParameter("userInRoleId", crId);
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
