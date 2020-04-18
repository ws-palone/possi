package fr.istic.iodeman.strategy;

import fr.istic.iodeman.models.Participant;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.TimeBox;

import java.util.Collection;

public interface PlanningDataValidator {

	public void configure(Planning planning,
			Collection<Participant> participants, Collection<TimeBox> timeboxes);
	
	public void validate();
	
}
