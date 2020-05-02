package fr.istic.iodeman.utils;

import fr.istic.iodeman.models.TimeBox;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeBoxHelper {

    public static Map<String, Integer> parseTimebox(Collection<TimeBox> timeBoxes) {
        Map<String, Integer> resolveTimeBox = new HashMap<>();
        int idTimeBox = 0;
        for(TimeBox t : timeBoxes) {
            resolveTimeBox.put(t.getFrom().getDate() + " " + t.getFrom().getHours() + " " + t.getFrom().getMinutes(), idTimeBox++);
        }
        return resolveTimeBox;
    }

    public static Integer find(Map<String, Integer> list, TimeBox timeBox) {
        return list.get(timeBox.getFrom().getDate() + " " + timeBox.getFrom().getHours() + " " + timeBox.getFrom().getMinutes());
    }

    public static boolean isOnLunchBreak(TimeBox lunchBreak, TimeBox timeBox) {

        if (lunchBreak != null && timeBox != null) {

            TimeBox lunchPeriod = new TimeBox(
                    (new DateTime(timeBox.getFrom()))
                            .withTimeAtStartOfDay()
                            .plusMinutes(new DateTime(lunchBreak.getFrom()).minuteOfDay().get())
                            .toDate(),
                    (new DateTime(timeBox.getFrom()))
                            .withTimeAtStartOfDay()
                            .plusMinutes(new DateTime(lunchBreak.getTo()).minuteOfDay().get())
                            .toDate()
            );

            return !isAvailable(lunchPeriod, timeBox);

        }

        return true;

    }

    private static boolean isAvailable(TimeBox unavailablePeriod, TimeBox timeBox) {

        Interval v = new Interval(new DateTime(timeBox.getFrom()), new DateTime(timeBox.getTo()));
        Interval uaV = new Interval(new DateTime(unavailablePeriod.getFrom()), new DateTime(unavailablePeriod.getTo()));
        return !v.contains(uaV) && !uaV.contains(v)
                && !v.overlaps(uaV) && !uaV.overlaps(v)
                && !v.equals(uaV);
    }
}
