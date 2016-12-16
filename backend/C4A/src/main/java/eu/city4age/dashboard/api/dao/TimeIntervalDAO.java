package eu.city4age.dashboard.api.dao;

import java.sql.Timestamp;
import java.util.List;

import eu.city4age.dashboard.api.model.TimeInterval;

public interface TimeIntervalDAO extends BaseDAO {
	
	public List<TimeInterval> getTimeIntervalsForPeriod(final Timestamp start, final Timestamp end);
        

}
