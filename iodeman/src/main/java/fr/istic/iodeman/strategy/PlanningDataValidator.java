package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;

import java.util.Collection;

public interface PlanningDataValidator {

	public void configure(Planning planning,
			Collection<Participant> participants, Collection<TimeBox> timeboxes);
	
	public void validate();
	
}
