package eu.city4age.dashboard.api.utils;

import java.sql.Timestamp;

public class TimestampConverter {

		/* method for calculating offset into timestamp of TimeInterval to convert it to UTC time */
	
		public static Timestamp adjustedIntervalTimestamp (Timestamp t, long offset) {
			if (offset == 0) return t;
			else return new Timestamp (t.getTime() - offset);
		}	
		
		/* method for calculating offset into timestamp of Timestamo in UTC time to local */
		
		public static Timestamp adjustedUtcIntervalTimestamp (Timestamp t, long offset) {
			if (offset == 0) return t;
			else return new Timestamp (t.getTime() + offset);
		}
}
