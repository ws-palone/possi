package fr.istic.iodeman.utils;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public class AlgoPlanningUtils {

	
	public static boolean isAvailable(Collection<Unavailability> unavailabilities, TimeBox timeBox) {
		
		for(Unavailability unavailability : unavailabilities) {
			if (!isAvailable(unavailability, timeBox)) {
				return false;
			}
		}
		return true;
		
	}
	
	public static boolean isAvailable(Unavailability unavailability, TimeBox timeBox) {
		
		return isAvailable(unavailability.getPeriod(), timeBox);
		
	}
	
	public static boolean isAvailable(TimeBox unavailablePeriod, TimeBox timeBox) {
		
		if (timeBox.getFrom().after(unavailablePeriod.getFrom())
				&& timeBox.getFrom().before(unavailablePeriod.getTo())) {
			return false;
		}else if(timeBox.getTo().after(unavailablePeriod.getFrom())
				&& timeBox.getTo().before(unavailablePeriod.getTo())) {
			return false;
		}else if (timeBox.getFrom().before(unavailablePeriod.getFrom())
				&& timeBox.getTo().after(unavailablePeriod.getTo())) {
			return false;
		}
		
		return true;
	}
	
	public static Collection<Priority> sortPrioritiesByWeight(Collection<Priority> priorities) {
		
		Function<Priority, Integer> getWeightFunction = new Function<Priority, Integer>() {
		    public Integer apply(Priority p) {
		        return p.getWeight();
		    }
		};

		Ordering<Priority> weightOrdering = Ordering.natural().reverse().onResultOf(getWeightFunction);

		ImmutableSortedSet<Priority> sortedPriorities = ImmutableSortedSet.orderedBy(
		    weightOrdering).addAll(priorities).build();
		
		return sortedPriorities;
	}
	
}
