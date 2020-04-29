package fr.istic.iodeman.strategy;

import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.TimeBox;

import java.util.Collection;

public interface PlanningDataValidator {

	public void configure(Planning planning,
						  Collection<OralDefense> participants, Collection<TimeBox> timeboxes);
	
	public void validate();
	
}
