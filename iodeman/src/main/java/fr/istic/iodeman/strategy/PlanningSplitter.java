package fr.istic.iodeman.strategy;

import java.util.List;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningSplitter {

	public List<TimeBox> execute(Planning planning);
	
}
