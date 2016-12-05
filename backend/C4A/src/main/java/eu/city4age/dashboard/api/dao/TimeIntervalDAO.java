package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

public interface TimeIntervalDAO extends BaseDAO {
	
	public List<Object[]> getTimeIntervalsForPeriod(final Timestamp start, final Timestamp end);

}
