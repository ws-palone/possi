package fr.istic.iodeman.utils;

import java.util.Collection;
import java.util.Comparator;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import fr.istic.iodeman.model.OralDefense;
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
		
		Comparator<Priority> byWeight = new Comparator<Priority>() {
			public int compare(Priority o1, Priority o2) {
				return o1.getWeight().compareTo(o2.getWeight());
			}
		};

		Collection<Priority> sortedPriorities = Ordering.from(byWeight).reverse().sortedCopy(priorities);
		
		return sortedPriorities;
		
	}
	
	public static Collection<OralDefense> sortOralDefensesByStartingDate(Collection<OralDefense> oralDefenses) {
		
		Comparator<OralDefense> byStartingDate = new Comparator<OralDefense>() {
			public int compare(OralDefense o1, OralDefense o2) {
				return o1.getTimebox().getFrom().compareTo(o2.getTimebox().getFrom());
			}
		};

		Collection<OralDefense> sortedOralDefenses = Ordering.from(byStartingDate).sortedCopy(oralDefenses);
		
		return sortedOralDefenses;
		
	}
	
}
