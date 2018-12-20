package eu.city4age.dashboard.api.jpa.user.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import eu.city4age.dashboard.api.pojo.domain.AttentionStatus;

public class AttentionEnumUserType implements UserType, Serializable  {
	

    /**
	 * Author: Vladimir Aleksic
	 * Note: This class is not needed for now
	 */
	
	
	private static final long serialVersionUID = 7968115676082680181L;

	static protected Logger logger = LogManager.getLogger(AttentionEnumUserType.class);
	
	private static final int[] SQL_TYPES = {Types.VARCHAR};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @SuppressWarnings("rawtypes")
	public Class returnedClass() {
        return AttentionStatus.Status.class;
    }

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable)value;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
            return true;
        }   
        if (null == x || null == y) {   
            return false;
        }
        return x.equals(y); 
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String typeName = resultSet.getString(names[0]);   
		AttentionStatus.Status result = null;   
        if (!resultSet.wasNull()) {
            result = AttentionStatus.Status.valueOf(typeName.toUpperCase());
        }   
        return result;
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (null == value) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, String.valueOf(((AttentionStatus.Status)value).getName()));
        }
		
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}   



}
