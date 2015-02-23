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
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class AlgoPlanningImplV2 implements AlgoPlanningV2 {

	private Planning planning;
	private List<Room> rooms;
	private Collection<Priority> priorities;
	private Collection<Unavailability> unavailabilities;
	
	private List<OralDefense> results;
	private List<TimeBox> remainingTimeboxes;
	private List<Participant> remainingParticipants;
	
	private Map<TimeBox, Integer> allocationsPerTimebox;
	private Map<Participant, List<TimeBox>> buffer;
	
	private Integer nbMaxAllocPerDay;
	
	private boolean isReady = false;
	private boolean hasNewAllocations;
	
	public void configure(Planning planning,
			Collection<Participant> participants, Collection<TimeBox> timeboxes,
			Collection<Unavailability> unavailabilities) {
		/*
		// verify that a planning has been configure correctly
		Validate.notNull(planning);
		Validate.notNull(planning.getRooms());
		Validate.notEmpty(planning.getRooms());
		
		// verify that we have timeboxes
		Validate.notNull(timeboxes);
		Validate.notEmpty(timeboxes);
		
		// verify that we have participants
		Validate.notNull(participants);
		Validate.notEmpty(participants);
		
		// verify that we have enough timeboxes and rooms to create an oral defense for each participant
		Validate.isTrue(timeboxes.size() * planning.getRooms().size() >= participants.size());
		*/
		PlanningDataValidator validator = new PlanningDataValidatorImpl();
		validator.configure(planning, participants, timeboxes);
		validator.validate();
		
				
		this.planning = planning;
		this.rooms = Lists.newArrayList(planning.getRooms());
		
		if (planning.getPriorities() != null) {
			this.priorities = AlgoPlanningUtils.sortPrioritiesByWeight(planning.getPriorities());
		}else{
			this.priorities = Lists.newArrayList();
		}

		this.results = Lists.newArrayList();
		this.remainingTimeboxes = Lists.newArrayList(timeboxes);
		this.allocationsPerTimebox = Maps.newHashMap();
		this.unavailabilities = Lists.newArrayList();
		if (unavailabilities != null) {
			this.unavailabilities.addAll(unavailabilities);
		}
		this.buffer = Maps.newHashMap();
		this.remainingParticipants = Lists.newArrayList(participants);
		
		this.nbMaxAllocPerDay = planning.getNbMaxOralDefensePerDay();
		if (this.nbMaxAllocPerDay == null) this.nbMaxAllocPerDay = 0;
		
		isReady = true;
		
	}

	public Collection<OralDefense> execute() {
		
		Validate.isTrue(isReady);
		
		System.out.println("remaining participants: "+remainingParticipants.size());
		
		// try to allocate a timebox to each participant
		// when considering their unavailabilities
		tryAllocation();
		
		if (!remainingParticipants.isEmpty()) {
			forceAllocation();
		}
	
		return results;	
		
		
	}
	
	private void tryAllocation() {
		
		hasNewAllocations = true;
		
		while (hasNewAllocations) {
			
			hasNewAllocations = false;
			
			for(final Participant p : Lists.newArrayList(remainingParticipants)) {
				
				System.out.println(
						"check possibles timeboxes for student "
						+p.getStudent().getId()
						+"["+p.getStudent().getUid()+"]"
				);
				
				// Get the remaining timeboxes where every participant is free
				List<TimeBox> possibleTbs = Lists.newArrayList();
				for(final TimeBox tb : remainingTimeboxes) {
					Collection<OralDefense> alreadyAssigned = Collections2.filter(results, new Predicate<OralDefense>() {
						@Override
						public boolean apply(OralDefense od) {
							return (od.getComposition().getStudent().equals(p.getStudent())
									|| od.getComposition().getFollowingTeacher().equals(p.getFollowingTeacher()))
									&& (od.getTimebox().getFrom().equals(tb.getFrom()));
						}			
					});
					if (alreadyAssigned.isEmpty()) {
						possibleTbs.add(tb);
					}
				}
				
				checkPossiblesTimeboxes(p, possibleTbs, null);
				
			}
			
			if (hasNewAllocations) {
				System.out.println("try allocation : success");
			}
		
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
			
			UnavailabilityCostValidator validator = new UnavailabilityCostValidatorImpl();
			validator.configure(priorities);
			
			int bestCost = -1;
			
			System.out.println(buffer.get(badLuckBryan).size()+" iterations max needed!");
			
			for (int i=0; i<buffer.get(badLuckBryan).size(); i++) {
				
				AlgoPlanningImplV2 algo = new AlgoPlanningImplV2();
				algo.configure(planning, remainingParticipants, remainingTimeboxes, unavailabilities);
				algo.setAllocationsPerTimebox(allocationsPerTimebox);
				algo.results.addAll(this.results);
				algo.allocateTimeBox(buffer.get(badLuckBryan).get(i), badLuckBryan);
				Collection<OralDefense> res = algo.execute();
				
				int cost = validator.execute(res, unavailabilities);
				
				if(bestCost == -1 || cost < bestCost) {
					System.out.println("better path detected with cost: "+cost);
					this.results.clear();
					this.results.addAll(res);
				}
				
				if (cost == 0) {	
					return;
				}
			}
			
		}
		
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
				Collection<Unavailability> unavailabilities = AlgoPlanningUtils.extractUnavailabilities(this.unavailabilities, participant, nextPriority.getRole());
						
						/*Collections2.filter(this.unavailabilities, new Predicate<Unavailability>() {
					public boolean apply(Unavailability a) {
						Person p = a.getPerson();
						return ( p.equals(participant.getStudent())
								|| p.equals(participant.getFollowingTeacher()))
								&& (p.getRole() == nextPriority.getRole());
					}
				});*/
				
				System.out.println("unavailabilities found: "+unavailabilities.size());
				/*System.out.println("{");
				for(Unavailability ua : unavailabilities) {
					System.out.println(ua.getPeriod().getFrom()+" - "+ua.getPeriod().getTo());
				}
				System.out.println("}");*/
				
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
	
	
	
	protected boolean allocateTimeBox(TimeBox timeBox, Participant participant) {
		
		Integer a = allocationsPerTimebox.get(timeBox);
		if (a == null) a = 0;
		
		Room room = rooms.get(a);

		OralDefense oralDefense;
		
		try {
			
			// create the oral defense
			oralDefense = OralDefenseFactory.createOralDefense(participant, room, timeBox);
			System.out.println("");
			System.out.println("new oral defense created for student "+participant.getStudent().getId()+"["+participant.getStudent().getFirstName()+"]");
		
		}catch(IllegalArgumentException ex) {
			
			return false;
		}
		
		// increment the number of allocations for this timebox
		a++;
		allocationsPerTimebox.put(timeBox, a);
		
		// check if all the rooms has been used on this timebox
		if (a == rooms.size()) {
			remainingTimeboxes.remove(timeBox);
		}
		
		// remove the participant from the list
		remainingParticipants.remove(participant);
		System.out.println("remaining students : "+remainingParticipants.size());
		System.out.println("");
		
		// clear the buffer
		buffer.remove(participant);

		// add the result
		results.add(oralDefense);
		
		// handle max allocations per day
		handleMaxAllocPerDay(timeBox);
		
		hasNewAllocations = true;
		
		return true;
	}
	
	private void handleMaxAllocPerDay(TimeBox timeBox) {
		
		if (nbMaxAllocPerDay > 0) {
			
			final int day = new DateTime(timeBox.getFrom()).dayOfYear().get();
			
			// extract the oral defense on the same day
			Collection<OralDefense> oralDefensesOnSameDay = Collections2.filter(results, new Predicate<OralDefense>() {
				@Override
				public boolean apply(OralDefense od) {
					return (day == new DateTime(od.getTimebox().getFrom()).dayOfYear().get());
				}
			});
			
			if (oralDefensesOnSameDay.size() == nbMaxAllocPerDay) {
				
				// remove all timeboxes left on this day
				for(TimeBox tb : Lists.newArrayList(remainingTimeboxes)) {
					if (day == new DateTime(tb.getFrom()).dayOfYear().get()) {
						remainingTimeboxes.remove(tb);
					}
				}
				
			}
			
		}
		
	}
	
	protected void setAllocationsPerTimebox(Map<TimeBox, Integer> mapAllocationsPerTimeBox) {
		
		this.allocationsPerTimebox = Maps.newHashMap(mapAllocationsPerTimeBox);
	
	}
		
}
