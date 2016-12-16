package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.AssessmentDAO;
import eu.city4age.dashboard.api.domain.OrderBy;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;

/** Hibernate implementacija dao-a za asesment.
 *
 * @author Milos Holclajtner (milos.holclajtner at login.co.rs)
 */
public class HibernateAssessmentDAO extends HibernateBaseDAO implements AssessmentDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;
    
	public List<TimeInterval> getDiagramDataForUserInRoleId(final Long crId, final Long dvParentId, final Timestamp start, final Timestamp end) {
		return castList(TimeInterval.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT ti FROM TimeInterval ti LEFT JOIN ti.geriatricFactorValues AS geriatricFactorValues INNER JOIN geriatricFactorValues.cdDetectionVariable AS cdDetectionVariable WHERE geriatricFactorValues.userInRole.id = :userInRoleId AND cdDetectionVariable.derivedDetectionVariable.id = :parentId AND ti.intervalStart >= :start AND ti.intervalEnd <= :end");
				q.setParameter("userInRoleId", crId);
				q.setParameter("parentId", dvParentId);
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));	
	}
    
	public List<TimeInterval> getLastFiveAssessmentsForDiagram(final Integer crId, final Timestamp start, final Timestamp end) {
		return castList(TimeInterval.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery q = session.createSQLQuery("SELECT {ti.*} FROM time_interval AS ti LEFT OUTER JOIN (geriatric_factor_value AS gfv LEFT OUTER JOIN (assessed_gef_value_set AS agvs INNER JOIN assessment AS aa ON agvs.assessment_id = aa.id) ON agvs.gef_value_id = gfv.id) ON gfv.time_interval_id=ti.id WHERE ti.interval_start >= :start AND ti.interval_end <= :end AND (aa.id IN (SELECT id FROM (SELECT DISTINCT a1.id,a1.created FROM assessment a1 INNER JOIN assessed_gef_value_set AS agvs1 ON agvs1.assessment_id = a1.id WHERE agvs1.gef_value_id = agvs.gef_value_id ORDER BY a1.created DESC FETCH FIRST 5 ROWS ONLY) t) OR aa.id IS NULL) AND (gfv.user_in_role_id = :userInRoleId OR gfv.id IS NULL) ORDER BY ti.id"); 
				q.addEntity("ti", TimeInterval.class);
				q.setParameter("start", start);
				q.setParameter("end", end);
				q.setParameter("userInRoleId", crId);
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));
	}

	public List<GeriatricFactorValue> getAssessmentsForSelectedDataSet(final List<Long> geriatricFactorIds, final List<Boolean> status, final Short authorRoleId, final OrderBy orderBy) {
		return castList(GeriatricFactorValue.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuilder sql = new StringBuilder("SELECT g FROM GeriatricFactorValue g INNER JOIN g.assessedGefValueSets AS assessedGefValueSets INNER JOIN  assessedGefValueSets.assessment AS assessment INNER JOIN assessment.assessmentAudienceRoles AS assessmentAudienceRoles WHERE g.id IN :geriatricFactorIds ");
				if(status.get(0) && !status.get(1))
					sql.append(" AND assessment.riskStatus = :riskWarning ");
				if(status.get(1) && !status.get(0))
					sql.append(" AND assessment.riskStatus = :riskAlert ");
				if(status.get(0) && status.get(1))
					sql.append(" AND (assessment.riskStatus = :riskWarning OR assessment.riskStatus = :riskAlert) ");
				if(status.get(2) && !status.get(3))
					sql.append(" AND assessment.dataValidityStatus = :questionableData ");
				if(status.get(3) && !status.get(2))
					sql.append(" AND assessment.dataValidityStatus = :faultyData ");
				if(status.get(2) && status.get(3))
					sql.append(" AND (assessment.dataValidityStatus = :questionableData OR assessment.dataValidityStatus = :faultyData) ");
				if(status.get(4) && !status.get(0) && !status.get(1) && !status.get(2) && !status.get(3))
					sql.append(" AND assessment.assessmentComment != NULL ");
				if(status.get(4) && (status.get(0) || !status.get(1) ||status.get(2) || status.get(3)))
					sql.append(" OR assessment.assessmentComment != NULL ");
				if(authorRoleId != null)
					sql.append(" AND assessment.userInRole.roleId = :userInRoleId ");
				if(orderBy != null)
					sql.append(" ORDER BY ");
		        switch (orderBy) {
	            case DATE_ASC:  
	            	sql.append(" assessment.created ASC ");
	                     break;
	            case DATE_DESC:  
	            	sql.append(" assessment.created DESC ");
	                     break;
	            case AUTHOR_NAME_ASC:  
	            	sql.append(" assessment.userInRole.userInSystem.id ASC ");
	                     break;
	            case AUTHOR_NAME_DESC:  
	            	sql.append(" assessment.userInRole.userInSystem.id DESC ");
	                     break;
	            case AUTHOR_ROLE_ASC:  
	            	sql.append(" assessment.userInRole.roleId ASC ");
	                     break;
	            case AUTHOR_ROLE_DESC:  
	            	sql.append(" assessment.userInRole.roleId DESC ");
	                     break;
		        }
				Query q = session.createQuery(sql.toString());
				q.setParameterList("geriatricFactorIds", geriatricFactorIds);
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
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));
	}

}
