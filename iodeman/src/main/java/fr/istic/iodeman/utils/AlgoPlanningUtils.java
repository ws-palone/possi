package fr.istic.iodeman.utils;

import fr.istic.iodeman.models.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class AlgoPlanningUtils {


	public static boolean isAvailable(TimeBox unavailablePeriod, TimeBox timeBox) {
		
		Interval v = new Interval(new DateTime(timeBox.getFrom()), new DateTime(timeBox.getTo()));
		Interval uaV = new Interval(new DateTime(unavailablePeriod.getFrom()), new DateTime(unavailablePeriod.getTo()));
		return !v.contains(uaV) && !uaV.contains(v)
				&& !v.overlaps(uaV) && !uaV.overlaps(v)
				&& !v.equals(uaV);
	}

}
