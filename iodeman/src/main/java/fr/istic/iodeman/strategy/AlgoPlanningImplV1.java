package fr.istic.iodeman.strategy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
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
		
		Validate.notNull(planning);
		
		this.planning = planning;
		this.rooms = Lists.newArrayList(planning.getRooms());
		
		if (planning.getPriorities() != null) {
			this.priorities = AlgoPlanningUtils.sortPrioritiesByWeight(planning.getPriorities());
		}else{
			this.priorities = Lists.newArrayList();
		}
		
	}
	
	public Collection<OralDefense> execute(List<TimeBox> timeboxes, List<Unavailability> unavailabilities) {
		
		// verify that a planning has been configure correctly
		Validate.notNull(planning);
		Validate.notNull(planning.getRooms());
		Validate.notEmpty(planning.getRooms());
		
		// verify that we have timeboxes
		Validate.notNull(timeboxes);
		Validate.notEmpty(timeboxes);
		
		// verify that we have participants
		Validate.notNull(planning.getParticipants());
		Validate.notEmpty(planning.getParticipants());
		
		// verify that we have enough timeboxes and rooms to create an oral defense for each participant
		Validate.isTrue(timeboxes.size() * rooms.size() >= planning.getParticipants().size());
		
		this.results = Lists.newArrayList();
		this.remainingTimeboxes = Lists.newArrayList(timeboxes);
		this.allocationsPerTimebox = Maps.newHashMap();
		this.unavailabilities = Lists.newArrayList();
		if (unavailabilities != null) {
			this.unavailabilities.addAll(unavailabilities);
		}
		this.buffer = Maps.newHashMap();
		this.remainingParticipants = Lists.newArrayList(planning.getParticipants());
		
		System.out.println("remaining participants: "+remainingParticipants.size());
		
		while (!remainingParticipants.isEmpty()) {
			
			// try to allocate a timebox to each participant
			// when considering their unavailabilities
			tryAllocation();
			
			// allocate a timebox to a participant from its list
			// of available timebox
			if (!remainingParticipants.isEmpty()) {
				forceAllocation();
			}
			
		} // loop until every participant is allocated to a timebox
	
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
			
			buffer.put(participant, Lists.newArrayList(possibleTbs));
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
				System.out.println("{");
				for(Unavailability ua : unavailabilities) {
					System.out.println(ua.getPeriod().getFrom()+" - "+ua.getPeriod().getTo());
				}
				System.out.println("}");
				
				if (!unavailabilities.isEmpty()) {
					for (TimeBox timeBox : Lists.newArrayList(possibleTbs)) {
						
						System.out.println("check unavailability "+
								(new DateTime(timeBox.getFrom()).toString("dd/MM/yyyy HH:mm")));
						
						// Check if there is no unavailabilities for that timebox
						if (!AlgoPlanningUtils.isAvailable(unavailabilities, timeBox)) {
							System.out.println("removing one timebox...");
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
