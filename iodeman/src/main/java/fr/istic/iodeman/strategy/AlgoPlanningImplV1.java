package fr.istic.iodeman.strategy;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public class AlgoPlanningImplV1 implements AlgoPlanning {

	private Planning planning;
	private List<Room> rooms;
	private List<Participant> participants;
	private List<Priority> priorities;
	
	private List<OralDefense> results;
	private List<TimeBox> remainingTimeboxes;
	
	public void configure(Planning planning) {
		
		this.planning = planning;
		this.rooms = (List<Room>) planning.getRooms();
		this.participants = (List<Participant>) planning.getParticipants();
		this.priorities = (List<Priority>) planning.getPriorities();
	
	}
	

	public Collection<OralDefense> execute(List<TimeBox> timeboxes, List<Unavailability> unavailability) {
		
		this.results = Lists.newArrayList();
		this.remainingTimeboxes = Lists.newArrayList(timeboxes);
		
		
		
		return results;
	
	}
	
}
