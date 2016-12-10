package eu.city4age.dashboard.api.dao;

import eu.city4age.dashboard.api.model.TimeInterval;
import java.sql.Timestamp;
import java.util.List;

public interface TimeIntervalDAO extends BaseDAO {
	
	public List<Object[]> getTimeIntervalsForPeriod(final Timestamp start, final Timestamp end);
        
        public List<TimeInterval> getTimeIntervals(final Timestamp start, final Timestamp end);

}
