package eu.city4age.dashboard.api.pojo.comparator;

import java.util.Comparator;

import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;

public class FSTComparator implements Comparator<FrailtyStatusTimeline> {

	@Override
	public int compare(FrailtyStatusTimeline o1, FrailtyStatusTimeline o2) {
		return (o1.getTimeIntervalId().compareTo(o2.getTimeIntervalId()));
	}

}
