package fr.istic.iodeman.utils;

import fr.istic.iodeman.model.TimeBox;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeBoxHelper {

    public static Map<String, Integer> parseTimebox(Collection<TimeBox> timeBoxes) {
        Map<String, Integer> resolveTimeBox = new HashMap<String, Integer>();
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

            return !AlgoPlanningUtils.isAvailable(lunchPeriod, timeBox);

        }

        return true;

    }
}
