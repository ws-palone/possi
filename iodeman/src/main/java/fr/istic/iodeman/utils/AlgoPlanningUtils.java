package fr.istic.iodeman.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import fr.istic.iodeman.model.*;
import org.apache.commons.lang.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Pattern;

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
		
		Interval v = new Interval(new DateTime(timeBox.getFrom()), new DateTime(timeBox.getTo()));
		Interval uaV = new Interval(new DateTime(unavailablePeriod.getFrom()), new DateTime(unavailablePeriod.getTo()));
		if (v.contains(uaV) || uaV.contains(v)
				|| v.overlaps(uaV) || uaV.overlaps(v)
				|| v.equals(uaV)) {
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
	
	public static Collection<Unavailability> extractUnavailabilities(Collection<Unavailability> unavailabilities, final Participant participant, final Role role) {
		
		if (unavailabilities != null && !unavailabilities.isEmpty()) {
			return Collections2.filter(unavailabilities, new Predicate<Unavailability>() {
				public boolean apply(Unavailability a) {
					Person p = a.getPerson();
					if (p.getRole() == role) {
						return (p.equals(participant.getStudent()) 
								|| p.equals(participant.getFollowingTeacher()));
					}
					return false;
				}
			});
		}
		return Lists.newArrayList();
	}

	public static String emailToName(String email) {
		String name = email;
		if( email !=null && !email.isEmpty()){
			int index = email.indexOf('@');
			if(index > 0) {
				String email2 = email.substring(0, index);
				String[] parts = email2.split(Pattern.quote("."));
				if(parts.length >= 2) {
					String prenom = parts[0]; // 004
					String nom = parts[1];
					name = WordUtils.capitalize(prenom) + " " + nom.toUpperCase();
				}
			}
		}
		return name;
	}
}
