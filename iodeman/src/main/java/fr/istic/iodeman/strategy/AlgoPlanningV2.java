package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface AlgoPlanningV2 {

	public void configure(Planning planning, Collection<Participant> participants, Collection<TimeBox> timeboxes, Collection<Unavailability> unavailabilities);
	
	public Collection<OralDefense> execute();
	
}
