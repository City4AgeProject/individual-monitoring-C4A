package eu.city4age.dashboard.api.jpa.user.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

public class JsonStringType implements UserType, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5044179487046093067L;
	
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }
        try {
            st.setObject(index, value.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert to String: " + ex.getMessage(), ex);
        }
    }
 
    @Override
    public Object deepCopy(Object originalValue) throws HibernateException {
        if (originalValue == null) {
            return null;
        }
        if (!(originalValue instanceof String)) {
            return null;
        }
        String resultString = originalValue.toString();
        return resultString;
    }
 
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            return cellContent;
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert from String: " + ex.getMessage(), ex);
        }
    }
 
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object copy = deepCopy(value);
 
        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }
 
        throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
    }
 
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }
 
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
 
    @Override
    public boolean isMutable() {
        return true;
    }
 
    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }
 
        return x.hashCode();
    }
 
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }
 
    @Override
    public Class<?> returnedClass() {
        return String.class;
    }
 
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

}