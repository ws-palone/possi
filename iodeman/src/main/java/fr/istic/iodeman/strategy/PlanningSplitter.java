package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;

import java.util.List;

public interface PlanningSplitter {

	public List<TimeBox> execute(Planning planning);
	
}
