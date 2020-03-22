package fr.istic.iodeman.utils;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeBoxHelper {
    public static  Map<String, Integer> getTimeBoxResolved(Planning planning) {
        PlanningSplitter splitter = new PlanningSplitterImpl();
       return parseTimebox(splitter.execute(planning));
    }

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

//    Todo Ajouter une fonction pour redonner les timebox en fonction des crenaux
}
