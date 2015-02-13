package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningDataValidator {

	public void configure(Planning planning,
			Collection<Participant> participants, Collection<TimeBox> timeboxes);
	
	public void validate();
	
}
