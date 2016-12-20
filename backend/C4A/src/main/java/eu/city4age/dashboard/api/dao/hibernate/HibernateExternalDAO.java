package eu.city4age.dashboard.api.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import eu.city4age.dashboard.api.dao.ExternalDAO;
import eu.city4age.dashboard.api.model.CareProfile;
import eu.city4age.dashboard.api.model.CdDetectionVariable;
import eu.city4age.dashboard.api.model.CrProfile;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.UserInRole;
import eu.city4age.dashboard.api.model.UserInSystem;

public class HibernateExternalDAO extends HibernateBaseDAO implements ExternalDAO {

	@Autowired
	protected SessionFactory sessionFactory;


	
	public List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(final List<String> parentFactors) {
		return castList(CdDetectionVariable.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT c FROM CdDetectionVariable c WHERE c.detectionVariableType.detectionVariableType IN :gefType");
				q.setParameterList("gefType", parentFactors);
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));
	}

	public List<CdDetectionVariable> getDetectionVariableForDetectionVariableType(final String parentFactor) {
		return castList(CdDetectionVariable.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT c FROM CdDetectionVariable c WHERE c.detectionVariableType.detectionVariableType= :gefType");
				q.setParameter("gefType", parentFactor);
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));
	}

	public List<GeriatricFactorValue> getGeriatricFactorValueForDetectionVariableId(final Long dvId, final Long uId) {
		return castList(GeriatricFactorValue.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT g FROM GeriatricFactorValue g WHERE g.cdDetectionVariable.id = :varId and g.userInRole.id = :userId");
				q.setParameter("varId", dvId);
				q.setParameter("userId", uId);
				q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
				return q.list();
			}
		}));
	}

	public String getUserInSystemUsername(final Long gefId) {
		return cast(String.class, getHibernateTemplate().execute(new HibernateCallback<String>() {
			public String doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT g.userInRole.userInSystem.username FROM GeriatricFactorValue g WHERE g.id = :gefId");
				q.setParameter("gefId", gefId);
				return (String) q.uniqueResult();
			}
		}));
	}

	public List<FrailtyStatusTimeline> getFrailtyStatus(final List<Long> timeintervalIds, final Long uId) {
		return castList(FrailtyStatusTimeline.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT fst FROM FrailtyStatusTimeline fst WHERE fst.timeInterval.id IN :timeintervalIds and fst.userInRole.id = :userId");
				q.setParameterList("timeintervalIds", timeintervalIds);
				q.setParameter("userId", uId);
				return q.list();
			}
		}));
	}

	public List<UserInRole> getUserInRoleByRoleId(final Short roleId) {
		return castList(UserInRole.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT u FROM UserInRole u Where u.roleId = :roleId");
				q.setParameter("roleId", roleId);
				return q.list();
			}
		}));
	}

	public List<CrProfile> getProfileByUserInRoleId(final Long userId) {
		return castList(CrProfile.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT c FROM CrProfile c Where c.userInRole.id = :userId");
				q.setParameter("userId", userId);
				return q.list();
			}
		}));
	}

	public List<CareProfile> getCareProfileByUserInRoleId(final Long userId) {
		return castList(CareProfile.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT c FROM CareProfile c WHERE c.userInRoleId = :userId ");
				q.setParameter("userId", userId);
				return q.list();
			}
		}));
	}

	public List<FrailtyStatusTimeline> getFrailtyStatusByUserInRoleId(final Long userId) {
		return castList(FrailtyStatusTimeline.class, getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT fst FROM FrailtyStatusTimeline fst WHERE fst.userInRole.id = :userId ");
				q.setParameter("userId", userId);
				return q.list();
			}
		}));
	}

	public UserInSystem getUserInSystem(final String username, final String password) {
		return cast(UserInSystem.class, getHibernateTemplate().execute(new HibernateCallback<UserInSystem>() {
			public UserInSystem doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT u FROM UserInSystem u WHERE u.username = :username AND u.password=:password");
				q.setParameter("username", username);
				q.setParameter("password", password);
				return (UserInSystem) q.uniqueResult();
			}
		}));
	}

	public UserInRole getUserInRoleByUserInSystemId(final Long uisId) {
		return cast(UserInRole.class, getHibernateTemplate().execute(new HibernateCallback<UserInRole>() {
			public UserInRole doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT u FROM UserInRole u WHERE u.userInSystem.id = :uisId");
				q.setParameter("uisId", uisId);
				return (UserInRole) q.uniqueResult();
			}
		}));
	}

	public String getUserInSystemUsernameByUserInRoleId(final Long uId) {
		return cast(String.class, getHibernateTemplate().execute(new HibernateCallback<String>() {
			public String doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery("SELECT u.userInSystem.username FROM UserInRole u WHERE u.id = :uId");
				q.setParameter("uId", uId);
				return (String) q.uniqueResult();
			}
		}));
	}

	
}
