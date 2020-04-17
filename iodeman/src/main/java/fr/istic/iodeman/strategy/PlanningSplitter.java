package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;

import java.util.List;

public interface PlanningSplitter {

	PlanningSplitter execute(Planning planning);

	List<TimeBox> getTimeBoxes();

	List<TimeBox> getTimeBoxesWithoutLunchBreak();

	List<TimeBox> getLunchBreakTimeBoxes();

	int getNbDays();
}
