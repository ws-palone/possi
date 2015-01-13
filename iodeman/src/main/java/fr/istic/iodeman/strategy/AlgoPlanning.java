package fr.istic.iodeman.strategy;

import java.util.Collection;
import java.util.List;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface AlgoPlanning {

	public void configure(Planning planning);
	
	public Collection<OralDefense> execute(List<TimeBox> timeboxes, List<Unavailability> unavailability);
	
}
