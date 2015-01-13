package fr.istic.iodeman.strategy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.istic.iodeman.factory.OralDefenseFactory;
import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class AlgoPlanningImplV1 implements AlgoPlanning {

	private Planning planning;
	private List<Room> rooms;
	private Collection<Priority> priorities;
	private Collection<Unavailability> unavailabilities;
	
	private List<OralDefense> results;
	private List<TimeBox> remainingTimeboxes;
	private List<Participant> remainingParticipants;
	
	private Map<TimeBox, Integer> allocationsPerTimebox;
	private Map<Participant, List<TimeBox>> buffer;
	
	private boolean hasNewAllocations;
	
	public void configure(Planning planning) {
		
		this.planning = planning;
		this.rooms = Lists.newArrayList(planning.getRooms());
		this.priorities = AlgoPlanningUtils.sortPrioritiesByWeight(planning.getPriorities());
	
	}
	
	public Collection<OralDefense> execute(List<TimeBox> timeboxes, List<Unavailability> unavailabilities) {
		
		this.results = Lists.newArrayList();
		this.remainingTimeboxes = Lists.newArrayList(timeboxes);
		this.allocationsPerTimebox = Maps.newHashMap();
		this.unavailabilities = Lists.newArrayList(unavailabilities);
		this.buffer = Maps.newHashMap();
		this.remainingParticipants = Lists.newArrayList(planning.getParticipants());
		
		System.out.println("remaining participants: "+remainingParticipants.size());
		
		while (!remainingParticipants.isEmpty()) {
			
			tryAllocation();
			
			if (!remainingParticipants.isEmpty()) {
				forceAllocation();
			}
			
		}
	
		return results;
	
	}
	
	private void tryAllocation() {
		
		hasNewAllocations = false;
		
		boolean b = true;
		while (b) {
			
			for(Participant p : Lists.newArrayList(remainingParticipants)) {
				
				System.out.println("check possibles timeboxes for student "+p.getStudent().getId());
				checkPossiblesTimeboxes(p, Lists.newArrayList(remainingTimeboxes), null);
				
			}
			
			if (hasNewAllocations) {
				System.out.println("try allocation : success");
			}
			
			b = hasNewAllocations;
			hasNewAllocations = false;
		}
		
	}
	
	private void forceAllocation() {
		
		int nb = remainingTimeboxes.size()+1;
		Participant badLuckBryan = null;
		
		for(Participant participant : buffer.keySet()) {
			if (buffer.get(participant).size() < nb) {
				badLuckBryan = participant;
			}
		}
		
		if (badLuckBryan != null) {
			System.out.println("force allocation...");
			allocateTimeBox(buffer.get(badLuckBryan).get(0), badLuckBryan);
		}
		
		if (!hasNewAllocations) {
			forceAllocation();
		}
		
		hasNewAllocations = false;
		
	}
	
	private void checkPossiblesTimeboxes(final Participant participant, List<TimeBox> possibleTbs, Priority priority) {
		
		if (possibleTbs.size() == 1) {
			
			boolean allocated = allocateTimeBox(possibleTbs.get(0), participant);
			if (allocated) return;
			
		}
		
		if (!possibleTbs.isEmpty()){
			
			buffer.put(participant, possibleTbs);
			System.out.println("insert "+possibleTbs.size()+" timeboxes in the buffer");
			
			final Priority nextPriority = getNextPriority(priority);
			
			if (nextPriority != null) {
				
				System.out.println("set priority level to : "+nextPriority.getRole());
				
				// filter the unavaibilities to get only the ones matching the current priority level
				Collection<Unavailability> unavailabilities = Collections2.filter(this.unavailabilities, new Predicate<Unavailability>() {
					public boolean apply(Unavailability a) {
						Person p = a.getPerson();
						return ( p.equals(participant.getStudent())
								|| p.equals(participant.getFollowingTeacher()))
								&& (p.getRole() == nextPriority.getRole());
					}
				});
				
				System.out.println("unavailabilities found: "+unavailabilities.size());
				
				if (!unavailabilities.isEmpty()) {
					for (TimeBox timeBox : Lists.newArrayList(possibleTbs)) {
						
						// Check if there is no unavailabilities for that timebox
						if (!AlgoPlanningUtils.isAvailable(unavailabilities, timeBox)) {
							System.out.println("unavailable timebox found : "
									+(new DateTime(timeBox.getFrom()).toString("dd/MM/yyyy HH:mm")));
							possibleTbs.remove(timeBox);
						}
						
					}
				}
				
				// let's do it again
				checkPossiblesTimeboxes(participant, possibleTbs, nextPriority);
				
			}
			
		}
		
	}
	
	private Priority getNextPriority(Priority from) {
		
		if (priorities != null && !priorities.isEmpty()) {
			
			Iterator<Priority> it = priorities.iterator();
			
			if (from == null) {
				return it.next();
			}
			
			while(it.hasNext()) {
				Priority p = it.next();
				if (p.equals(from)){
					return it.hasNext() ? it.next() : null;
				}
			}
		
		}
		
		return null;
		
	}
	
	
	
	private boolean allocateTimeBox(TimeBox timeBox, Participant participant) {

		if (!remainingTimeboxes.contains(timeBox)) {
			remainingTimeboxes.remove(timeBox);
			System.out.println("error, trying to allocate a timebox that is already allocated!");
			return false;
		}
		
		Integer a = allocationsPerTimebox.get(timeBox);
		if (a == null) a = 0;
		
		Room room = rooms.get(a);

		OralDefense oralDefense;
		
		try {
			
			oralDefense = OralDefenseFactory.createOralDefense(participant, room, timeBox);
			System.out.println("");
			System.out.println("new oral defense created for student "+participant.getStudent().getId());
		
		}catch(IllegalArgumentException ex) {
			
			return false;
		
		}
		
		a++;
		allocationsPerTimebox.put(timeBox, a);
		
		if (a == rooms.size()) {
			remainingTimeboxes.remove(timeBox);
		}
		
		remainingParticipants.remove(participant);
		System.out.println("remaining students : "+remainingParticipants.size());
		System.out.println("");
		
		buffer.remove(participant);

		results.add(oralDefense);
		
		hasNewAllocations = true;
		
		return true;
	}
		
}
